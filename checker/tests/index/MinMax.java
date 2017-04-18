import org.checkerframework.common.value.qual.GTENegativeOne;
import org.checkerframework.common.value.qual.Positive;

class MinMax {
    // They call me a power gamer. I stole the test cases from issue 26.
    void mathmax() {
        @Positive int i = Math.max(-15, 2);
    }

    void mathmin() {
        @GTENegativeOne int i = Math.min(-1, 2);
    }
}
