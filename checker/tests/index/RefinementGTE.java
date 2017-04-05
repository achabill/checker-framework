import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;

public class RefinementGTE {

    void test_forward(@NonNegative int a, @NonNegative int j, @NonNegative int s) {
        /** forwards greater than or equals */
        if (a >= -1) {
            @GTENegativeOne int b = a;
        } else {
            //:: error: (assignment.type.incompatible)
            @GTENegativeOne int c = a;
        }

        if (j >= 0) {
            @NonNegative int k = j;
        } else {
            //:: error: (assignment.type.incompatible)
            @NonNegative int l = j;
        }
    }

    void test_backwards(@NonNegative int a, @NonNegative int j, @NonNegative int s) {
        /** backwards greater than or equal */
        if (-1 >= a) {
            //:: error: (assignment.type.incompatible)
            @NonNegative int b = a;
        } else {
            @NonNegative int c = a;
        }

        if (0 >= j) {
            //:: error: (assignment.type.incompatible)
            @Positive int k = j;
        } else {
            @Positive int l = j;
        }

        if (1 >= s) {
            //:: error: (assignment.type.incompatible)
            @Positive int t = s;
        } else {
            @Positive int u = s;
        }
    }
}
//a comment
