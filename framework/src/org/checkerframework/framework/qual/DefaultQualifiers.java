package org.checkerframework.framework.qual;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the annotations to be included in a type without having to provide them explicitly.
 *
 * <p>This annotation permits specifying multiple default qualifiers for more than one type system.
 * It is necessary because Java forbids multiple annotations of the same name at a single location.
 *
 * <p>Example:
 * <!-- &nbsp; is a hack that prevents @ from being the first character on the line, which confuses Javadoc -->
 *
 * <pre>
 * &nbsp; @DefaultQualifiers({
 * &nbsp;     @DefaultQualifier(NonNull.class),
 * &nbsp;     @DefaultQualifier(value = Interned.class, locations = ALL_EXCEPT_LOCALS),
 * &nbsp;     @DefaultQualifier(Tainted.class)
 * &nbsp; })
 * </pre>
 *
 * @see DefaultQualifier
 */
// TODO: use repeating annotations (will make source depend on Java 8).
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({PACKAGE, TYPE, CONSTRUCTOR, METHOD, FIELD, LOCAL_VARIABLE, PARAMETER})
public @interface DefaultQualifiers {
    /** The default qualifier settings */
    DefaultQualifier[] value() default {};
}
