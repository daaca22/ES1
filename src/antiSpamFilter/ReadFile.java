package antiSpamFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {

	private Scanner scan;

	public ArrayList<String> ReadRules(String file) {
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
			System.out.println(s1);
		}

		return rules;
	}

}
