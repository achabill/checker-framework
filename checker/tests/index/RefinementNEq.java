import org.checkerframework.common.value.qual.GTENegativeOne;
import org.checkerframework.common.value.qual.NonNegative;
import org.checkerframework.common.value.qual.Positive;

public class RefinementNEq {

    void test_not_equal(int a, int j, int s) {

        //:: error: (assignment.type.incompatible)
        @NonNegative int aa = a;
        if (-1 != a) {
            //:: error: (assignment.type.incompatible)
            @GTENegativeOne int b = a;
        } else {
            @GTENegativeOne int c = a;
        }

        if (0 != j) {
            //:: error: (assignment.type.incompatible)
            @NonNegative int k = j;
        } else {
            @NonNegative int l = j;
        }

        if (1 != s) {
            //:: error: (assignment.type.incompatible)
            @Positive int t = s;
        } else {
            @Positive int u = s;
        }
    }
}
//a comment
