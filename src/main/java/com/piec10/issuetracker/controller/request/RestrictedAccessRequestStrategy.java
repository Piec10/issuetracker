package com.piec10.issuetracker.controller.request;

public class RestrictedAccessRequestStrategy implements Request{

    private RestrictedAccessRequest request;

    public RestrictedAccessRequestStrategy(RestrictedAccessRequest request) {
        this.request = request;
    }

    @Override
    public String processRequest() {

        if(request.isNull()) return request.redirectWhenNull();

        if(request.hasNoPermission()) return request.redirectWhenNoPermission();

        request.doWork();

        return request.redirectWhenSuccess();
    }
}
