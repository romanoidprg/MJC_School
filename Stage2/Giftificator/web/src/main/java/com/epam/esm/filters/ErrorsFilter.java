package com.epam.esm.filters;

import com.epam.esm.errors.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ErrorsFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {

            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),"12345");

//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        }
//
//        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        if (exception != null) {
//            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), "httpServletResponse.getHeader(Header)");
//            PrintWriter writer = httpServletResponse.getWriter();
//            writer.write(convertObjectToJson(errorResponse));
//            writer.flush();
//        } else {
//            chain.doFilter(httpServletRequest, httpServletResponse);
//        }
    }

    public String convertObjectToJson(Object object) {
        String result = null;
        if (object != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
