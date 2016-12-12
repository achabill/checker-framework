// Test case for issue #64: https://github.com/kelloggm/checker-framework/issues/64

// @skip-test until the issue is fixed

import org.checkerframework.common.value.qual.*;

class LengthTransfer {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Needs 2 arguments, got " + args.length);
        }
        int limit = Integer.parseInt(args[0]);
        int period = Integer.parseInt(args[1]);
    }

    void m(String @ArrayLen(2) [] args) {
        int limit = Integer.parseInt(args[0]);
        int period = Integer.parseInt(args[1]);
    }
}
