package document_manager.repository.impl;
/*
    Created by KhaiTT
    Time: 10:55 7/7/2022
*/

import document_manager.dto.*;
import document_manager.entity.Rotation;
import document_manager.repository.IRotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RotationRepository implements IRotationRepository {
    private static final String FIND_ALL = "select * from rotation where is_delete = b'0' " +
            "and incoming_document_id in (select incoming_document.id from incoming_document where is_delete = b'0' " +
            " and lower(incoming_document.excerpt) like lower(:excerpt)) " +
            "and sender_id in (select users.id from users where is_delete = b'0' and lower(user_name) like lower(:senderName)) " +
            "and receiver_id in (select users.id from users where is_delete = b'0' and lower(user_name) like lower(:receiverName)) " +
            "and delivery_date between cast(:fromDate as timestamp) and cast(:toDate as timestamp) " +
            "order by id limit :limit offset :offSet ;";

    private static final String ADD_NEW = "insert into rotation(incoming_document_id, sender_id, receiver_id, delivery_date) values " +
            "(:incomingDocumentId, :senderId, :receiverId, :deliveryDate);";

    private static final String DELETE = "update rotation set is_delete = b'1' where id = :id ;";

    private static final String FIND_BY_ID = "select * from rotation where is_delete = b'0' and id = :id ;";

    private static final String UPDATE_ROTATION = "update rotation set incoming_document_id = :incomingDocumentId, sender_id = :senderId, receiver_id = :receiverId where id = :id ;";

    private static final String ROTATION_LIST = "select rotation.id, incoming_document.excerpt, incoming_document.serial_number, incoming_document.signing_date,\n" +
            "rotation.delivery_date, sender.user_name as sender, receiver.user_name as receiver, signer.user_name as signer " +
            "from rotation " +
            "join incoming_document on rotation.incoming_document_id = incoming_document.id " +
            "join users as sender on rotation.sender_id = sender.id " +
            "join users as receiver on rotation.receiver_id = receiver.id " +
            "join users as signer on incoming_document.signer_id = signer.id " +
            "where incoming_document.id = ? and rotation.is_delete = b'0' and incoming_document.is_delete = b'0' and signer.is_delete = b'0';";
    private static final String DOCUMENT_AMOUNT = "select users.user_name as receiver_name, count(receiver_id) as document_amount from rotation " +
            "inner join users on rotation.receiver_id = users.id " +
            "inner join incoming_document on rotation.incoming_document_id = incoming_document.id " +
            "where users.is_delete = b'0' and incoming_document.is_delete = b'0' " +
            "and rotation.is_delete = b'0' " +
            "group by users.id ";

    @Lazy
    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Rotation> findAll(String excerpt, String senderName, String receiverName, String fromDate, String toDate, Long pageNumber, Long elementNumber) {
        TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(FIND_ALL, Rotation.class);
        query.setParameter("excerpt", '%' + excerpt + '%');
        query.setParameter("senderName", '%' + senderName + '%');
        query.setParameter("receiverName", '%' + receiverName + '%');
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("limit", elementNumber);
        query.setParameter("offSet", elementNumber * pageNumber);
        return query.getResultList();
    }

    @Override
    public List<RotationListOfDocumentResponse> findAllRotationListOfDocumentById(Long id) {
        return jdbcTemplate.query(ROTATION_LIST,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, id);
                    }
                }, new RowMapper<RotationListOfDocumentResponse>() {
                    @Override
                    public RotationListOfDocumentResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        RotationListOfDocumentResponse rotationListOfDocumentResponse = new RotationListOfDocumentResponse();
                        rotationListOfDocumentResponse.setId(resultSet.getLong(1));
                        rotationListOfDocumentResponse.setExcerpt(resultSet.getString(2));
                        rotationListOfDocumentResponse.setSerialNumber(resultSet.getString(3));
                        rotationListOfDocumentResponse.setSigningDate(resultSet.getString(4));
                        rotationListOfDocumentResponse.setDeliveryDate(resultSet.getString(5));
                        rotationListOfDocumentResponse.setSender(resultSet.getString(6));
                        rotationListOfDocumentResponse.setReceiver(resultSet.getString(7));
                        rotationListOfDocumentResponse.setSigner(resultSet.getString(8));
                        return rotationListOfDocumentResponse;
                    }
                });
    }

    @Override
    public List<DocumentAmountReceivedResponse> getDocumentAmountReceivedList() {
        return jdbcTemplate.query(DOCUMENT_AMOUNT,
                new RowMapper<DocumentAmountReceivedResponse>() {
                    @Override
                    public DocumentAmountReceivedResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        DocumentAmountReceivedResponse documentAmountReceivedResponse = new DocumentAmountReceivedResponse();
                        documentAmountReceivedResponse.setReceiverName(resultSet.getString(1));
                        documentAmountReceivedResponse.setDocumentAmount(resultSet.getLong(2));
                        return documentAmountReceivedResponse;
                    }
                });
    }

    @Override
    public long totalRecordNumber(String excerpt, String senderName, String receiverName, String fromDate, String toDate) {
        final long[] row = {0};
        AtomicInteger count = new AtomicInteger(2);
        StringBuilder getCountListQuery = new StringBuilder("select count(*) from rotation " +
                "inner join incoming_document on rotation.incoming_document_id = incoming_document.id " +
                "inner join users as sender on rotation.sender_id = sender.id " +
                "inner join users as receiver on rotation.receiver_id = receiver.id " +
                "where rotation.is_delete = b'0' " +
                "and delivery_date between cast( ? as timestamp) and cast( ? as timestamp) ");
        if (!excerpt.equals("")) {
            getCountListQuery.append("and incoming_document.is_delete = b'0' and incoming_document.excerpt ilike ? ");
        }
        if (!senderName.equals("")) {
            getCountListQuery.append("and sender.user_name ilike ? ");
        }
        if (!receiverName.equals("")) {
            getCountListQuery.append("and receiver.user_name ilike ? ");
        }
        jdbcTemplate.query(String.valueOf(getCountListQuery),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, fromDate);
                        ps.setString(2, toDate);
                        if (!excerpt.equals("")) {
                            ps.setString(count.incrementAndGet(), '%' + excerpt + '%');
                        }
                        if (!senderName.equals("")) {
                            ps.setString(count.incrementAndGet(), '%' + senderName + '%');
                        }
                        if (!receiverName.equals("")) {
                            ps.setString(count.incrementAndGet(), '%' + receiverName + '%');
                        }
                    }
                },
                new RowMapper<TotalRecordResponse>() {
                    @Override
                    public TotalRecordResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        row[0] = rs.getLong(1);
                        return new TotalRecordResponse();
                    }
                });
        return row[0];
    }

    @Override
    public void saveMulti(RotationMultiRequest rotationMultiRequest) {
        StringBuilder addNewMulQuery = new StringBuilder("insert into rotation(incoming_document_id, sender_id, receiver_id) values ");
        List<Long> receiverIdList = rotationMultiRequest.getReceiverId();
        for (Long aLong : receiverIdList) {
            addNewMulQuery.append("(").append(rotationMultiRequest.getIncomingDocumentId()).append(", ").append(rotationMultiRequest.getSenderId()).append(", ").append(aLong.toString()).append(" ), ");
        }
        String query1 =  addNewMulQuery.substring(0, addNewMulQuery.length() - 2);
        TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(query1, Rotation.class);
        query.executeUpdate();
    }

    @Override
    public List<RotationResponse> findAllRotationDto(String excerpt, String senderName, String receiverName, String fromDate, String toDate, Long pageNumber, Long elementNumber) {
        AtomicInteger count = new AtomicInteger(2);
        StringBuilder getListQuery = new StringBuilder("select rotation.id, rotation.incoming_document_id, incoming_document.excerpt, rotation.sender_id, sender.user_name as sender_name, " +
                "rotation.receiver_id, receiver.user_name as receiver_name, rotation.delivery_date from rotation " +
                "inner join incoming_document on rotation.incoming_document_id = incoming_document.id " +
                "inner join users as sender on rotation.sender_id = sender.id " +
                "inner join users as receiver on rotation.receiver_id = receiver.id " +
                "where rotation.is_delete = b'0' " +
                "and delivery_date between cast( ? as timestamp) and cast( ? as timestamp) ");
        if (!excerpt.equals("")) {
            getListQuery.append("and incoming_document.is_delete = b'0' and incoming_document.excerpt ilike ? ");
        }
        if (!senderName.equals("")) {
            getListQuery.append("and sender.user_name ilike ? ");
        }
        if (!receiverName.equals("")) {
            getListQuery.append("and receiver.user_name ilike ? ");
        }
        getListQuery.append("order by rotation.id limit ? offset ? ");
        return jdbcTemplate.query(String.valueOf(getListQuery), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, fromDate);
                ps.setString(2, toDate);
                if (!excerpt.equals("")) {
                    ps.setString(count.incrementAndGet(), '%' + excerpt + '%');
                }
                if (!senderName.equals("")) {
                    ps.setString(count.incrementAndGet(), '%' + senderName + '%');
                }
                if (!receiverName.equals("")) {
                    ps.setString(count.incrementAndGet(), '%' + receiverName + '%');
                }
                ps.setLong(count.incrementAndGet(), elementNumber);
                ps.setLong(count.incrementAndGet(), elementNumber * pageNumber);
            }
        }, new RowMapper<>() {
            @Override
            public RotationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                RotationResponse rotationResponse = new RotationResponse();
                rotationResponse.setId(rs.getLong(1));
                rotationResponse.setIncomingDocumentId(rs.getLong(2));
                rotationResponse.setIncomingDocumentExcerpt(rs.getString(3));
                rotationResponse.setSenderId(rs.getLong(4));
                rotationResponse.setSenderName(rs.getString(5));
                rotationResponse.setReceiverId(rs.getLong(6));
                rotationResponse.setReceiverName(rs.getString(7));
                rotationResponse.setDeliveryDate(LocalDateTime.parse(rs.getString(8).replace(" ", "T")));
                return rotationResponse;
            }
        });
    }

    @Override
    public Rotation findById(Long id) {
        try {
            TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(FIND_BY_ID, Rotation.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Rotation rotation) {
        TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(ADD_NEW, Rotation.class);
        query.setParameter("incomingDocumentId", rotation.getIncomingDocumentId());
        query.setParameter("senderId", rotation.getSenderId());
        query.setParameter("receiverId", rotation.getReceiverId());
        query.setParameter("deliveryDate", rotation.getDeliveryDate());
        query.executeUpdate();
    }

    @Override
    public void delete(Long id) {
        TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(DELETE, Rotation.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Rotation rotation) {
        TypedQuery<Rotation> query = (TypedQuery<Rotation>) entityManager.createNativeQuery(UPDATE_ROTATION, Rotation.class);
        query.setParameter("incomingDocumentId", rotation.getIncomingDocumentId());
        query.setParameter("senderId", rotation.getSenderId());
        query.setParameter("receiverId", rotation.getReceiverId());
        query.setParameter("id", rotation.getId());
        query.executeUpdate();
    }

    @Override
    public List<RotationResponse> findAllRotationDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber) {
        StringBuilder getListQuery = new StringBuilder("select rotation.id, rotation.incoming_document_id, incoming_document.excerpt, rotation.sender_id, sender.user_name as sender_name, " +
                "rotation.receiver_id, receiver.user_name as receiver_name, rotation.delivery_date from rotation " +
                "inner join incoming_document on rotation.incoming_document_id = incoming_document.id " +
                "inner join users as sender on rotation.sender_id = sender.id " +
                "inner join users as receiver on rotation.receiver_id = receiver.id " +
                "where rotation.is_delete = b'0' and incoming_document.is_delete = b'0' ");
        if (!searchParam.equals("")) {
            getListQuery.append("and to_tsvector(unaccent(incoming_document.excerpt) || ' ' || unaccent(sender.user_name) || ' ' || unaccent(receiver.user_name) || ' ' || to_char(delivery_date, 'yyyy-MM-dd hh:MM:ss')) @@ to_tsquery(unaccent( ? )) ");
        }
        getListQuery.append("order by rotation.id limit ? offset ? ");
        AtomicInteger count = new AtomicInteger(0);
        return jdbcTemplate.query(String.valueOf(getListQuery), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (!searchParam.equals("")) {
                    ps.setString(count.incrementAndGet(), searchParam);
                }
                ps.setLong(count.incrementAndGet(), elementNumber);
                ps.setLong(count.incrementAndGet(), elementNumber * pageNumber);
            }
        }, new RowMapper<>() {
            @Override
            public RotationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                RotationResponse rotationResponse = new RotationResponse();
                rotationResponse.setId(rs.getLong(1));
                rotationResponse.setIncomingDocumentId(rs.getLong(2));
                rotationResponse.setIncomingDocumentExcerpt(rs.getString(3));
                rotationResponse.setSenderId(rs.getLong(4));
                rotationResponse.setSenderName(rs.getString(5));
                rotationResponse.setReceiverId(rs.getLong(6));
                rotationResponse.setReceiverName(rs.getString(7));
                rotationResponse.setDeliveryDate(LocalDateTime.parse(rs.getString(8).replace(" ", "T")));
                return rotationResponse;
            }
        });
    }

    @Override
    public long totalRecordNumberWithFullTextSearch(String searchParam) {
        final long[] row = {0};
        StringBuilder totalCountQuery = new StringBuilder("select count(*) from rotation " +
                "inner join incoming_document on rotation.incoming_document_id = incoming_document.id " +
                "inner join users as sender on rotation.sender_id = sender.id " +
                "inner join users as receiver on rotation.receiver_id = receiver.id " +
                "where rotation.is_delete = b'0' and incoming_document.is_delete = b'0' ");
        if (!searchParam.equals("")) {
            totalCountQuery.append("and to_tsvector(unaccent(incoming_document.excerpt) || ' ' || unaccent(sender.user_name) || ' ' || unaccent(receiver.user_name) || ' ' || to_char(delivery_date, 'yyyy-MM-dd hh:MM:ss')) @@ to_tsquery(unaccent( ? )) ");
        }
        jdbcTemplate.query(String.valueOf(totalCountQuery),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        if (!searchParam.equals("")) {
                            ps.setString(1, searchParam);
                        }
                    }
                },
                new RowMapper<TotalRecordResponse>() {
                    @Override
                    public TotalRecordResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        row[0] = rs.getLong(1);
                        return new TotalRecordResponse();
                    }
                });
        return row[0];
    };
}
