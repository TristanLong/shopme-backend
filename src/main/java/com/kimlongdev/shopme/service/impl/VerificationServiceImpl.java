package com.kimlongdev.shopme.service.impl;

import com.kimlongdev.shopme.modal.VerificationCode;
import com.kimlongdev.shopme.repository.VerificationCodeRepository;
import com.kimlongdev.shopme.service.VerificationService;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceImpl implements VerificationService {
    private final VerificationCodeRepository verificationCodeRepository;

    VerificationServiceImpl(VerificationCodeRepository verificationCodeRepository){

        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public VerificationCode createVerificationCode(String otp,String email) {
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if(isExist!=null) {
            verificationCodeRepository.delete(isExist);
        }

        // Create new verification code for seller
        VerificationCode verificationCode= new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        return verificationCodeRepository.save(verificationCode);

    }
}
