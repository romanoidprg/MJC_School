package com.epam.esm.common_service;

public interface CustomOrderService {

    Long createFromUserIdAndCertId(Long userId, Long certId) throws Exception;
}
