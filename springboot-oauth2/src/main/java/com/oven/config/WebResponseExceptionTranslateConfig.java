package com.oven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * web全局异常返回处理器
 *
 * @author Oven
 */
@Configuration
public class WebResponseExceptionTranslateConfig {

    /**
     * 自定义登录或者鉴权失败时的返回信息
     */
    @Bean(name = "webResponseExceptionTranslator")
    public WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                OAuth2Exception body = responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                if (400 == responseEntity.getStatusCode().value()) {
                    assert body != null;
                    if ("Bad credentials".equals(body.getMessage())) {
                        return new ResponseEntity<>(new OAuth2Exception("您输入的用户名或密码错误"), headers, HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(body, headers, responseEntity.getStatusCode());
            }
        };
    }

}
