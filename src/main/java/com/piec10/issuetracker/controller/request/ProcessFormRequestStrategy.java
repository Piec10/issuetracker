package com.piec10.issuetracker.controller.request;

public class ProcessFormRequestStrategy implements Request {

    private ProcessFormRequest request;

    @Override
    public String processRequest() {

        if(request.formHasErrors()) return request.whenFormHasErrors();

        if(request.isGuestUser()) return request.whenIsGuestUser();

        if(request.isNew()) return request.createNew();

        return request.update();
    }
}
