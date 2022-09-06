package com.oven;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void getPass() {
        String name = encryptor.encrypt("5217");
        System.out.println("加密结果：" + name);
    }

    @Test
    public void passDecrypt() {
        String username = encryptor.decrypt("lFuHUaMta6RjpJtBKRKBcg==");
        System.out.println("解密结果：" + username);
    }

}