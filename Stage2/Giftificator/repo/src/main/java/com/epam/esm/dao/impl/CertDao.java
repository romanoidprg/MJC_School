package com.epam.esm.dao.impl;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import jdk.nashorn.internal.parser.DateParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {
    public static final String SQL_CREATE_CERT = "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";
    private static final String SQL_CREATE_CERT_TAG = "INSERT INTO certs_tags VALUES (?, ?)";
    private static final String SQL_READ_CERT_BY_ID = "SELECT c.*, t.* " +
            "FROM (gift_certificates AS c LEFT JOIN certs_tags AS ct ON c.id=ct.cert_id) " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id " +
            "WHERE c.id=?";
    private final ConnectionPool connectionPool;

    public CertDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

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
                //todo control tag existing
                st = c.prepareStatement(SQL_CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, tag.getName());
                st.executeUpdate();
                rs = st.getGeneratedKeys();
                rs.next();
                long tagId = rs.getLong(1);
                st = c.prepareStatement(SQL_CREATE_CERT_TAG);
                st.setLong(1, certId);
                st.setLong(2, tagId);
                st.execute();
            }
            result = true;
        } catch (SQLException e) {
            //todo logging exception
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
        return new ArrayList<>();
    }

    @Override
    public boolean update(GiftCertificate entity) {
        //todo realize
        return true;
    }

    @Override
    public boolean delete(long id) {
        //todo realize
        return true;
    }
}
