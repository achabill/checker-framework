package org.checkerframework.checker.divbyzero;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;
import java.lang.annotation.Annotation;
import javax.lang.model.element.AnnotationMirror;
import org.checkerframework.checker.divbyzero.qual.*;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;
import org.checkerframework.javacutil.AnnotationUtils;

public class DivByZeroAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    /**
     * Compute the default annotation for the given literal.
     *
     * @param literal the literal in the syntax tree to examine
     * @return the most specific possible point in the lattice for the given literal
     */
    private Class<? extends Annotation> defaultAnnotation(LiteralTree literal) {
        switch (literal.getKind()) {
            case INT_LITERAL:
                int intValue = (Integer) literal.getValue();
                if (intValue != 0) {
                    return NonZero.class;
                } else {
                    return Zero.class;
                }
            case LONG_LITERAL:
                long longValue = (Long) literal.getValue();
                if (longValue != 0) {
                    return NonZero.class;
                } else {
                    return Zero.class;
                }
        }
        return MaybeZero.class;
    }

    // ========================================================================
    // Checker Framework plumbing

    public DivByZeroAnnotatedTypeFactory(BaseTypeChecker c) {
        super(c);
        postInit();
    }

    @Override
    protected TreeAnnotator createTreeAnnotator() {
        return new ListTreeAnnotator(new DivByZeroTreeAnnotator(this), super.createTreeAnnotator());
    }

    private class DivByZeroTreeAnnotator extends TreeAnnotator {

        public DivByZeroTreeAnnotator(AnnotatedTypeFactory atypeFactory) {
            super(atypeFactory);
        }

        @Override
        public Void visitLiteral(LiteralTree tree, AnnotatedTypeMirror type) {
            if (tree.getKind() == Tree.Kind.NULL_LITERAL) {
                return super.visitLiteral(tree, type);
            }
            Class<? extends Annotation> c = defaultAnnotation(tree);
            AnnotationMirror m = AnnotationUtils.fromClass(getProcessingEnv().getElementUtils(), c);
            type.replaceAnnotation(m);
            return null;
        }
    }
}
