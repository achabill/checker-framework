import divbyzero.qual.*;

class Foo {

    public static void f() {
        int one = 1;
        int zero = 0;
        //:: error: (possible divide by zero: zero)
        int x = one / zero;
        int y = zero / one;
        //:: error: (possible divide by zero: y)
        int z = x / y;
        String s = "hello";
    }

    public static void g(int y) {
        if (y == 0) {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        } else {
            int x = 1 / y;
        }

        if (y != 0) {
            int x = 1 / y;
        } else {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        }

        if (!(y == 0)) {
            int x = 1 / y;
        } else {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        }

        if (!(y != 0)) {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        } else {
            int x = 1 / y;
        }

        if (y < 0) {
            int x = 1 / y;
        }

        if (y <= 0) {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        }

        if (y > 0) {
            int x = 1 / y;
        }

        if (y >= 0) {
            //:: error: (possible divide by zero: y)
            int x = 1 / y;
        }
    }

    public static void h() {
        int zero_the_hard_way = 0 + 0 - 0 * 0;
        //:: error: (possible divide by zero: zero_the_hard_way)
        int x = 1 / zero_the_hard_way;

        int one_the_hard_way = 0 * 1 + 1;
        int y = 1 / one_the_hard_way;
    }
}
