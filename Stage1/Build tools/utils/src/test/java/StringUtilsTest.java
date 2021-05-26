import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumber() {
        StringUtils stringUtils = new StringUtils();
        assertTrue(stringUtils.isPositiveNumber("87.23"));
        assertTrue(stringUtils.isPositiveNumber("0xFA"));
        assertTrue(stringUtils.isPositiveNumber("067"));
        assertFalse(stringUtils.isPositiveNumber("09"));
        assertFalse(stringUtils.isPositiveNumber("25j3"));
        assertFalse(stringUtils.isPositiveNumber("-253"));
        assertFalse(stringUtils.isPositiveNumber(""));
    }
}