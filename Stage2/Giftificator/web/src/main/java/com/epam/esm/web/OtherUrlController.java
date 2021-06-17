package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.NotExistEndPointException;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = {"/*","/v1/*","/v1/certificates/*", "/v1/tags/*"})
public class OtherUrlController {

    private final Logger logger = LogManager.getLogger(OtherUrlController.class);

    @RequestMapping(produces = APPLICATION_JSON_VALUE)
    public void exceptionHandler() throws NotExistEndPointException {
        NotExistEndPointException e = new NotExistEndPointException();
        logger.error(e.getMessage());
            throw e;
    }

}
