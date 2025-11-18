package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.config.JwtProvider;
import com.kimlongdev.shopme.domain.AccountStatus;
import com.kimlongdev.shopme.exception.SellerException;
import com.kimlongdev.shopme.modal.Seller;
import com.kimlongdev.shopme.modal.VerificationCode;
import com.kimlongdev.shopme.repository.VerificationCodeRepository;
import com.kimlongdev.shopme.response.ApiResponse;
import com.kimlongdev.shopme.service.EmailService;
import com.kimlongdev.shopme.service.SellerService;
import com.kimlongdev.shopme.service.VerificationService;
import com.kimlongdev.shopme.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationService verificationService;
    private final JwtProvider jwtProvider;


    /*
1. Client gửi request với body chứa email
   ↓
2. Server tạo OTP ngẫu nhiên (6 chữ số)
   ↓
3. Lưu OTP vào database (bảng verification_code)
   - Liên kết với email
   - Lưu thời gian tạo
   ↓
4. Gửi email chứa OTP đến seller
   - Subject: "Login OTP"
   - Body: "your login otp is - " + OTP
   ↓
5. Trả về response: "otp sent"

     */
    @PostMapping("/send/login-top")
    public ResponseEntity<ApiResponse> sentLoginOtp(@RequestBody VerificationCode req) throws MessagingException, SellerException {
        String otp = OtpUtils.generateOTP();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, req.getEmail());

        String subject = "Login OTP";
        String text = "your login otp is - ";
        emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);

        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    /*
1. Client gửi OTP qua URL path
   ↓
2. Server tìm VerificationCode trong database theo OTP
   ↓
3. Kiểm tra OTP có tồn tại và khớp không
   - Nếu sai → throw exception "wrong otp..."
   ↓
4. Gọi sellerService.verifyEmail(email, otp)
   - Tìm seller theo email
   - Cập nhật emailVerified = true
   - Cập nhật accountStatus (có thể)
   - Xóa hoặc vô hiệu hóa OTP
   ↓
5. Trả về thông tin seller đã verified
1. Client gửi request với path variable chứa otp
     */
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws SellerException {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws SellerException, MessagingException {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtils.generateOTP();
        VerificationCode verificationCode = verificationService.createVerificationCode(otp, seller.getEmail());

        String subject = "Email Verification Code";
        String text = "Welcome to ShopMe, verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @RequestHeader("Authorization") String jwt) throws SellerException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws SellerException {

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws SellerException {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();

    }
}