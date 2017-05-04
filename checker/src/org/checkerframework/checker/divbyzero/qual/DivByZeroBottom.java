package org.checkerframework.checker.divbyzero.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({Zero.class, NonZero.class})
@ImplicitFor(
    literals = {LiteralKind.NULL},
    typeNames = {java.lang.Void.class}
)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface DivByZeroBottom {}
