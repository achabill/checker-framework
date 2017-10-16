import org.checkerframework.checker.index.qual.LTEqLengthOf;
import org.checkerframework.checker.index.qual.LTLengthOf;

//@ SuppressWarnings("local.variable.unsafe.dependent.annotation")
class RefineLT {
    final int[] arr = {1};

    void testLTL(@LTLengthOf("arr") int test, @LTLengthOf("arr") int a, @LTLengthOf("arr") int a3) {
        int b = 2;
        if (b < test) {
            @LTLengthOf("arr") int c = b;
        }
        //:: error: (assignment.type.incompatible)
        @LTLengthOf("arr") int c1 = b;

        if (b < a3) {
            int potato = 7;
        } else {
            //:: error: (assignment.type.incompatible)
            @LTLengthOf("arr") int d = b;
        }
    }

    void testLTEL(@LTLengthOf("arr") int test, int a) {
        //:: error: (assignment.type.incompatible)
        @LTEqLengthOf("arr") int a3 = Integer.parseInt("3");

        int b = 2;
        if (b < test) {
            @LTEqLengthOf("arr") int c = b;
        }
        //:: error: (assignment.type.incompatible)
        @LTEqLengthOf("arr") int c1 = b;

        if (b < a) {
            int potato = 7;
        } else {
            //:: error: (assignment.type.incompatible)
            @LTEqLengthOf("arr") int d = b;
        }
    }
}
