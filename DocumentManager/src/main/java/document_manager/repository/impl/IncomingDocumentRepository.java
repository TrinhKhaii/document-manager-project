package document_manager.repository.impl;
/*
    Created by KhaiTT
    Time: 16:52 7/6/2022
*/

import document_manager.dto.IncomingDocumentResponse;
import document_manager.dto.TotalRecordResponse;
import document_manager.entity.IncomingDocument;
import document_manager.repository.IIncomingDocumentRepository;
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
public class IncomingDocumentRepository implements IIncomingDocumentRepository {
    private static final String GET_LIST_QUERY = "select * from incoming_document inner join users on incoming_document.signer_id = users.id where incoming_document.is_delete = b'0' and excerpt ilike :excerpt " +
            "and serial_number ilike :serialNumber and signing_date between cast(:fromDate as timestamp) and cast(:toDate as timestamp) " +
            "and users.user_name ilike :signerName order by incoming_document.id limit :limit offset :offSet ;";

    private static final String SAVE_INCOMING_DOCUMENT = "insert into incoming_document(excerpt, serial_number, signer_id) values " +
            "(:excerpt, :serialNumber, :signerId );";
    private static final String FIND_BY_SERIAL_NUMBER = "select * from incoming_document where is_delete = b'0' and serial_number = :serialNumber ;";
    private static final String DELETE_BY_ID = "update incoming_document set is_delete = b'1' where id = :id ;";
    private static final String FIND_BY_ID = "select * from incoming_document where is_delete = b'0' and id = :id ;";
    private static final String UPDATE_INCOMING_DOCUMENT = "update incoming_document set excerpt = :excerpt, serial_number = :serialNumber, signer_id = :signerId where id = :id ";

    @Lazy
    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<IncomingDocument> findAll(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber) {
        TypedQuery<IncomingDocument> query = (TypedQuery<IncomingDocument>) entityManager.createNativeQuery(GET_LIST_QUERY, IncomingDocument.class);
        query.setParameter("excerpt", '%' + excerpt + '%');
        query.setParameter("serialNumber", '%' + serialNumber + '%');
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("signerName", '%' + signerName + '%');
        query.setParameter("limit", elementNumber);
        query.setParameter("offSet", elementNumber * pageNumber);
        return query.getResultList();
    }

