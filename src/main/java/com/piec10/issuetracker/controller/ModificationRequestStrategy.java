package com.piec10.issuetracker.controller;

public class ModificationRequestStrategy implements Request{

    ModificationRequest request;

    @Override
    public String processRequest() {

        if(request.isNull()) return request.redirectWhenNull();

        if(request.hasNoPermission()) return request.redirectWhenNoPermission();

        request.modify();

        return request.redirectWhenSuccess();
    }
}
