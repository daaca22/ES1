package antiSpamFilter;

/**
 * This Class was created to help store the values read from the rules.cf file,
 * and to make easier to calculate the number of Fp`s and Fn`s in other classes.
 * 
 * @author Daniel Coimbra and Goncalo Meireles
 *
 */

public class Rule {

	private String ruleName;
	private Double value;

	/**
	 * This method is a constructor that given and name and a Double, creates a
	 * Rule.
	 * 
	 * @param ruleName
	 *            name of the rule
	 * @param value
	 *            value of the name given above
	 * @return nothing.
	 */

	public Rule(String rule, Double value) {
		super();
		this.ruleName = rule;
		this.value = value;
	}

	/**
	 * This method is to get a name of a certain Rule.
	 * 
	 * @param nothing.
	 * 
	 * @return String name of the Rule.
	 */

	public String getRule() {
		return ruleName;
	}

	/**
	 * This method is to get a value of a certain Rule.
	 * 
	 * @param nothing.
	 * 
	 * @return Double value of the Rule.
	 */

	public Double getValue() {
		return value;
	}

}
