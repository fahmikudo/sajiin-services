package id.sajiin.sajiinservices.shared.constant;

public class MessageConstant {

    public static final String SUCCESS_MESSAGE = "Success";
    public static final String SUCCESS_MESSAGE_ID = "Sukses";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    public static final String INVALID_CREDENTIALS_MESSAGE_ID = "Otorisasi tidak sesuai";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid request";
    public static final String INVALID_REQUEST_MESSAGE_ID = "Permintaan tidak valid";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE_ID = "Terjadi kesalahan pada server";
    public static final String FORBIDDEN_MESSAGE = "Forbidden access to this resource";
    public static final String FORBIDDEN_MESSAGE_ID = "Akses terlarang ke sumber daya ini";
    public static final String CREATED_MESSAGE = "Created successfully";
    public static final String CREATED_MESSAGE_ID = "Data berhasil dibuat";

    private MessageConstant() {
        throw new IllegalStateException();
    }

    public static String messageCode(int statusCode) {
        return switch (statusCode) {
            case 201 -> CREATED_MESSAGE;
            case 200 -> SUCCESS_MESSAGE;
            case 400 -> INVALID_REQUEST_MESSAGE;
            case 401 -> INVALID_CREDENTIALS_MESSAGE;
            case 403 -> FORBIDDEN_MESSAGE;
            default -> INTERNAL_SERVER_ERROR_MESSAGE;
        };
    }

}
