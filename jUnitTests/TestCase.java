package antiSpamFilter;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestCase {

	@Test
	public final void test() {// Criar um vector com pesos, para testar os metodos FP e de FN e separar isto por metodos uma para cada classe
		// fail("Not yet implemented"); // TODO
		ReadFile rf = new ReadFile();
		int regras = rf.numberOfRules("/Users/danielcoimbra/Desktop/ES/rules.cf");
		assertTrue(regras == 335);
		ArrayList<Rule> readRules = rf.readRules("/Users/danielcoimbra/Desktop/ES/rules.cf");
		// assertTrue(readRules==);
		ArrayList<Email> readHams = rf.readHamOrSpam("/Users/danielcoimbra/Desktop/ES/ham.log.txt");

		Rule rule = new Rule("rule1", 1.1);
		String r = rule.getRule();
		double d = rule.getValue();
		assertTrue(r.equals("rule1"));
		assertTrue(d == 1.1);

		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		rulesList.add(rule);
		rf.writeRules("teste", rulesList);
		rf.addRondomValue();

		GUI gui = new GUI();
//		gui.calculateFP(readHams);
//		gui.calculateFN(readHams);
//		gui.getPeso("rule");
		ArrayList<Double> pesos = new ArrayList<Double>();
		gui.open(10, 10);
		

	}

}
