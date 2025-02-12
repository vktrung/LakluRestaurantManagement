package com.laklu.pos.validator;

public class TableMustAvailable extends BaseRule {

    @Override
    public String getValidateField() {
        return "tables";
    }

    @Override
    public boolean isValid() {
        // implement the logic to check if the table is available
        return false;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
