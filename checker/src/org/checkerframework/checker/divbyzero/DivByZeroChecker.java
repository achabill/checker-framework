package org.checkerframework.checker.divbyzero;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.qual.RelevantJavaTypes;

@RelevantJavaTypes({Integer.class, Long.class})
public class DivByZeroChecker extends BaseTypeChecker {}
