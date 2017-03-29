//@skip-test

import org.checkerframework.common.value.qual.*;

public class Index117 {

    public static void foo(boolean includeIndex, String[] roots) {

        String[] result = new String[(includeIndex ? 2 : 1) * roots.length + 2];
        result[0] = "hello";

        String[] result2 = new String[2 * roots.length + 2];
        result2[0] = "hello";

        String[] result3 = new String[roots.length + 2];

        @IntRange(from = 2, to = 6)
        int x = roots.length + 2;
        @IntRange(from = 0, to = Integer.MAX_VALUE)
        int y = roots.length;

        result3[0] = "hello";

        String[] result4 = new String[0 + 2];
        result4[0] = "hello";
    }

    void intRangeTest(@IntRange(from = 1, to = 3) int k) {
        @IntRange(from = 3, to = 5)
        int j = k + 2;
    }
}
