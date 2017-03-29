package org.checkerframework.checker.index;

import com.sun.source.tree.Tree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import org.checkerframework.checker.index.minlen.MinLenAnnotatedTypeFactory;
import org.checkerframework.checker.index.qual.MinLen;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.common.value.qual.IntVal;
import org.checkerframework.common.value.util.Range;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;

/** A collection of utility functions used by several Index Checker subcheckers. */
public class IndexUtil {

    /**
     * Gets the value field of an annotation with a list of strings in its value field. Null is
     * returned if the annotation has no value field.
     *
     * <p>For the Index Checker, this will get a list of array names from an Upper Bound or SameLen
     * annotation. making this safe to call on any Upper Bound or SameLen annotation.
     */
    public static List<String> getValueOfAnnotationWithStringArgument(AnnotationMirror anno) {
        if (!AnnotationUtils.hasElementValue(anno, "value")) {
            return null;
        }
        return AnnotationUtils.getElementValueArray(anno, "value", String.class, true);
    }

    /**
     * Finds the min and max values of an integer given its type in the Constant Value Checker's
     * type system. If there is no IntVal or IntRange annotation, returns null. Otherwise, the first
     * element in the resulting list is the min, and the last is the max.
     */
    public static List<Long> getMinAndMaxValues(AnnotatedTypeMirror valueType) {
        AnnotationMirror intVal = valueType.getAnnotation(IntVal.class);
        List<Long> result = new ArrayList<>(2);
        if (intVal != null) {
            List<Long> intVals = ValueAnnotatedTypeFactory.getIntValues(intVal);
            result.add(Collections.min(intVals));
            result.add(Collections.max(intVals));
        } else {
            // Look for an IntRange annotation
            AnnotationMirror intRange = valueType.getAnnotation(IntRange.class);
            if (intRange == null) {
                return null;
            }
            Range range = ValueAnnotatedTypeFactory.getIntRange(intRange);
            result.add(range.from);
            result.add(range.to);
        }
        return result;
    }

    /**
     * Either returns the exact value of the given tree according to the Constant Value Checker, or
     * null if the exact value is not known. This method should only be used by clients who need
     * exactly one value -- such as the LBC's binary operator rules -- and not by those that need to
     * know whether a valueType belongs to a particular qualifier.
     */
    public static Long getExactValue(Tree tree, ValueAnnotatedTypeFactory factory) {
        AnnotatedTypeMirror valueType = factory.getAnnotatedType(tree);
        List<Long> possibleValues = getMinAndMaxValues(valueType);
        if (possibleValues != null && possibleValues.get(0) == possibleValues.get(1)) {
            return possibleValues.get(0);
        } else {
            return null;
        }
    }

    /**
     * Finds the minimum value in a Value Checker type. If there is no information (such as when the
     * list of possible values is empty or null), returns null. Otherwise, returns the smallest
     * value in the list of possible values.
     */
    public static Long getMinValue(Tree tree, ValueAnnotatedTypeFactory factory) {
        AnnotatedTypeMirror valueType = factory.getAnnotatedType(tree);
        List<Long> possibleValues = getMinAndMaxValues(valueType);
        if (possibleValues != null) {
            return possibleValues.get(0);
        } else {
            return null;
        }
    }

    /**
     * Finds the maximum value in a Value Checker type. If there is no information (such as when the
     * list of possible values is empty or null), returns null. Otherwise, returns the smallest
     * value in the list of possible values.
     */
    public static Long getMaxValue(Tree tree, ValueAnnotatedTypeFactory factory) {
        AnnotatedTypeMirror valueType = factory.getAnnotatedType(tree);
        List<Long> possibleValues = getMinAndMaxValues(valueType);
        if (possibleValues != null) {
            return possibleValues.get(1);
        } else {
            return null;
        }
    }

    /**
     * Queries the MinLen Checker to determine if there is a known minimum length for the array
     * represented by {@code tree}. If not, returns -1.
     */
    public static int getMinLen(Tree tree, MinLenAnnotatedTypeFactory minLenAnnotatedTypeFactory) {
        AnnotatedTypeMirror minLenType = minLenAnnotatedTypeFactory.getAnnotatedType(tree);
        AnnotationMirror anm = minLenType.getAnnotation(MinLen.class);
        return getMinLen(anm);
    }

    /**
     * Returns the MinLen value of the given annotation mirror, or -1 if the annotation mirror is
     * null.
     */
    public static int getMinLen(AnnotationMirror anm) {
        if (anm == null) {
            return -1;
        }
        return AnnotationUtils.getElementValue(anm, "value", Integer.class, true);
    }
}
