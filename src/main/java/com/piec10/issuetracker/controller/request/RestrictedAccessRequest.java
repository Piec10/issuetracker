package com.piec10.issuetracker.controller.request;

public interface RestrictedAccessRequest {

    boolean isNull();

    String redirectWhenNull();

    boolean hasNoPermission();

    String redirectWhenNoPermission();

    void doWork();

    String redirectWhenSuccess();
}
