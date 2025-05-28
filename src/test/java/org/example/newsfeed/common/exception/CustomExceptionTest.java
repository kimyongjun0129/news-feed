package org.example.newsfeed.common.exception;

import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomExceptionTest {

    @Test
    void testCustomException(){
        CustomException e = new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);

        Assertions.assertEquals(CustomErrorCode.EMAIL_INVALID_FORMAT, e.getErrorCode());
        Assertions.assertEquals("이메일 형식이 잘못되었습니다.", e.getMessage());
    }
}
