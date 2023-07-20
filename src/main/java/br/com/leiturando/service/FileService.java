package br.com.leiturando.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Base64;
import java.util.Objects;

import static br.com.leiturando.domain.Const.CHARACTERS_LIST;

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

    public String downloadFile(String fileName) throws IOException {
        S3Object s3Object;

        if(CHARACTERS_LIST.contains(fileName)) {
            s3Object = s3Client.getObject(bucketName, fileName + ".png");
        } else {
            s3Object = s3Client.getObject(bucketName, fileName);
        }

        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] imageByte = IOUtils.toByteArray(inputStream);

            return byteToBase64(imageByte);
        } catch (IOException e) {
            throw new IOException("Falha ao baixar o arquivo");
        }
    }

    public static String byteToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
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
