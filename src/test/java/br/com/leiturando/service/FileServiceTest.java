package br.com.leiturando.service;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemException;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    FileService fileService;

    @Mock
    AmazonS3 amazonS3;

    @Value("${aws.s3.audio.bucket}")
    private String bucketName;

    MultipartFile file;

    @Test
    void uploadFileWithSuccess() throws FileSystemException {
        file = new MockMultipartFile("profile", "profile", MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());
        String result = fileService.uploadFile(file);

        Assertions.assertTrue(result.contains(Objects.requireNonNull(file.getOriginalFilename())));
    }

    @Test
    void FailedToUploadFileWithoutName() {
        file = new MockMultipartFile("profile", null, MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());

        FileSystemException exception = Assertions.assertThrows(FileSystemException.class,
                () -> fileService.uploadFile(file));

        Assertions.assertEquals("Invalid file name", exception.getLocalizedMessage());
    }
}
