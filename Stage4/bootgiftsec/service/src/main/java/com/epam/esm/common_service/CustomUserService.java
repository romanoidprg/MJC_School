package com.epam.esm.common_service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomUserService {

    User readByName(String name);


}
