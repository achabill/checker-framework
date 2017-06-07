package org.checkerframework.checker.index.upperbound;

/*>>>
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;
*/

import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.VariableTree;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import org.checkerframework.checker.index.IndexUtil;
import org.checkerframework.checker.index.qual.SameLen;
import org.checkerframework.checker.index.samelen.SameLenAnnotatedTypeFactory;
import org.checkerframework.checker.index.upperbound.UBQualifier.LessThanLengthOf;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.dataflow.util.PurityUtils;
import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedArrayType;
import org.checkerframework.framework.util.FlowExpressionParseUtil;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.TreeUtils;

/** Warns about array accesses that could be too high. */
public class UpperBoundVisitor extends BaseTypeVisitor<UpperBoundAnnotatedTypeFactory> {

    private static final String UPPER_BOUND = "array.access.unsafe.high";
    private static final String LOCAL_VAR_ANNO = "local.variable.unsafe.dependent.annotation";
    private static final String DEPENDENT_NOT_PERMITTED = "dependent.not.permitted";

    public UpperBoundVisitor(BaseTypeChecker checker) {
        super(checker);
    }

    /**
     * This visits all local variable declarations and issues a warning if a dependent annotation is
     * written on a local variable. This is necessary for the soundness of the reassignment code,
     * which must unrefine all qualifiers, but which cannot collect all in-scope local variables. So
     * that there are no local variables with qualifiers that are not in the store, this method
     * forbids programmers from writing such qualifiers. This may introduce false positives if the
     * programmer would like to write a non-primary annotation on a local variable.
     *
     * <p>Also issues warnings if the expression in a dependent annotation is not permitted.
     * Permitted expressions in dependent annotations are:
     *
     * <p>1. final or effectively final variables 2. local variables 3. private fields 4. pure
     * method calls all of whose arguments (including the receiver expression) are composed only of
     * expressions in this list 5. accesses of a public final field whose access expression
     * (sometimes called the receiver) is composed only of expressions in this list
     *
     * <p>Any other expression results in a warning.
     */
    @Override
    public Void visitVariable(VariableTree node, Void p) {
        Element elt = TreeUtils.elementFromDeclaration(node);
        AnnotatedTypeMirror atm = atypeFactory.getAnnotatedTypeLhs(node);
        AnnotationMirror anm = atm.getAnnotationInHierarchy(atypeFactory.UNKNOWN);
        if (anm != null && AnnotationUtils.hasElementValue(anm, "value")) {
            // This is a dependent annotation. If this is a local variable,
            // issue a warning; dependent annotations should not be written on local variables.
            if (elt.getKind() == ElementKind.LOCAL_VARIABLE) {
                checker.report(Result.warning(LOCAL_VAR_ANNO), node);
            }
            UBQualifier qual = UBQualifier.createUBQualifier(anm);
            if (qual.isLessThanLengthQualifier()) {
                LessThanLengthOf ltl = (LessThanLengthOf) qual;
                for (String array : ltl.getArrays()) {
                    checkIfPermittedInDependentTypeAnno(array, node);
                }
            }
        }

        return super.visitVariable(node, p);
    }

    private boolean checkIfPermittedInDependentTypeAnno(String expr, Tree tree) {
        try {
            FlowExpressions.Receiver rec =
                    atypeFactory.getReceiverFromJavaExpressionString(expr, getCurrentPath());
            if (rec.isUnmodifiableByOtherCode()) { // covers final and effectively final variables
                return true;
            }
            if (rec instanceof FlowExpressions.LocalVariable) {
                return true;
            }
            if (rec instanceof FlowExpressions.FieldAccess) {
                FlowExpressions.FieldAccess faRec = (FlowExpressions.FieldAccess) rec;
                if (faRec.getField().getModifiers().contains(Modifier.PRIVATE)
                        || (faRec.getField().getModifiers().contains(Modifier.PUBLIC)
                                && faRec.isFinal()
                                && checkIfPermittedInDependentTypeAnno(
                                        faRec.getReceiver().toString(), tree))) {
                    return true;
                } else {
                    // issue warning
                    checker.report(
                            Result.warning(
                                    DEPENDENT_NOT_PERMITTED,
                                    expr,
                                    "fields in a dependent type must either be private or public and final with a receiver that is private, public and final, or a local variable"),
                            tree);
                }
            }
            if (rec instanceof FlowExpressions.MethodCall) {
                FlowExpressions.MethodCall mcRec = (FlowExpressions.MethodCall) rec;
                boolean parametersArePermittedInDependentTypeAnno = true;
                for (FlowExpressions.Receiver r : mcRec.getParameters()) {
                    parametersArePermittedInDependentTypeAnno =
                            parametersArePermittedInDependentTypeAnno
                                    && checkIfPermittedInDependentTypeAnno(r.toString(), tree);
                }
                if (parametersArePermittedInDependentTypeAnno
                        && PurityUtils.isSideEffectFree(atypeFactory, mcRec.getElement())
                        && checkIfPermittedInDependentTypeAnno(
                                mcRec.getReceiver().toString(), tree)) {
                    return true;
                } else {
                    // issue warning
                    checker.report(
                            Result.warning(
                                    DEPENDENT_NOT_PERMITTED,
                                    expr,
                                    "all method calls in dependent types must be pure, have a receiver that is permitted in a dependent type annotation, and have parameters that are permitted in a dependent type annotation"),
                            tree);
                }
            }
            checker.report(
                    Result.warning(
                            DEPENDENT_NOT_PERMITTED,
                            expr,
                            "the expression did not fit one of the categories of permitted expression in dependent types. Those categories are: \n           1. final or effectively final variables\n"
                                    + "           2. local variables\n"
                                    + "           3. private fields\n"
                                    + "           4. pure method calls all of whose arguments (including the receiver expression)\n"
                                    + "           are composed only of expressions in this list\n"
                                    + "           5. accesses of a public final field whose access expression (sometimes called the\n"
                                    + "           receiver) is composed only of expressions in this list"),
                    tree);
            return false;
        } catch (FlowExpressionParseUtil.FlowExpressionParseException e) {
            // issue warning
            checker.report(
                    Result.warning(DEPENDENT_NOT_PERMITTED, expr, "the expression was unparseable"),
                    tree);
            return false;
        }
    }

