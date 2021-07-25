package com.epam.esm.dao;

import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByNameIgnoreCase(String name);

    @Query(value = "SELECT user_id FROM " +
            "(SELECT user_id, SUM(cost) as summa FROM orders GROUP BY user_id) as t1 " +
            "WHERE t1.summa = " +
            "(SELECT max(t2.summa) FROM " +
            "(SELECT user_id, SUM(cost) as summa FROM orders GROUP BY user_id) as t2) ", nativeQuery = true)
    Long getIdOfUserWhichHaveHigestCostOfOrders();

}
