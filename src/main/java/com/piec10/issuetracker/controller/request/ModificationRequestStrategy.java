package com.piec10.issuetracker.controller.request;

public class ModificationRequestStrategy implements Request{

    private ModificationRequest request;

    public ModificationRequestStrategy(ModificationRequest request) {
        this.request = request;
    }

    @Override
    public String processRequest() {

        if(request.isNull()) return request.redirectWhenNull();

        if(request.hasNoPermission()) return request.redirectWhenNoPermission();

        request.modify();

        return request.redirectWhenSuccess();
    }
}
