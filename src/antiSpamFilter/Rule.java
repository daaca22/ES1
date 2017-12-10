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

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
