import org.checkerframework.checker.minlen.qual.*;

class equalTo {

    public static void equalToMinLen(int @MinLen(2) [] m, int @MinLen(0) [] r) {
        if (r == m) {
            int @MinLen(2) [] j = r;
        }
    }
}
