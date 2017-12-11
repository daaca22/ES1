package antiSpamFilter;

public class Rule {

	private String rule;
	private Double value;

	public Rule(String rule, Double value) {
		super();
		this.rule = rule;
		this.value = value;
	}

	public String getRule() {
		return rule;
	}
	public Double getValue() {
		return value;
	}

 
}
