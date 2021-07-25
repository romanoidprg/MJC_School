package com.epam.esm.dao;

import com.epam.esm.model.User;

public interface CustomUserDao {

    User readByName(String userName);
}
