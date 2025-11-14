package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.modal.VerificationCode;

public interface VerificationService {
    VerificationCode createVerificationCode(String otp, String email);
}
