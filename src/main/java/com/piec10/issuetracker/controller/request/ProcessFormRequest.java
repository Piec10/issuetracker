package com.piec10.issuetracker.controller.request;

public interface ProcessFormRequest {

    boolean formHasErrors();

    String redirectWhenFormHasErrors();

    boolean isGuest();

    String redirectWhenGuest();

    boolean isNew();

    String createNew();

    String update();
}
