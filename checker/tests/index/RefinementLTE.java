import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.common.value.qual.*;

public class RefinementLTE {

    void test_backwards(
            @IntRange(from = Integer.MIN_VALUE, to = Integer.MAX_VALUE) int a,
            @NonNegative int j,
            @NonNegative int s) {
        /** backwards less than or equals */
        if (-1 <= a) {
            @GTENegativeOne int b = a;
            //:: error: (assignment.type.incompatible)
            @NonNegative int d = a;
        } else {
            //:: error: (assignment.type.incompatible)
            @Positive int c = a;
        }

        if (0 <= j) {
            @NonNegative int k = j;
            //:: error: (assignment.type.incompatible)
            @Positive int l = j;
        }

        if (1 <= s) {
            @Positive int t = s;
        } else {
            //:: error: (assignment.type.incompatible)
            @Positive int u = s;
        }
    }

    void test_forwards(
            @IntRange(from = Integer.MIN_VALUE, to = Integer.MAX_VALUE) int a,
            @NonNegative int j,
            @NonNegative int s) {
        /** forwards less than or equal */
        if (a <= -1) {
            //:: error: (assignment.type.incompatible)
            @NonNegative int b = a;
        } else {
            @NonNegative int c = a;
        }

        if (j <= 0) {
            //:: error: (assignment.type.incompatible)
            @Positive int k = j;
        } else {
            @Positive int l = j;
        }

        if (s <= 1) {
            //:: error: (assignment.type.incompatible)
            @Positive int t = s;
        } else {
            @Positive int u = s;
        }
    }
}
//a comment
