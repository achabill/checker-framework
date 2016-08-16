package org.checkerframework.checker.lowerbound.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;

/**
 * In the Lower Bound Checker's type system, this type
 * represents any variable not known to be an integer greater than or equal to -1
 *
 * @checker_framework.manual #lowerbound-checker Lower Bound Checker
 */
@DefaultQualifierInHierarchy
@SubtypeOf({})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface LowerBoundUnknown {}