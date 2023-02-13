package com.piec10.issuetracker.controller.request;

public class ProcessFormRequestStrategy implements Request {

    private ProcessFormRequest request;

    @Override
    public String processRequest() {

        if(request.formHasErrors()) return request.redirectWhenFormHasErrors();

        if(request.isGuest()) return request.redirectWhenGuest();

        if(request.isNew()) return request.createNew();

        return request.update();
    }
}
