package com.epam.esm.dao.impl;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDao implements CommonDao<Tag, TagCriteria> {

    private static final String SQL_CREATE_TAG = "INSERT INTO tags (name) VALUES (?)";
    private static final String SQL_READ_TAG_BY_ID = "SELECT * FROM tags WHERE id=?";
    private static final String SQL_READ_TAG_BY_CRITERIA = "SELECT * FROM tags WHERE name LIKE ? ORDER BY ";
    private static final String SQL_ID = "id";
    private static final String SQL_NAME = "name ";
    private static final String SQL_DELETE_TAG = "DELETE FROM tags WHERE id=?";

    private final Logger logger = LogManager.getLogger(TagDao.class);


    @Autowired
    private ConnectionPool connectionPool;

    @Override
    public boolean create(Tag entity) {
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_CREATE_TAG);
            st.setString(1, entity.getName());
            st.executeUpdate();
            result = true;
        } catch (
                SQLException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public Tag readById(long id) {
        Tag result = null;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_READ_TAG_BY_ID);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = new Tag(id, rs.getString(2));
            }
        } catch (
                SQLException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Tag entity) {
        return false;
    }

    @Override
    public List<Tag> readByCriteria(TagCriteria criteria) {
        List<Tag> result = new ArrayList<>();
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(prepareSQLFromCriteria(criteria));
            st.setString(1, "%" + criteria.getName() + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(new Tag(rs.getLong(1), rs.getString(2)));
            }
        } catch (
                SQLException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    private String prepareSQLFromCriteria(TagCriteria cr) {
        StringBuilder result = new StringBuilder(SQL_READ_TAG_BY_CRITERIA);
        if (cr.isSortByName()) {
            result.append(SQL_NAME);
            result.append(cr.getSortOrder());
        } else {
            result.append(SQL_ID);
        }
        return result.toString();
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        try (Connection c = connectionPool.getConnection()) {
            PreparedStatement st = c.prepareStatement(SQL_DELETE_TAG);
            st.setLong(1, id);
            result = st.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
