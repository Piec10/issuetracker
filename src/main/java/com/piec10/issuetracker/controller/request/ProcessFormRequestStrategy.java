package com.piec10.issuetracker.controller.request;

public class ProcessFormRequestStrategy implements Request {

    private ProcessFormRequest request;

    public ProcessFormRequestStrategy(ProcessFormRequest request) {
        this.request = request;
    }

    @Override
    public String processRequest() {

        if(request.formHasErrors()) return request.whenFormHasErrors();

        if(request.isNotNew()) return request.update();

        if(request.isGuestUser()) return request.whenIsGuestUser();

        else return request.createNew();
    }
}
