package tests;

import java.io.File;
import java.util.List;
import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.framework.test.CheckerFrameworkPerDirectoryTest;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests the constant value propagation type system without overflow.
 *
 * <p>NOTE: $CHECKERFRAMEWORK/framework/tests/value/ needs to be on the classpath. Otherwise
 * ExceptionTest will fail because it cannot find the ExceptionTest.class file for reflective method
 * resolution.
 *
 * @author plvines
 */
public class ValueNoOverflowTest extends CheckerFrameworkPerDirectoryTest {

    public ValueNoOverflowTest(List<File> testFiles) {
        super(
                testFiles,
                org.checkerframework.common.value.ValueChecker.class,
                "value",
                "-Anomsgtext",
                "-Astubs=statically-executable.astub",
                "-A" + ValueChecker.REPORT_EVAL_WARNS,
                "-A" + ValueChecker.IGNORE_OVERFLOW);
    }

    @Parameters
    public static String[] getTestDirs() {
        return new String[] {"value", "all-systems", "value-no-overflow"};
    }
}
