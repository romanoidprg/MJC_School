import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

public class StringUtils {
    boolean isPositiveNumber(String str) {
        return isCreatable(str) && str.charAt(0) != '-';
    }
}
