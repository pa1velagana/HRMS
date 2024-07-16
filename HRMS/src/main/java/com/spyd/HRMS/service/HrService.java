package com.spyd.HRMS.service;

import com.spyd.HRMS.request.LoginRequest;
import com.spyd.HRMS.modal.Hr;

public interface HrService {
    String savingUserCredentials(Hr user);

    String loginCredentials(LoginRequest loginRequest);
}
