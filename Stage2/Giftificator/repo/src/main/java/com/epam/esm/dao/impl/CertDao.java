package com.epam.esm.dao.impl;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {
    public static final String SQL_CREATE_CERT = "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";
    private static final String SQL_CREATE_CERT_TAG = "INSERT INTO certs_tags VALUES (?, ?)";

    @Override
    public boolean create(GiftCertificate entity) {
        boolean result = false;
        try (Connection c = ConnectionPool.POOL.getConnection()) {
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
            String a = e.getMessage();
            //todo logging exception
        }
        //todo realize
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
        //todo realize

        //todo: must be deleted -->
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "11111"));
        tags.add(new Tag(2, "22222"));
        tags.add(new Tag(3, "33333"));

        GiftCertificate cert = new GiftCertificate(id, id + " Cert", "This is " + id + " Cert", 30, 40,
                Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)),
                Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), tags);
        //todo: <-- must be deleted

        return cert;
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
