import org.checkerframework.framework.qual.PolyAll;

public final class PolyAllTypeVar<T extends Comparable<T>> {
    public void method(@PolyAll T[] a1, @PolyAll T[] a2) {
        // Type systems that don't support dataflow will issue a warning here
        @SuppressWarnings({"guieffect", "units"})
        T elt1 = a1[0];
    }
}
