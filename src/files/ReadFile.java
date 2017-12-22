package files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import infoStructure.Email;
import infoStructure.Rule;

/**
 * This Class handles everything that have something to do with files, this
 * class uses Scanner to read and write in the files.
 * 
 * @author Daniel Coimbra and Goncalo Meireles
 *
 */

public class ReadFile {

	private Scanner scan;

	/**
	 * This method was made to read the Rules.cf file, if the rules.cf file does not
	 * have the values of the rules, it will use the addRandomValue() method, to
	 * generate random Doubles.
	 * 
	 * @param file
	 *            String that has the path for the rules.cf
	 * @return ArrayList of class Rule.
	 */
	public ArrayList<Rule> readRules(String file) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		String s = new String();
		Double p;
		try {
			scan = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}
		while (scan.hasNext()) {
			s = scan.next();
			if (scan.hasNextDouble()) {
				p = scan.nextDouble();
			} else {
				p = addRondomValue();
			}
			Rule rule = new Rule(s, p);
			rules.add(rule);
		}
		scan.close();
		return rules;
	}

	/**
	 * This method was made to count the number of rules in the rules.cf file.
	 * 
	 * @param file
	 *            String that has the path for the ham.log.txt or spam.log.txt file.
	 * @return integer with number of rules.
	 */
	public int numberOfRules(String file) {
		String s = new String();
		int number = 0; // contador
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}
		while (scan.hasNextLine()) {// le linha a linha
			s = scan.nextLine();
			number++;
		}
		scan.close();

		return number;
	}

	/**
	 * This method was made to read the ham.log.txt and spam.log.txt file.
	 * 
	 * @param file
	 *            String that has the path for the ham.log.txt or spam.log.txt file.
	 * @return ArrayList of class Email.
	 */

	public ArrayList<Email> readHamOrSpam(String file) { // recebe o caminho para o ham.log ou spam.log
		ArrayList<Email> mailList = new ArrayList<Email>();// cria uma lista de mails
		String pesos[] = null;
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}

		while (scan.hasNextLine()) {
			Email email = new Email("", pesos);
			email.setName(scan.next());

			String currentline = scan.nextLine();
			String[] items = currentline.split("	");

			pesos = new String[items.length];// vai ler o primeiro argumento de cada linha
			pesos = removeElement(items, 0);

			email.setValues(pesos);
			mailList.add(email);

		}

		scan.close();

		return mailList;
	}

	private String[] removeElement(String[] original, int element) {
		String[] n = new String[original.length - 1];
		System.arraycopy(original, 0, n, 0, element);
		System.arraycopy(original, element + 1, n, element, original.length - element - 1);
		return n;
	}

	/**
	 * This method was made to write the rules name and their values in the rules.cf
	 * file.
	 * 
	 * @param file
	 *            String that has the path for the ham.log.txt or spam.log.txt file.
	 * @param listRules
	 *            ArrayList with Rules that will iterate to write in the file
	 * @return nothing.
	 */

	public void writeRules(String file, ArrayList<Rule> listRules) {

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (Rule r : listRules) {
				bw.write(r.getRule() + " " + r.getValue());
				bw.newLine();
			}

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	/**
	 * This method was made to generate random Doubles between -5 and 5.
	 * 
	 * @param nothing
	 * 
	 * @return random Double between -5 and 5.
	 */
	public Double addRondomValue() {
		double randomValue = 0.0;
		Random r = new Random();
		randomValue = -5 + (5 - (-5)) * r.nextDouble();
		return randomValue;
	}

	/**
	 * This method was made to read the AntiSpamFilterProblem.NSGAII.rf file, and it
	 * will fine the best values for FP and FN, and also fine the line that they are
	 * in.
	 * 
	 * @param nothing
	 * 
	 * @return Array of Doubles where the 2 first positions are the FP and FN
	 *         values, respectively and the third position is the line that they are
	 *         in.
	 */
	public Double[] readVectorPesos() {

		Double[] d = new Double[3];
		Double fp = 0.0;
		Double fn = 0.0;
		Double oldFp = 10000.0;
		Double oldFn = 0.0;
		int row = 0;
		int c = 1;
		try {
			scan = new Scanner(new File("experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem.NSGAII.rf"));
		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}
		while (scan.hasNext()) {

			fp = scan.nextDouble();
			fn = scan.nextDouble();

			if (oldFp > fp) {
				oldFp = fp;
				oldFn = fn;
				row = c;
			}
			if (oldFp == fp && oldFn > fn) {
				oldFp = fp;
				oldFn = fn;
				row = c;
			}
			c++;

		}
		d[0] = oldFp;
		d[1] = oldFn;
		d[2] = (double) row;
		scan.close();
		return d;
	}

	/**
	 * This method was made to read the AntiSpamFilterProblem.NSGAII.rs file, and it
	 * will read the values of the line that is given.
	 * 
	 * @param integer
	 *            number of the line.
	 * 
	 * @return ArrayList of Doubles with the values for the rules from the automatic
	 *         configuration
	 */
	public ArrayList<Double> getPesosFromFileAuto(int row) {
		int s = 0;
		String line = null;
		ArrayList<Double> pesos = new ArrayList<Double>();
		try {
			scan = new Scanner(new File("experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem.NSGAII.rs"));
		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}

		while (scan.hasNextLine()) {
			s++;
			if (s == row) {
				line = scan.nextLine();
			}
			scan.nextLine();
		}

		String[] p = line.split(" ");
		for (int i = 0; i != p.length; i++) {
			pesos.add(Double.parseDouble(p[i]));
		}

		return pesos;
	}
}
