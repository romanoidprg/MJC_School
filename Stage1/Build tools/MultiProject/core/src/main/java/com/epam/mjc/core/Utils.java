package com.epam.mjc.core;

import com.epam.mjc.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str) {
        for (String s : str) {
            if (!StringUtils.isPositiveNumber(s)) {
                return false;
            }
        }
        return true;
    }

}
