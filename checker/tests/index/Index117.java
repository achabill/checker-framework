//@skip-test

public class Index117 {

    public static void foo(boolean includeIndex, String[] roots) {

        String[] result = new String[(includeIndex ? 2 : 1) * roots.length + 2];
        result[0] = "hello";

        String[] result2 = new String[2 * roots.length + 2];
        result2[0] = "hello";

        String[] result3 = new String[roots.length + 2];
        result3[0] = "hello";

        String[] result4 = new String[0 + 2];
        result4[0] = "hello";
    }
}
