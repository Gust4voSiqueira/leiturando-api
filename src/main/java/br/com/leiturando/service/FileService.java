package br.com.leiturando.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class FileService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.audio.bucket}")
    private String bucketName;

    public String downloadFile(String fileName) throws IOException {
        S3Object s3Object;

        s3Object = s3Client.getObject(bucketName, fileName + ".png");

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
}
