package antiSpamFilter;

public class Email {


	private String name;
	private String[] pesoName;
	
	
	public Email(String name, String[] pesoName) {
		this.name = name;
		this.pesoName = pesoName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String[] getPesoName() {
		return pesoName;
	}


	public void setPesoName(String[] pesoName) {
		this.pesoName = pesoName;
	}
	
	

	
	
}
