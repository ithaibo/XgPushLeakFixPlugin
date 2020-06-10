public class PrintException {

    public static void printStackTrace(Exception e) {
        if (null == e) return;
        e.printStackTrace();
    }
}
