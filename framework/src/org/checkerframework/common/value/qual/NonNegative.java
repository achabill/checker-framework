package org.checkerframework.common.value.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotated expression evaluates to an integer greater than or equal to 0. An alias for
 * {@code @IntRange(from = 0)}.
 *
 * @checker_framework.manual #index-checker Index Checker
 */
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface NonNegative {}
