package com.piec10.issuetracker.controller.request;

public interface ProcessFormRequest {

    boolean formHasErrors();

    String whenFormHasErrors();

    boolean isGuestUser();

    String whenIsGuestUser();

    boolean isNew();

    String createNew();

    String update();
}
