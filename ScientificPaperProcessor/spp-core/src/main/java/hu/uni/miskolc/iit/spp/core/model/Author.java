package hu.uni.miskolc.iit.spp.core.model;

public class Author {
	
	private String name;
	private String email;
	private String affiliation;
	
	public Author(String name, String email, String affiliation) {
		this.name = name;
		this.email = email;
		this.affiliation = affiliation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public String toString() {
		return "Author [name=" + name + ", email=" + email + ", affiliation=" + affiliation + "]";
	}
}
