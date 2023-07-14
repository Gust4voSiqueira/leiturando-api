package br.com.leiturando.service;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemException;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class FileServiceTest {
    @InjectMocks
    FileService fileService;

    @Mock
    AmazonS3 amazonS3;

    @Value("${aws.s3.audio.bucket}")
    private String bucketName;

    MultipartFile file;

    @Test
    public void uploadFileWithSuccess() throws FileSystemException {
        file = new MockMultipartFile("profile", "profile", MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());
        String result = fileService.uploadFile(file);

        assertTrue(result.contains(Objects.requireNonNull(file.getOriginalFilename())));
    }

    @Test(expected = FileSystemException.class)
    public void FailedToUploadFileWithoutName() throws FileSystemException {
        file = new MockMultipartFile("profile", null, MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());

        fileService.uploadFile(file);
    }
}
