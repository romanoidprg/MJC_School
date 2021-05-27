package com.epam.mjc;

import com.epam.mjc.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumber() {
        assertTrue(StringUtils.isPositiveNumber("87.23"));
        assertTrue(StringUtils.isPositiveNumber("0xFA"));
        assertTrue(StringUtils.isPositiveNumber("067"));
        assertFalse(StringUtils.isPositiveNumber("09"));
        assertFalse(StringUtils.isPositiveNumber("25j3"));
        assertFalse(StringUtils.isPositiveNumber("-253"));
        assertFalse(StringUtils.isPositiveNumber(""));
    }
}