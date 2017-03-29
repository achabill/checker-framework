// Test case for issue #66:
// https://github.com/kelloggm/checker-framework/issues/66

import org.checkerframework.checker.index.qual.*;
import org.checkerframework.common.value.qual.*;

public class ArrayConstructionPositiveLength {

    public void makeArray(@Positive int max_values) {
        // It would be nice if this passed, but right now it does not. The equivalent
        // test below does pass, however.
        //:: error: (assignment.type.incompatible)
        String @MinLen(1) [] a = new String[max_values];
    }

    public void makeArray2(@IntRange(from = 1, to = Integer.MAX_VALUE) int max_values) {
        String @MinLen(1) [] a = new String[max_values];
    }
}