    /**
     * When the visitor reaches an array access, it needs to check a couple of things. First, it
     * checks if the index has been assigned a reasonable UpperBound type: only an index with type
     * LTLengthOf(arr) is safe to access arr. If that fails, it checks if the access is still safe.
     * To do so, it checks if the Value Checker knows the minimum length of arr by querying the
     * Value Annotated Type Factory. If the minimum length of the array is known, the visitor can
     * check if the index is less than that minimum length. If so, then the access is still safe.
     * Otherwise, report a potential unsafe access.
     */
    @Override
    public Void visitArrayAccess(ArrayAccessTree tree, Void type) {
        ExpressionTree indexTree = tree.getIndex();
        ExpressionTree arrTree = tree.getExpression();
        visitAccess(indexTree, arrTree);
        return super.visitArrayAccess(tree, type);
    }

    /**
     * Checks if this array access is either using a variable that is less than the length of the
     * array, or using a constant less than the array's minlen. Issues an error if neither is true.
     */
    private void visitAccess(ExpressionTree indexTree, ExpressionTree arrTree) {
        AnnotatedTypeMirror indexType = atypeFactory.getAnnotatedType(indexTree);
        String arrName = FlowExpressions.internalReprOf(this.atypeFactory, arrTree).toString();

        UBQualifier qualifier = UBQualifier.createUBQualifier(indexType, atypeFactory.UNKNOWN);
        if (qualifier.isLessThanLengthOf(arrName)) {
            return;
        }

        // Find max because it's important to determine whether the index is less than the
        // minimum length of the array. If it could be any of several values, only the max is of
        // interest.
        Long valMax = IndexUtil.getMaxValue(indexTree, atypeFactory.getValueAnnotatedTypeFactory());

        AnnotationMirror sameLenAnno = atypeFactory.sameLenAnnotationFromTree(arrTree);
        // Produce the full list of relevant names by checking the SameLen type.
        if (sameLenAnno != null && AnnotationUtils.areSameByClass(sameLenAnno, SameLen.class)) {
            if (AnnotationUtils.hasElementValue(sameLenAnno, "value")) {
                List<String> slNames =
                        AnnotationUtils.getElementValueArray(
                                sameLenAnno, "value", String.class, true);
                if (qualifier.isLessThanLengthOfAny(slNames)) {
                    return;
                }
                for (String slName : slNames) {
                    // Check if any of the arrays have a minlen that is greater than the
                    // known constant value.
                    int minlenSL =
                            atypeFactory
                                    .getValueAnnotatedTypeFactory()
                                    .getMinLenFromString(slName, arrTree, getCurrentPath());
                    if (valMax != null && valMax < minlenSL) {
                        return;
                    }
                }
            }
        }

        // Check against the minlen of the array itself.
        Integer minLen = IndexUtil.getMinLen(arrTree, atypeFactory.getValueAnnotatedTypeFactory());
        if (valMax != null && minLen != null && valMax < minLen) {
            return;
        }

        checker.report(
                Result.failure(UPPER_BOUND, indexType.toString(), arrName, arrName), indexTree);
    }

    @Override
    protected void commonAssignmentCheck(
            AnnotatedTypeMirror varType,
            ExpressionTree valueExp,
            /*@CompilerMessageKey*/ String errorKey) {
        if (!relaxedCommonAssignment(varType, valueExp)) {
            super.commonAssignmentCheck(varType, valueExp, errorKey);
        }
    }

