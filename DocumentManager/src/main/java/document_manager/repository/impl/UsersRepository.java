package document_manager.repository.impl;
/*
    Created by KhaiTT
    Time: 10:58 7/6/2022
*/

import document_manager.dto.TotalRecordResponse;
import document_manager.entity.Users;
import document_manager.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsersRepository implements IUsersRepository {
    private static final String GET_USER_BY_ID_QUERY = "select * from users where is_delete = b'0' and id = :id";
    private static final String GET_USER_BY_USER_NAME_QUERY = "select * from users where is_delete = b'0' and user_name = :name";
    private static final String ADD_NEW_USER = "insert into users(user_name) values (:name)";
    private static final String DELETE_USER = "update users set is_delete = b'1' where id = :id ;";
    private static final String UPDATE_USER = "update users set user_name = :name where id = :id";
    @Lazy
    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Users> findAll(String name, Long pageNumber, Long elementNumber) {
        StringBuilder findAllQuery = new StringBuilder("select * from users where is_delete = b'0' ");
        if (!name.equals("")) {
            findAllQuery.append("and unaccent(users.user_name) ilike unaccent( :name ) ");
        }
        findAllQuery.append("order by id limit :limit offset :offSet ");
        TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(String.valueOf(findAllQuery), Users.class);
        if (!name.equals("")) {
            query.setParameter("name", '%' + name + '%');
        }
        query.setParameter("limit", elementNumber);
        query.setParameter("offSet", elementNumber * pageNumber);
        return query.getResultList();
    }

    @Override
    public Users findByName(String name) {
        try {
            Query query = entityManager.createNativeQuery(GET_USER_BY_USER_NAME_QUERY, Users.class);
            query.setParameter("name", name);
            return (Users) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Users findById(Long id) {
        try {
            TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(GET_USER_BY_ID_QUERY, Users.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void save(Users users) {
        TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(ADD_NEW_USER, Users.class);
        query.setParameter("name", users.getUserName());
        query.executeUpdate();
    }

    @Override
    public void delete(Long id) {
        TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(DELETE_USER, Users.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Users users) {
        TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(UPDATE_USER, Users.class);
        query.setParameter("name", users.getUserName());
        query.setParameter("id", users.getId());
        query.executeUpdate();
    }

    @Override
    public long totalRecordNumber(String name) {
        final long[] row = {0};
        StringBuilder countAllQuery = new StringBuilder("select count(*) from users where is_delete = b'0' ");
        if (!name.equals("")) {
            countAllQuery.append("and unaccent(users.user_name) ilike unaccent( ? ) ");
        }

        jdbcTemplate.query(String.valueOf(countAllQuery),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        if (!name.equals("")) {
                            ps.setString(1, '%' + name + '%');
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
    public List<Users> findAllByFullTextSearch(String name, Long pageNumber, Long elementNumber) {
        StringBuilder getListQuery = new StringBuilder("select * from users where is_delete = b'0' ");
        if (!name.equals("")) {
            getListQuery.append("and to_tsvector(unaccent(users.user_name)) @@ to_tsquery(unaccent( :name )) ");
        }
        getListQuery.append("order by id limit :limit offset :offSet ");
        TypedQuery<Users> query = (TypedQuery<Users>) entityManager.createNativeQuery(String.valueOf(getListQuery), Users.class);
        if (!name.equals("")) {
            query.setParameter("name", name);
        }
        query.setParameter("limit", elementNumber);
        query.setParameter("offSet", elementNumber * pageNumber);
        return query.getResultList();
    }

    @Override
    public long totalRecordNumberOfFullTextSearch(String name) {
        final long[] row = {0};
        StringBuilder countAllQuery = new StringBuilder("select count(*) from users where is_delete = b'0' ");
        if (!name.equals("")) {
            countAllQuery.append("and to_tsvector(unaccent(users.user_name)) @@ to_tsquery(unaccent( ? )) ");
        }
        jdbcTemplate.query(String.valueOf(countAllQuery),
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        if (!name.equals("")) {
                            ps.setString(1, name);
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
}
