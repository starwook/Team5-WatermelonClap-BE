package com.watermelon.server.event.admin.service;

import com.watermelon.server.exception.S3ImageFormatException;
import com.watermelon.server.S3ImageService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@SpringBootTest
class S3ImageServiceTest {

    private static final Logger log = LoggerFactory.getLogger(S3ImageServiceTest.class);
    @Autowired
    private S3ImageService s3ImageService;

    @Test
    void uploadImage() throws S3ImageFormatException, IOException {
        String dirName ="image/png";
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                dirName.getBytes(StandardCharsets.UTF_8));
        String imageUrl = s3ImageService.uploadImage(image);
        System.out.println(imageUrl);
    }

}