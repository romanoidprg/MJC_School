package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {
    @Override
    public boolean create(GiftCertificate entity) {
        //todo realize
        return true;
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
