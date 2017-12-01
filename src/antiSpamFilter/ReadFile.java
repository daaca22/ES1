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
			//System.out.println(s1);
		}
		return rules;
	}
	public int numberOfRules(String file) { // este metodo vai contar o numero de regras do ficheiro rules.cf
		String s = new String();
		int number = 0; //contador
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}
		System.out.println("teste");
		while (scan.hasNextLine()) {// l� linha a linha
			s = scan.nextLine();
			number ++;
		}
		scan.close();

		return number;
	}

	public ArrayList<Email> readHam(String file) { // recebe o caminho para o ham.log ou spam.log
		ArrayList<Email> mailList = new ArrayList<Email>();// cria uma lista de mails
		String pesos[] = null;
		Email email = new Email("", pesos);
		try {
			scan = new Scanner(new File(file));

		} catch (FileNotFoundException e) {
			System.out.println("Error while trying to read file");
		}

		while (scan.hasNextLine()) {
			email.setName(scan.next());
			String currentline = scan.nextLine();

			String[] items = currentline.split(" ");
			pesos = new String[items.length];// vai ler o primeiro argumento de cada linha
			//System.out.println(email.getName());
			for (int i = 0; i < items.length; i++) {
				pesos[i] = items[i];// vai adicionar todos os pesos de cada linha a este vector
				//System.out.println(pesos[i]);
			}

			email.setPesoName(pesos);
			mailList.add(email);

		}

		scan.close();
		return mailList;
	}

	
	
}
