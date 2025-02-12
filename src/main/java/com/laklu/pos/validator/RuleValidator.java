package com.laklu.pos.validator;

import com.laklu.pos.exceptions.RuleNotValidException;

import java.util.HashMap;

public class RuleValidator {
    public static void validate(Rule businessRule) {
        if (businessRule.isValid()) {
            return;
        }
        HashMap<String, String> errors = new HashMap<>();
        errors.put(businessRule.field(), businessRule.message());
        throw new RuleNotValidException(errors);
    }
}
