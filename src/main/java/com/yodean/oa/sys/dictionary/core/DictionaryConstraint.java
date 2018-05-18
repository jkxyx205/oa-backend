package com.yodean.oa.sys.dictionary.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by rick on 5/17/18.
 */
@Documented
@Constraint(validatedBy = DictionaryValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryConstraint {
    String message() default "Invalid dictionary word";
    String name();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