    @Override
    public List<IncomingDocumentResponse> findAllDocumentDto(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber) {
        System.out.println("findAllDocumentDto with page " + pageNumber);
        AtomicInteger count = new AtomicInteger(2);
        StringBuffer getListQuery = new StringBuffer("select incoming_document.id, incoming_document.excerpt, incoming_document.serial_number, incoming_document.signing_date, users.id as signer_id, users.user_name as signer_name from incoming_document " +
                "inner join users on incoming_document.signer_id = users.id where incoming_document.is_delete = b'0' " +
                "and signing_date between cast( ? as timestamp) and cast( ? as timestamp) ");
        if (!excerpt.equals("")) {
            getListQuery.append("and excerpt ilike ? ");
        }
        if (!serialNumber.equals("")) {
            getListQuery.append("and serial_number ilike ? ");
        }
        if (!signerName.equals("")) {
            getListQuery.append("and users.user_name ilike ? ");
        }
        getListQuery.append("order by incoming_document.id limit ? offset ? ");
        return jdbcTemplate.query(String.valueOf(getListQuery),
                ps -> {
                    ps.setString(1, fromDate);
                    ps.setString(2, toDate);
                    if (!excerpt.equals("")) {
                        ps.setString(count.incrementAndGet(), '%' + excerpt + '%');
                    }
                    if (!serialNumber.equals("")) {
                        ps.setString(count.incrementAndGet(), '%' + serialNumber + '%');
                    }
                    if (!signerName.equals("")) {
                        ps.setString(count.incrementAndGet(), '%' + signerName + '%');
                    }
                    ps.setLong(count.incrementAndGet(), elementNumber);
                    ps.setLong(count.incrementAndGet(), elementNumber * pageNumber);
                }, new RowMapper<>() {
                    @Override
                    public IncomingDocumentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        IncomingDocumentResponse incomingDocumentResponse = new IncomingDocumentResponse();
                        incomingDocumentResponse.setId(rs.getLong(1));
                        incomingDocumentResponse.setExcerpt(rs.getString(2));
                        incomingDocumentResponse.setSerialNumber(rs.getString(3));
                        incomingDocumentResponse.setSigningDate(LocalDateTime.parse(rs.getString(4).replace(" ", "T")));
                        incomingDocumentResponse.setSignerId(rs.getLong(5));
                        incomingDocumentResponse.setSignerName(rs.getString(6));
                        return incomingDocumentResponse;
                    }
                });
    }

    @Override
    public IncomingDocument findBySerialNumber(String serialNumber) {
        try {
            TypedQuery<IncomingDocument> query = (TypedQuery<IncomingDocument>) entityManager.createNativeQuery(FIND_BY_SERIAL_NUMBER, IncomingDocument.class);
            query.setParameter("serialNumber", serialNumber);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public long totalRecordNumber(String excerpt, String serialNumber, String fromDate, String toDate, String signerName) {
        final long[] row = {0};
        AtomicInteger count = new AtomicInteger(2);
        StringBuffer getCountListQuery = new StringBuffer("select count(*) from incoming_document " +
                "inner join users on incoming_document.signer_id = users.id where incoming_document.is_delete = b'0' " +
                "and signing_date between cast( ? as timestamp) and cast( ? as timestamp) ");
        if (!excerpt.equals("")) {
            getCountListQuery.append("and excerpt ilike ? ");
        }
        if (!serialNumber.equals("")) {
            getCountListQuery.append("and serial_number ilike ? ");
        }
        if (!signerName.equals("")) {
            getCountListQuery.append("and users.user_name ilike ? ");
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
                        if (!serialNumber.equals("")) {
                            ps.setString(count.incrementAndGet(), '%' + serialNumber + '%');
                        }
                        if (!signerName.equals("")) {
                            ps.setString(count.incrementAndGet(), '%' + signerName + '%');
                        }
                    }
                }, new RowMapper<TotalRecordResponse>() {
                    @Override
                    public TotalRecordResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        row[0] = rs.getLong(1);
                        return new TotalRecordResponse();
                    }
                });
        return row[0];
    }

    @Override
    public IncomingDocument findById(Long id) {
        try {
            TypedQuery<IncomingDocument> query = (TypedQuery<IncomingDocument>) entityManager.createNativeQuery(FIND_BY_ID, IncomingDocument.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void save(IncomingDocument incomingDocument) {
        TypedQuery<IncomingDocument> query = (TypedQuery<IncomingDocument>) entityManager.createNativeQuery(SAVE_INCOMING_DOCUMENT, IncomingDocument.class);
        query.setParameter("excerpt", incomingDocument.getExcerpt());
        query.setParameter("serialNumber", incomingDocument.getSerialNumber());
//        query.setParameter("signingDate", incomingDocument.getSigningDate());
        query.setParameter("signerId", incomingDocument.getSignerId());
        query.executeUpdate();
    }

    @Override
    public void delete(Long id) {
        TypedQuery query = (TypedQuery) entityManager.createNativeQuery(DELETE_BY_ID, IncomingDocument.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(IncomingDocument incomingDocument) {
        TypedQuery<IncomingDocument> query = (TypedQuery<IncomingDocument>) entityManager.createNativeQuery(UPDATE_INCOMING_DOCUMENT, IncomingDocument.class);
        query.setParameter("excerpt", incomingDocument.getExcerpt());
        query.setParameter("serialNumber", incomingDocument.getSerialNumber());
        query.setParameter("signerId", incomingDocument.getSignerId());
        query.setParameter("id", incomingDocument.getId());
        query.executeUpdate();
    }

    @Override
    public List<IncomingDocumentResponse> findAllDocumentDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber) {
        AtomicInteger count = new AtomicInteger(0);
        StringBuilder getListQuery = new StringBuilder("select incoming_document.id, incoming_document.excerpt, incoming_document.serial_number, incoming_document.signing_date, users.id as signer_id, users.user_name as signer_name from incoming_document " +
                "inner join users on incoming_document.signer_id = users.id where incoming_document.is_delete = b'0' ");
        if (!searchParam.equals("")) {
            getListQuery.append("and to_tsvector(unaccent(excerpt) || ' ' || unaccent(serial_number) || ' ' || unaccent(users.user_name) || ' ' || to_char(signing_date, 'yyyy-MM-dd hh:MM:ss')) @@ to_tsquery(unaccent( ? )) ");
        }
        getListQuery.append("order by incoming_document.id limit ? offset ? ");
        return jdbcTemplate.query(String.valueOf(getListQuery),
                ps -> {
                    if (!searchParam.equals("")) {
                        ps.setString(count.incrementAndGet(), searchParam);
                    }
                    ps.setLong(count.incrementAndGet(), elementNumber);
                    ps.setLong(count.incrementAndGet(), elementNumber * pageNumber);
                }, new RowMapper<>() {
                    @Override
                    public IncomingDocumentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        IncomingDocumentResponse incomingDocumentResponse = new IncomingDocumentResponse();
                        incomingDocumentResponse.setId(rs.getLong(1));
                        incomingDocumentResponse.setExcerpt(rs.getString(2));
                        incomingDocumentResponse.setSerialNumber(rs.getString(3));
                        incomingDocumentResponse.setSigningDate(LocalDateTime.parse(rs.getString(4).replace(" ", "T")));
                        incomingDocumentResponse.setSignerId(rs.getLong(5));
                        incomingDocumentResponse.setSignerName(rs.getString(6));
                        return incomingDocumentResponse;
                    }
                });
    }

    @Override
    public long totalRecordNumberWithFullTextSearch(String searchParam) {
        final long[] row = {0};
        StringBuilder totalCountQuery = new StringBuilder("select count(*) from incoming_document " +
                "inner join users on incoming_document.signer_id = users.id where incoming_document.is_delete = b'0' ");
        if (!searchParam.equals("")) {
            totalCountQuery.append("and to_tsvector(unaccent(excerpt) || ' ' || unaccent(serial_number) || ' ' || unaccent(users.user_name) || ' ' || to_char(signing_date, 'yyyy-MM-dd hh:MM:ss')) @@ to_tsquery(unaccent( ? )) ");
        }
        jdbcTemplate.query(String.valueOf(totalCountQuery),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        if (!searchParam.equals("")) {
                            ps.setString(1, searchParam);
                        }
                    }
                }, new RowMapper<TotalRecordResponse>() {
                    @Override
                    public TotalRecordResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        row[0] = rs.getLong(1);
                        return new TotalRecordResponse();
                    }
                });
        return row[0];
    }

}
