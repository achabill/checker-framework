import org.checkerframework.common.value.qual.NonNegative;

class IntroShift {
    void test() {
        @NonNegative int a = 1 >> 1;
        //:: error: (assignment.type.incompatible)
        @NonNegative int b = -1 >> 0;
    }
}
