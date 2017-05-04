package tests;

import java.io.File;
import java.util.List;
import org.checkerframework.framework.test.CheckerFrameworkPerDirectoryTest;
import org.junit.runners.Parameterized.Parameters;

/** JUnit tests for the Index Checker. */
public class DivByZeroTest extends CheckerFrameworkPerDirectoryTest {

    public DivByZeroTest(List<File> testFiles) {
        super(
                testFiles,
                org.checkerframework.checker.divbyzero.DivByZeroChecker.class,
                "divbyzero",
                "-Anomsgtext");
    }

    @Parameters
    public static String[] getTestDirs() {
        return new String[] {"divbyzero"};
    }
}