    /**
     * Returns whether the assignment is legal based on the relaxed assignment rules.
     *
     * <p>The relaxed assignment rules is the following: Assuming the varType (left-hand side) is
     * less than the length of some array given some offset
     *
     * <p>1. If both the offset and the value expression (rhs) are ints known at compile time, and
     * if the min length of the array is greater than offset + value, then the assignment is legal.
     * (This method returns true.)
     *
     * <p>2. If the value expression (rhs) is less than the length of an array that is the same
     * length as the array in the varType, and if the offsets are equal, then the assignment is
     * legal. (This method returns true.)
     *
     * <p>3. Otherwise the assignment is only legal if the usual assignment rules are true, so this
     * method returns false.
     *
     * <p>If the varType is less than the length of multiple arrays, then the this method only
     * returns true if the relaxed rules above apply for each array.
     *
     * <p>If the varType is an array type and the value express is an array initializer, then the
     * above rules are applied for expression in the initializer where the varType is the component
     * type of the array.
     */
    private boolean relaxedCommonAssignment(AnnotatedTypeMirror varType, ExpressionTree valueExp) {
        List<? extends ExpressionTree> expressions;
        if (valueExp.getKind() == Kind.NEW_ARRAY && varType.getKind() == TypeKind.ARRAY) {
            expressions = ((NewArrayTree) valueExp).getInitializers();
            if (expressions == null || expressions.isEmpty()) {
                return false;
            }
            // The qualifier we need for an array is in the component type, not varType.
            AnnotatedTypeMirror componentType = ((AnnotatedArrayType) varType).getComponentType();
            UBQualifier qualifier =
                    UBQualifier.createUBQualifier(componentType, atypeFactory.UNKNOWN);
            if (!qualifier.isLessThanLengthQualifier()) {
                return false;
            }
            for (ExpressionTree expressionTree : expressions) {
                if (!relaxedCommonAssignmentCheck((LessThanLengthOf) qualifier, expressionTree)) {
                    return false;
                }
            }
            return true;
        }

        UBQualifier qualifier = UBQualifier.createUBQualifier(varType, atypeFactory.UNKNOWN);
        return qualifier.isLessThanLengthQualifier()
                && relaxedCommonAssignmentCheck((LessThanLengthOf) qualifier, valueExp);
    }

    /**
     * Implements the actual check for the relaxed common assignment check. For what is permitted,
     * see {@link #relaxedCommonAssignment}.
     */
    private boolean relaxedCommonAssignmentCheck(
            LessThanLengthOf varLtlQual, ExpressionTree valueExp) {

        AnnotatedTypeMirror expType = atypeFactory.getAnnotatedType(valueExp);
        UBQualifier expQual = UBQualifier.createUBQualifier(expType, atypeFactory.UNKNOWN);

        Long value = IndexUtil.getMaxValue(valueExp, atypeFactory.getValueAnnotatedTypeFactory());

        if (value == null && !expQual.isLessThanLengthQualifier()) {
            return false;
        }

        SameLenAnnotatedTypeFactory sameLenFactory = atypeFactory.getSameLenAnnotatedTypeFactory();
        ValueAnnotatedTypeFactory valueAnnotatedTypeFactory =
                atypeFactory.getValueAnnotatedTypeFactory();
        checkloop:
        for (String arrayName : varLtlQual.getArrays()) {

            List<String> sameLenArrays =
                    sameLenFactory.getSameLensFromString(arrayName, valueExp, getCurrentPath());
            if (testSameLen(expQual, varLtlQual, sameLenArrays, arrayName)) {
                continue;
            }

            int minlen =
                    valueAnnotatedTypeFactory.getMinLenFromString(
                            arrayName, valueExp, getCurrentPath());
            if (testMinLen(value, minlen, arrayName, varLtlQual)) {
                continue;
            }
            for (String array : sameLenArrays) {
                int minlenSL =
                        valueAnnotatedTypeFactory.getMinLenFromString(
                                array, valueExp, getCurrentPath());
                if (testMinLen(value, minlenSL, arrayName, varLtlQual)) {
                    continue checkloop;
                }
            }

            return false;
        }

        return true;
    }

    /**
     * Tests whether replacing any of the arrays in sameLenArrays with arrayName makes expQual
     * equivalent to varQual.
     */
    private boolean testSameLen(
            UBQualifier expQual,
            LessThanLengthOf varQual,
            List<String> sameLenArrays,
            String arrayName) {

        if (!expQual.isLessThanLengthQualifier()) {
            return false;
        }

        for (String sameLenArrayName : sameLenArrays) {
            // Check whether replacing the value for any of the current type's offset results
            // in the type we're trying to match.
            if (varQual.isValidReplacement(
                    arrayName, sameLenArrayName, (LessThanLengthOf) expQual)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests a constant value (value) against the minlen (minlens) of an array (arrayName) with a
     * qualifier (varQual).
     */
    private boolean testMinLen(Long value, int minLen, String arrayName, LessThanLengthOf varQual) {
        if (value == null) {
            return false;
        }
        return varQual.isValuePlusOffsetLessThanMinLen(arrayName, value.intValue(), minLen);
    }
}
