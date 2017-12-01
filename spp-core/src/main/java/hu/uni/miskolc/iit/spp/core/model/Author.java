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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Author author = (Author) o;

		if (!name.equals(author.name)) return false;
		if (!email.equals(author.email)) return false;
		return affiliation.equals(author.affiliation);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + email.hashCode();
		result = 31 * result + affiliation.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Author [name=" + name + ", email=" + email + ", affiliation=" + affiliation + "]";
	}
}