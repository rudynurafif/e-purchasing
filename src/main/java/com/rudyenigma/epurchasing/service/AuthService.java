package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.model.request.AuthRequest;
import com.rudyenigma.epurchasing.model.response.LoginResponse;
import com.rudyenigma.epurchasing.model.response.RegisterResponse;

public interface AuthService {

    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);

}
