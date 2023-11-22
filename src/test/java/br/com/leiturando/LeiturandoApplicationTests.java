package br.com.leiturando;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SuppressWarnings("squid:S2699")
class LeiturandoApplicationTests {
	@InjectMocks
	LeiturandoApplication leiturandoApplication;

	@Test
	void contextLoads() {
		String[] args = new String[0];
		LeiturandoApplication.main(args);
	}

}
