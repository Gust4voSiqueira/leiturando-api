package br.com.leiturando.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Objects;

@Service
public class FileService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.audio.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws FileSystemException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));

        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws FileSystemException {
        if (Objects.equals(file.getOriginalFilename(), "")) {
            throw new FileSystemException("Invalid file name");
        }
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new FileSystemException("Error converting MultipartFile to file");
        }
        return convertedFile;
    }
}
