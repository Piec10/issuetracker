package com.piec10.issuetracker.controller;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IssueRequestFactoryImpl implements IssueRequestFactory{

    

    @Override
    public Request createDeleteIssueRequest(int issueId, HttpServletRequest request) {
        return null;
    }
}
