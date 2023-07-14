package br.com.leiturando;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class LeiturandoApplicationTests {
	@InjectMocks
	LeiturandoApplication leiturandoApplication;

	@Value("${spring.mail.password}")
	String bucketName;

	@Test
	void contextLoads() {
		String[] args = new String[0];
		LeiturandoApplication.main(args);
		assertNotNull(bucketName);
	}

}
