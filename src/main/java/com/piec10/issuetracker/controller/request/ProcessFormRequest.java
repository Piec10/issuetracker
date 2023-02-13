package com.piec10.issuetracker.controller.request;

public interface ProcessFormRequest {

    boolean formHasErrors();

    String whenFormHasErrors();

    boolean isGuestUser();

    String whenIsGuestUser();

    boolean isNotNew();

    String createNew();

    String update();
}
