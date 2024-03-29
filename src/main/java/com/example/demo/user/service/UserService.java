package com.example.demo.user.service;


import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.infra.UserEntity;

import java.time.Clock;
import java.util.UUID;

import com.example.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;

    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from( userCreate );
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());

        return user;
    }

    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        user.update(userUpdate);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user.login();
        // JPA와의 의존성이 끉어졌기에 직접 save를 해주어야 한다.
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        if (!certificationCode.equals(user.getCertificationCode())) {
            throw new CertificationCodeNotMatchedException();
        }
        user = user.certificate( certificationCode );
        userRepository.save(user);
    }


}