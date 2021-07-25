package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepo extends JpaRepository<Tag,Long> {
    @Query(value = "SELECT t6.tag_id FROM " +
            "(SELECT t4.*, count(t4.tag_id) as count FROM orders as t3 " +
            "LEFT JOIN certs_tags as t4 on t3.cert_id=t4.cert_id WHERE user_id=?1 GROUP BY t4.tag_id) as t6 " +
            "WHERE t6.count=(SELECT MAX(t5.count) from " +
            "(SELECT t4.*, count(t4.tag_id) as count FROM orders as t3 " +
            "LEFT JOIN certs_tags as t4 on t3.cert_id=t4.cert_id WHERE user_id=?1 GROUP BY t4.tag_id) as t5) ", nativeQuery = true)
    List<Long> getIdOfMostUsefullTagByUserId(Long userId);
}
