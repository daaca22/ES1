package antiSpamFilter;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestCase {

	@Test
	public final void test() {
		//fail("Not yet implemented"); // TODO
		ReadFile rf = new ReadFile();
		int regras = rf.numberOfRules("/Users/Utilizador/universidade/3ºAno/ES/rules.cf");
		assertTrue(regras == 335);
		ArrayList<String> readRules= rf.readRules("/Users/Utilizador/universidade/3ºAno/ES/rules.cf");
		//assertTrue(readRules==);
		
		
	}

}
