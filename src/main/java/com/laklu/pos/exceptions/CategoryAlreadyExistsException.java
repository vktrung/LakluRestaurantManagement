package com.laklu.pos.exceptions;

public class CategoryAlreadyExistsException extends RestHttpException {
    public CategoryAlreadyExistsException() {
        super(ExceptionCode.CATEGORY_ALREADY_EXISTS);
    }
}
