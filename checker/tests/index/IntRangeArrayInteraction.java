import org.checkerframework.checker.index.qual.*;
import org.checkerframework.common.value.qual.*;

class IntRangeArrayInteraction {
    void test(@IntRange(from = 1, to = Integer.MAX_VALUE) int length) {
        int[] a = new int[length];
        int @MinLen(1) [] x = a;
    }
}
