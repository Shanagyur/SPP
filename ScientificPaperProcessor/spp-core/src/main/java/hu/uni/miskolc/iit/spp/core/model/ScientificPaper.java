package hu.uni.miskolc.iit.spp.core.model;

import java.io.File;
import java.util.List;

public class ScientificPaper {
	
	private String title;
	private String paperAbstract;
	private List<String> keywords;
	private List<Author> authors;
	private File paper;
	
	public ScientificPaper(String title, String paperAbstract, List<String> keywords, List<Author> authors,
			File paper) {
		this.title = title;
		this.paperAbstract = paperAbstract;
		this.keywords = keywords;
		this.authors = authors;
		this.paper = paper;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public File getPaper() {
		return paper;
	}

	public void setPaper(File paper) {
		this.paper = paper;
	}

	@Override
	public String toString() {
		return "ScientificPaper [title=" + title + ", paperAbstract=" + paperAbstract + ", keywords=" + keywords
				+ ", authors=" + authors + ", paper=" + paper + "]";
	}
}
