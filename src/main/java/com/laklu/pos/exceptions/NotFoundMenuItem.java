package com.laklu.pos.exceptions;

public class NotFoundMenuItem extends RestHttpException {
    public NotFoundMenuItem() {
        super(ExceptionCode.NOT_FOUND_MENU_ITEM);
    }
}
