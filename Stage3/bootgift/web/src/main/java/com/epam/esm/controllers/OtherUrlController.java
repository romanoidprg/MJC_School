package com.epam.esm.controllers;

import com.epam.esm.errors.NotExistEndPointException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = {"/*","/v1/*","/v1/certificates/*", "/v1/tags/*", "/v1/users/*/*/*/"})
public class OtherUrlController {

    private final Logger logger = LogManager.getLogger(OtherUrlController.class);

    @RequestMapping(produces = APPLICATION_JSON_VALUE)
    public void exceptionHandler() throws NotExistEndPointException {
        NotExistEndPointException e = new NotExistEndPointException();
        logger.error(e.getMessage());
            throw e;
    }

}
