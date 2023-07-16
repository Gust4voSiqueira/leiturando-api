package br.com.leiturando;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
class LeiturandoApplicationTests {
	@InjectMocks
	LeiturandoApplication leiturandoApplication;

	@Value("${aws.s3.audio.bucket}")
	String bucketName;

	@Test
	void contextLoads() {
		String[] args = new String[0];
		LeiturandoApplication.main(args);
		Assertions.assertNotNull(bucketName);
	}

}
