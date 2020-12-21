package com.example.megareddit.service;


import com.example.megareddit.dto.RegisterRequest;
import com.example.megareddit.exception.SpringRedditException;
import com.example.megareddit.model.NotificationEmail;
import com.example.megareddit.model.User;
import com.example.megareddit.model.VerificationToken;
import com.example.megareddit.repository.UserRepository;
import com.example.megareddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import static java.time.Instant.now;
import static com.example.megareddit.util.Constants.ACTIVATION_EMAIL;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;


    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build(
                "Thank you for signing up to Spring Reddit," +
                " please click on the below url to activate your account : " +
                ACTIVATION_EMAIL + "/" + token);
        mailService.sendMail(new NotificationEmail(
                "Please Activate your account",
                user.getEmail(),
                message));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException(
                "User Not Found with id - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

}

















