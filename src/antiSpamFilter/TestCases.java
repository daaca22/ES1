package antiSpamFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestCases {

	@Test
	void test() {
		//fail("Not yet implemented");
		ReadFile rf = new ReadFile();
		int regras = rf.numberOfRules("/Users/danielcoimbra/Desktop/ES/rules.cf");
		assertTrue(regras == 335);
	}

}
