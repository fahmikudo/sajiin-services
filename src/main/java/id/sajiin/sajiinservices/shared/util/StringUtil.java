package id.sajiin.sajiinservices.shared.util;

public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("String Util Class");
    }

    public static boolean isNotNullAndNotEmpty(String token) {
        boolean isNOE;

        isNOE = token != null && !token.isEmpty() && !token.equals("null") && !token.equals("[null]");

        return isNOE;
    }

    public static boolean isNullOrEmpty(String token) {
        return token == null || token.isEmpty() || token.equals("null");
    }

}
