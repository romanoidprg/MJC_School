package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class TagDao implements CommonDao<Tag, TagCriteria> {

    private final Logger logger = LogManager.getLogger(TagDao.class);

    @Override
    public boolean create(Tag entity) {
        boolean result = false;
        return result;
    }

    @Override
    public Tag readById(long id) {
        Tag result = null;
        return result;
    }

    @Override
    public boolean update(Tag entity) {
        return false;
    }

    @Override
    public List<Tag> readByCriteria(TagCriteria criteria) {
        List<Tag> result = new ArrayList<>();
        return result;
    }

    private String prepareSQLFromCriteria(TagCriteria cr) {
        StringBuilder result = new StringBuilder("");//new StringBuilder(SQL_READ_TAG_BY_CRITERIA);
        return result.toString();
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        return result;
    }
}
