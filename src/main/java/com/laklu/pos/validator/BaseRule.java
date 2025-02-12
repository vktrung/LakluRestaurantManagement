package com.laklu.pos.validator;

public abstract class BaseRule implements Rule{

    @Override
    public String field() {
        return this.getValidateField();
    }

    public abstract String getValidateField();
}
