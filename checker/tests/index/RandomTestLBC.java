import java.util.Random;
import org.checkerframework.common.value.qual.NonNegative;

public class RandomTestLBC {
    void test() {
        Random rand = new Random();
        int[] a = new int[8];
        @NonNegative double d1 = Math.random() * a.length;
        @NonNegative int deref = (int) (Math.random() * a.length);
        @NonNegative int deref2 = (int) (rand.nextDouble() * a.length);
        @NonNegative int deref3 = rand.nextInt(a.length);
    }
}
