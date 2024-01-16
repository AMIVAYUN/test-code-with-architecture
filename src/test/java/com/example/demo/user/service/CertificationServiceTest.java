package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.port.MailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificationServiceTest {


    @Test
    public void 이메일과컨텐츠가제대로만들어보내지는지테스트(){
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService( mailSender );
        certificationService.send("sdfasdf@naver.com",1,"aaaaaa-aaaaaaaaaaa-aaaaaaaa");

        assertThat(mailSender.email).isEqualTo("sdfasdf@naver.com");
        assertThat(mailSender.title).isEqualTo("Please certify your email address");
    }
}
