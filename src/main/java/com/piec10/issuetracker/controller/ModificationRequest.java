package com.piec10.issuetracker.controller;

public interface ModificationRequest {

    boolean isNull();

    String redirectWhenNull();

    boolean hasNoPermission();

    String redirectWhenNoPermission();

    void modify();

    String redirectWhenSuccess();
}
