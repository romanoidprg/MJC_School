package com.epam.esm.dao;

import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.TagDao;

public class DaoFactory {
    public static CertDao getCertDao(ConnectionPool connectionPool) {
        return new CertDao(connectionPool);
    }

    public static TagDao getTagDao(ConnectionPool connectionPool) {
        return new TagDao(connectionPool);
    }
}
