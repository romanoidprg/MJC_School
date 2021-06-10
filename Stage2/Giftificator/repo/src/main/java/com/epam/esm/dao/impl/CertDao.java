package com.epam.esm.dao.impl;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {
    private static final String SQL_CREATE_CERT = "INSERT INTO gift_certificates " +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";
    private static final String SQL_CREATE_CERT_TAG = "INSERT INTO certs_tags VALUES (?, ?)";
    private static final String SQL_READ_CERT_BY_ID = "SELECT c.*, t.* " +
            "FROM (gift_certificates AS c LEFT JOIN certs_tags AS ct ON c.id=ct.cert_id) " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id " +
            "WHERE c.id=?";
    private static final String SQL_DELETE_CERT = "DELETE FROM gift_certificates WHERE id=?";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT * FROM tags WHERE name=?";
    private static final String SQL_READ_CERT_BY_CRITERIA = "SELECT c.*, t.* " +
            "FROM (gift_certificates AS c LEFT JOIN certs_tags AS ct ON c.id=ct.cert_id) " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id " +
            "WHERE t.name LIKE ? AND c.name LIKE ? AND c.description LIKE ? " +
            "ORDER BY ";
    private static final String SQL_NAME = "c.name ";
    private static final String SQL_CREATE_DATE = "c.create_date ";
    private static final String SQL_UPDATE_DATE = "c.last_update_date ";
    private static final String SQL_ID = "c.id";

    @Autowired
    private ConnectionPool connectionPool;

//    public CertDao(ConnectionPool connectionPool) {
//        this.connectionPool = connectionPool;
//    }

    @Override
    public boolean create(GiftCertificate entity) {
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
            //todo realize transaction
            ResultSet rs;
            PreparedStatement st = c.prepareStatement(SQL_CREATE_CERT, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, entity.getName());
            st.setString(2, entity.getDescription());
            st.setInt(3, entity.getPrice());
            st.setInt(4, entity.getDuration());
            st.setString(5, entity.getCreateDate());
            st.setString(6, entity.getLastUpdateDate());
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            rs.next();
            long certId = rs.getLong(1);
            for (Tag tag : entity.getTags()) {
                long tagId = getTagIdIfTagWithNameExist(tag.getName(), c);
                if (tagId == -1) {
                    st = c.prepareStatement(SQL_CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
                    st.setString(1, tag.getName());
                    st.executeUpdate();
                    rs = st.getGeneratedKeys();
                    rs.next();
                    tagId = rs.getLong(1);
                }
                st = c.prepareStatement(SQL_CREATE_CERT_TAG);
                st.setLong(1, certId);
                st.setLong(2, tagId);
                st.execute();

            }
            result = true;
        } catch (
                SQLException e) {
            //todo logging exception
        }
        return result;
    }

    private long getTagIdIfTagWithNameExist(String name, Connection c) throws SQLException {
        long result = -1;
        PreparedStatement st = c.prepareStatement(SQL_FIND_TAG_BY_NAME);
        st.setString(1, name);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            result = rs.getLong(1);
        }
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
        //todo what the best return - null or cert with empty fields if nothing found?
        GiftCertificate result = null;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_READ_CERT_BY_ID);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString(2);
                String description = rs.getString(3);
                int price = rs.getInt(4);
                int duration = rs.getInt(5);
                String createDate = rs.getString(6);
                String lastUpdateDate = rs.getString(7);
                List<Tag> tags = new ArrayList<>();
                if (rs.getString(9) != null) {
                    do {
                        tags.add(new Tag(rs.getLong(8), rs.getString(9)));
                    } while (rs.next());
                }

                result = new GiftCertificate(id, name, description, price, duration,
                        new SimpleDateFormat(GiftCertificate.DATE_FORMAT).parse(createDate),
                        new SimpleDateFormat(GiftCertificate.DATE_FORMAT).parse(lastUpdateDate),
                        tags);
            }
        } catch (SQLException | ParseException e) {
            //todo logging exception
        }
        return result;
    }

    @Override
    public List<GiftCertificate> readByCriteria(CertCriteria criteria) {
        //todo realize
        List<GiftCertificate> result = new ArrayList<>();
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(prepareSQLFromCriteria(criteria));
            st.setString(1, criteria.getTagName());
            st.setString(2, "%" + criteria.getName() + "%");
            st.setString(3, "%" + criteria.getDescription() + "%");
            ResultSet rs = st.executeQuery();
            List<Tag> tags = new ArrayList<>();
            long prevId = 0;
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                int price = rs.getInt(4);
                int duration = rs.getInt(5);
                String createDate = rs.getString(6);
                String lastUpdateDate = rs.getString(7);
                if (id != prevId) {
                    prevId = id;
                    tags = new ArrayList<>();
                    result.add(new GiftCertificate(id, name, description, price, duration,
                            new SimpleDateFormat(GiftCertificate.DATE_FORMAT).parse(createDate),
                            new SimpleDateFormat(GiftCertificate.DATE_FORMAT).parse(lastUpdateDate),
                            tags));
                }
                tags.add(new Tag(rs.getLong(8), rs.getString(9)));

            }
        } catch (SQLException | ParseException e) {
            //todo logging exception
        }
        return result;
    }

    private String prepareSQLFromCriteria(CertCriteria cr) {
        StringBuilder result = new StringBuilder(SQL_READ_CERT_BY_CRITERIA);
        boolean sortById = true;

        if (cr.isSortByName()) {
            result.append(", ");
            result.append(SQL_NAME);
            result.append(cr.getSortNameOrder());
            sortById= false;
        }
        if (cr.isSortByCrDate()) {
            result.append(", ");
            result.append(SQL_CREATE_DATE);
            result.append(cr.getSortCrDateOrder());
            sortById= false;
        }
        if (cr.isSortByUpdDate()) {
            result.append(", ");
            result.append(SQL_UPDATE_DATE);
            result.append(cr.getSortUpdDateOrder());
            sortById= false;
        }
        if (sortById) {
            result.append(SQL_ID);
        }

        return result.toString();
    }

    @Override
    public boolean update(GiftCertificate entity) {
        //todo realize

        return true;
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_DELETE_CERT);
            st.setLong(1, id);
            result = st.executeUpdate() != 0;
        } catch (SQLException e) {
            //todo logging exception
        }
        return result;
    }
}
