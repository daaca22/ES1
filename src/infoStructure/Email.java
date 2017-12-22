package infoStructure;

/**
 * This Class was created to help store the rules from each Email read from the
 * files spam.log and ham.log.
 * 
 * @author Daniel Coimbra and Goncalo Meireles
 *
 */
public class Email {

	private String name;
	private String[] values;

	/**
	 * This method is a constructor that given and name and an Array of strings,
	 * creates and Email.
	 * 
	 * @param name
	 *            name of the Email
	 * @param values
	 *            Array of strings with the names of the rules
	 * @return nothing.
	 */

	public Email(String name, String[] values) {
		this.name = name;
		this.values = values;
	}

	/**
	 * This method is to get the name of the email.
	 * 
	 * @param nothing
	 * 
	 * @return String that is the name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * This method is to give an Email a name.
	 * 
	 * @param name
	 *            String.
	 * @return nothing.
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method is to get all the rules names from an email.
	 * 
	 * @param nothing.
	 * 
	 * @return Array of strings with all the names.
	 */

	public String[] getValues() {
		return values;
	}

	/**
	 * This method is to give an Email an array of Strings, names of the rules.
	 * 
	 * @param values
	 *            Array of strings with all the names.
	 * @return nothing.
	 */
	public void setValues(String[] values) {
		this.values = values;
	}

}
