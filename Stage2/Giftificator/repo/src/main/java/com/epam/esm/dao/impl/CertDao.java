package com.epam.esm.dao.impl;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String SQL_READ_CERT_BY_CRITERIA = "SELECT c1.*, t1.* FROM " +
            "((SELECT c.* FROM (gift_certificates AS c LEFT JOIN certs_tags AS ct ON c.id=ct.cert_id) " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id WHERE c.name LIKE ? AND c.description LIKE ? AND t.name LIKE ?) " +
            "AS c1 LEFT JOIN certs_tags AS ct1 ON c1.id=ct1.cert_id) LEFT JOIN tags AS t1 ON ct1.tag_id=t1.id ORDER BY ";
    private static final String SQL_NAME = "c1.name ";
    private static final String SQL_CREATE_DATE = "c1.create_date ";
    private static final String SQL_UPDATE_DATE = "c1.last_update_date ";
    private static final String SQL_ID = "c1.id";
    private static final String SQL_READ_CERT_BY_CRITERIA_ANY_TAG = "SELECT c1.*, t1.*  FROM " +
            "(gift_certificates AS c1 LEFT JOIN certs_tags AS ct1 ON c1.id=ct1.cert_id) " +
            "LEFT JOIN tags AS t1 ON ct1.tag_id=t1.id " +
            "WHERE c1.name LIKE ? AND c1.description LIKE ? ORDER BY ";
    private static final String SQL_UPDATE_CERT = "UPDATE gift_certificates SET name=?, description=?, " +
            "price=?, duration=?, create_date=?, last_update_date=? WHERE id=?";

    private final Logger logger = LogManager.getLogger(CertDao.class);

    @Autowired
    private ConnectionPool connectionPool;

    @Override
    public boolean create(GiftCertificate entity) {
        entity.resetNullFieldsToDefaults();
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
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
            logger.error(e.getMessage());
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
        rs.close();
        st.close();
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
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
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public List<GiftCertificate> readByCriteria(CertCriteria criteria) {
        List<GiftCertificate> result = new ArrayList<>();
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(prepareSQLFromCriteria(criteria));
            st.setString(1, "%" + criteria.getName() + "%");
            st.setString(2, "%" + criteria.getDescription() + "%");
            if (!criteria.getTagName().equals("")) {
                st.setString(3, criteria.getTagName());
            }
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
                long tagId = rs.getLong(8);
                String tagName = rs.getString(9);
                if (tagId != 0) {
                    tags.add(new Tag(tagId, tagName));
                }

            }
        } catch (SQLException | ParseException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    private String prepareSQLFromCriteria(CertCriteria cr) {
        StringBuilder result = new StringBuilder();
        if (cr.getTagName().equals("")) {
            result.append(SQL_READ_CERT_BY_CRITERIA_ANY_TAG);
        } else {
            result.append(SQL_READ_CERT_BY_CRITERIA);
        }
        boolean sortById = true;

        if (cr.isSortByName()) {
            sortById = false;
            result.append(SQL_NAME);
            result.append(cr.getSortNameOrder());
        }
        if (cr.isSortByCrDate()) {
            if (!sortById) {
                result.append(", ");
            } else {
                sortById = false;
            }
            result.append(SQL_CREATE_DATE);
            result.append(cr.getSortCrDateOrder());
        }

        if (cr.isSortByUpdDate()) {
            if (!sortById) {
                result.append(", ");
            } else {
                sortById = false;
            }
            result.append(SQL_UPDATE_DATE);
            result.append(cr.getSortUpdDateOrder());
        }
        if (sortById) {
            result.append(SQL_ID);
        }

        return result.toString();
    }

    @Override
    public boolean update(GiftCertificate updCert) {
        boolean result = false;
        GiftCertificate currCert = null;
        if (updCert != null) {
            currCert = readById(updCert.getId());
        }
        if (currCert != null) {
            try (Connection c = connectionPool.getConnection()) {
                currCert.update(updCert);
                PreparedStatement st = c.prepareStatement(SQL_UPDATE_CERT);
                st.setString(1, currCert.getName());
                st.setString(2, currCert.getDescription());
                st.setInt(3, currCert.getPrice());
                st.setInt(4, currCert.getDuration());
                st.setString(5, currCert.getCreateDate());
                st.setString(6, currCert.getLastUpdateDate());
                st.setLong(7, currCert.getId());
                st.executeUpdate();
                result = true;
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_DELETE_CERT);
            st.setLong(1, id);
            result = st.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
