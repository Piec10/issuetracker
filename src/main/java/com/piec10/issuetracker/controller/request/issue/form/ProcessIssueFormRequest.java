package com.piec10.issuetracker.controller.request.issue.form;

import com.piec10.issuetracker.controller.request.ProcessFormRequest;
import com.piec10.issuetracker.controller.request.Request;
import com.piec10.issuetracker.controller.request.issue.IssueRequestFactory;
import com.piec10.issuetracker.controller.request.issue.form.IssueFormRequest;
import com.piec10.issuetracker.form.FormIssue;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.controller.request.RequestRedirections.toIssueForm;
import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isGuest;

public class ProcessIssueFormRequest extends IssueFormRequest implements ProcessFormRequest {

    private IssueRequestFactory factory;
    private BindingResult bindingResult;
    private HttpServletRequest request;

    public ProcessIssueFormRequest(IssueRequestFactory factory,
                                   IssueService issueService,
                                   FormIssue formIssue,
                                   BindingResult bindingResult,
                                   HttpServletRequest request, Model model) {

        super(issueService, model);
        this.formIssue = formIssue;
        this.factory = factory;
        this.bindingResult = bindingResult;
        this.request = request;
    }


    @Override
    public boolean formHasErrors() {
        return bindingResult.hasErrors();
    }

    @Override
    public String whenFormHasErrors() {
        if(isNew()) removeDoneStatusFromList();
        addCommonModelAttributes();
        return toIssueForm();
    }

    @Override
    public boolean isNotNew() {
        return !isNew();
    }

    private boolean isNew() {
        return formIssue.getId() == 0;
    }

    @Override
    public String update() {
        Request updateIssueRequest = factory.createUpdateIssueRequest(request, formIssue);
        return updateIssueRequest.processRequest();
    }

    @Override
    public boolean isGuestUser() {
        return isGuest(request);
    }

    @Override
    public String whenIsGuestUser() {
        removeDoneStatusFromList();
        addCommonModelAttributes();
        model.addAttribute("guestUserError", "Sorry, Guest user cannot create new issues.");
        return toIssueForm();
    }

    @Override
    public String createNew() {
        Request createIssueRequest = factory.getCreateIssueRequest(request, formIssue);
        return createIssueRequest.processRequest();
    }
}
