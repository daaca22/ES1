package antiSpamFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ReadFile {

	private Scanner scan;

	// lê o ficheiro Rules.cf se não tiver as regras no ficheiro vai gerar uns
	// valores entre -5 e 5 aleatorios para criar uma lista de regras
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
			System.out.println(s + p);
			Rule rule = new Rule(s, p);
			rules.add(rule);
		}
		scan.close();
		return rules;
	}

	public int numberOfRules(String file) { // este metodo vai contar o numero de regras do ficheiro rules.cf
		String s = new String();
		int number = 0; // contador
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}
		while (scan.hasNextLine()) {// l� linha a linha
			s = scan.nextLine();
			number++;
		}
		scan.close();

		return number;
	}

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

	public Double addRondomValue() {// apos carregar os caminhos carrega pesos aleatorios
		double randomValue = 0.0;
		double d = 0.0;
		Random r = new Random();
		randomValue = -5 + (5 - (-5)) * r.nextDouble();
		d = round(randomValue, 3);
		return d;
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
