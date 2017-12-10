package antiSpamFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {

	private Scanner scan;

	public ArrayList<String> readRules(String file) {
		ArrayList<String> rules = new ArrayList<String>();
		String s = new String();
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");

		}

		while (scan.hasNext()) {
			s = scan.nextLine();
			rules.add(s);
		}
		scan.close();
		for (String s1 : rules) {
			// System.out.println(s1);
		}
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
		System.out.println("teste");
		while (scan.hasNextLine()) {// l� linha a linha
			s = scan.nextLine();
			number++;
		}
		scan.close();

		return number;
	}

	public ArrayList<Email> readHam(String file) { // recebe o caminho para o ham.log ou spam.log
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

	public String[] removeElement(String[] original, int element) {
		String[] n = new String[original.length - 1];
		System.arraycopy(original, 0, n, 0, element);
		System.arraycopy(original, element + 1, n, element, original.length - element - 1);
		return n;
	}

}
