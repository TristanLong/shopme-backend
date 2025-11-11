package com.kimlongdev.shopme.service.impl;

import com.kimlongdev.shopme.config.JwtProvider;
import com.kimlongdev.shopme.domain.USER_ROLE;
import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.modal.VerificationCode;
import com.kimlongdev.shopme.repository.CartRepository;
import com.kimlongdev.shopme.repository.UserRepository;
import com.kimlongdev.shopme.repository.VerificationCodeRepository;
import com.kimlongdev.shopme.request.LoginRequest;
import com.kimlongdev.shopme.response.AuthResponse;
import com.kimlongdev.shopme.response.SignUpRequest;
import com.kimlongdev.shopme.service.AuthService;
import com.kimlongdev.shopme.service.EmailService;
import com.kimlongdev.shopme.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthComboBoxUI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;

    @Override
    public String createUser(SignUpRequest signUpRequest) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signUpRequest.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(signUpRequest.getOtp())) {
            throw new Exception("wrong otp...");
        }


        User user = userRepository.findByEmail(signUpRequest.getEmail());

        if (user == null) {
            User newUser = new User();
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setFullName(signUpRequest.getFullName());
            newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            newUser.setMobile("0988888888");
            newUser.setPassword(passwordEncoder.encode(signUpRequest.getOtp()));

            user = userRepository.save(newUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        // Assign ROLE_CUSTOMER authority
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        // Create an Authentication object with the user's email and authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                signUpRequest.getEmail(),
                null,
                authorities
        );

        // Set the authentication in the SecurityContext
        // user is now logged in after sign up
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public void sendLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            String actualEmail = email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(actualEmail);
            if (user == null) {
                throw new Exception("User not exist");
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        // Remove existing OTP
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        // Generate and save new OTP
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        // Send OTP via email
        String subject = "Your OTP Code";
        String text = "Your OTP code is: ";

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public AuthResponse signingIn(LoginRequest loginRequest) throws Exception {
        String username = loginRequest.getEmail();
        String otp = loginRequest.getOtp();

        Authentication authentication = authenticate(username, otp);

        // Đánh dấu người dùng đã đăng nhập, các request sau có thể lấy thông tin từ SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Tạo JWT token chứa thông tin và quyền hạn
        String token = jwtProvider.generateToken(authentication);

        // Tạo response trả về cho client
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(token);
        authResponse.setMessage("Login successful");

        // Lấy vai trò của người dùng từ Authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(role));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
        // Load user from database
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new Exception("Invalid username or otp");
        }

        // Verify OTP
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp...");
        }


        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
