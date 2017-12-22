package antiSpamFilter;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.junit.Test;

import Interface.GUI;
import files.ReadFile;
import infoStructure.Email;
import infoStructure.Rule;

public class Tests {

	@Test
	public final void test() {

		ReadFile rf = new ReadFile();
		int regras = rf.numberOfRules("/Users/danielcoimbra/Desktop/ES/rules.cf");
		assertTrue(regras == 335);
		ArrayList<Rule> readRules = rf.readRules("/Users/danielcoimbra/Desktop/ES/rules.cf");

		ArrayList<Rule> readRulesWithNoValues = rf.readRules("/Users/danielcoimbra/Desktop/rules.cf");// sem valores vai
																										// gerar random
																										// values

		ArrayList<Email> readHams = rf.readHamOrSpam("/Users/danielcoimbra/Desktop/ES/ham.log.txt");

		ArrayList<Email> readSpam = rf.readHamOrSpam("/Users/danielcoimbra/Desktop/ES/spam.log.txt");

		rf.getPesosFromFileAuto(2);

		Rule rule = new Rule("rule1", 1.1);
		String r = rule.getRule();
		double d = rule.getValue();
		assertTrue(r.equals("rule1"));
		assertTrue(d == 1.1);

		readRules.add(rule);

		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		rulesList.add(rule);
		rf.writeRules("teste", rulesList);
		rf.addRondomValue();
		Email.class.getName();
		String[] em = new String[10];
		Email m = new Email(" ", em);
		m.getName();
		m.getValues();

		GUI gui = new GUI();
		gui.main(null);

		ArrayList<Double> pesos = new ArrayList<Double>();
		gui.open(400, 700);
		gui.addFrameContent();
		gui.generateLatexReport();

		ArrayList<Email> hamList = new ArrayList<>();
		ArrayList<Email> spamList = new ArrayList<>();
		ArrayList<Rule> listRules = new ArrayList<>();
		int fp = gui.calculateFP(hamList, listRules);
		int fn = gui.calculateFN(spamList, listRules);

		gui.getPeso("Rule", listRules);
		gui.generateR();

		JButton search1 = new JButton();

		search1.addActionListener(null);
		ArrayList<Double> listD = new ArrayList<>();

		DefaultTableModel model = new DefaultTableModel();
		ArrayList<Rule> Rul = gui.getRulesList(model);
		gui.setModelContentManual(listRules, model);
		gui.setModelContentAuto(listRules, model, listD);
		gui.clearTable(model);

		String[] rules = new String[5];
		gui.isSpam(rules, listRules);

		Double d1[] = new Double[3];
		d1 = rf.readVectorPesos();
		rf.getPesosFromFileAuto(d1[2].intValue());

		DefaultTableModel modelManual = new DefaultTableModel();
		gui.clearTable(modelManual);
		pesos.add(d);

	}

}
