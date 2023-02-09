public class ExitTool<T> {
    public static final int EXIT_CODE = -1;
    public T checkExitCode(T value) {
        try {
            if(Integer.parseInt(String.valueOf(value)) == EXIT_CODE) System.exit(0);
            return value;
        } catch (Exception e) {
            return value;
        }
    }
}
