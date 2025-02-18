package com.laklu.pos.exceptions;

public class NotFoundCategory extends RestHttpException {
    public NotFoundCategory() {
        super(ExceptionCode.NOT_FOUND_CATEGORY);
    }
}
