package com.musala.gateway.utils;

import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.Set;

@Component
public class BeanValidator {

    public static <T> void validate(T bean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
