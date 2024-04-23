package com.gypApp_main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
class GypAppMainApplicationTests {

	@Test
	void mainMethodShouldRunSuccessfully(CapturedOutput output) {

		GypAppMainApplication.main(new String[]{});

		assertTrue(output.toString().contains("Started GypAppMainApplication"));
	}

}
