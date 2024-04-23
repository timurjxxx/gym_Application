package com.gypApp_workLoadService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)

class GypAppWorkLoadServiceApplicationTests {

	@Test
	void mainMethodShouldRunSuccessfully(CapturedOutput output) {

		GypAppWorkLoadServiceApplication.main(new String[]{});

		assertTrue(output.toString().contains("Started GypAppWorkLoadServiceApplication"));
	}
}

