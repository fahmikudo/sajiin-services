package id.sajiin.sajiinservices.shared.util;

public class NumberUtil {

    public static boolean isNotNullAndNotZero(Integer input) {
        return input != null && input != 0;
    }

    public static boolean isNotNullAndNotZero(Long input) {
        return input != null && input != 0L;
    }

    public static boolean isNotNullAndPositive(Integer input) {
        return input != null && input > 0;
    }

    public static boolean isNullOrZero(Integer input) {
        return input == null || input == 0;
    }

    public static boolean isNullOrZero(Long input) {
        return input == null || input == 0L;
    }

    public static boolean isNullOrNegative(Integer input) {
        return input == null || input < 0;
    }

    public static boolean isNullOrZeroOrNegative(Integer input) {
        return input == null || input <= 0;
    }

    public static boolean isNotNullAndNotNegative(Integer input) {
        return input != null && input >= 0;
    }

    public static boolean isNotNullAndLowerThanOne(Integer input) {
        return input != null && input < 1;
    }

}
