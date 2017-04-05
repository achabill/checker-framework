import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.common.value.qual.IntVal;

public class RefinementLT {

    void test_backwards(@NonNegative int a, @NonNegative int j, @NonNegative int s) {
        /** backwards less than */
        if (-1 < a) {
            @NonNegative int b = a;
            //:: error: (assignment.type.incompatible)
            @Positive int d = a;
        } else {
            @Positive int c = a; // dead code - the above check is always true.
        }

        if (0 < j) {
            @Positive int k = j;
        } else {
            //:: error: (assignment.type.incompatible)
            @Positive int l = j;
            @IntVal(0) int m = j;
        }

        if (1 < s) {
            @Positive int t = s;
        } else {
            //:: error: (assignment.type.incompatible)
            @Positive int u = s;
            @IntVal({0, 1}) int v = s;
        }
    }

    void test_forwards(@NonNegative int a, @NonNegative int j, @NonNegative int s) {
        /** forwards less than */
        if (a < -1) {
            @Positive int b = a; // dead code
        } else {
            @GTENegativeOne int c = a;
            //:: error: (assignment.type.incompatible)
            @Positive int d = a;
        }

        if (j < 0) {
            @Positive int k = j; // dead code
        } else {
            @NonNegative int l = j;
            //:: error: (assignment.type.incompatible)
            @Positive int m = j;
        }

        if (s < 1) {
            //:: error: (assignment.type.incompatible)
            @Positive int t = s;
        } else {
            @Positive int u = s;
        }
    }
}
//a comment
