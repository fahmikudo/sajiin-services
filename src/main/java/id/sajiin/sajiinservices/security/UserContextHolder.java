package id.sajiin.sajiinservices.security;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> contextHolder = new InheritableThreadLocal<>();

    private UserContextHolder() {
        throw new IllegalStateException("UserContextHolder class");
    }

    public static UserContext get() {
        return contextHolder.get();
    }

    public static void set(UserContext userContext) {
        contextHolder.set(userContext);
    }

    public static void clear() {
        contextHolder.remove();
    }

}
