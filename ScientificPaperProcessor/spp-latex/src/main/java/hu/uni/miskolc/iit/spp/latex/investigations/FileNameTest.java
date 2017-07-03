package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

public class FileNameTest {

	private File file;
	private Collection<String> possibleFileNames;
	
	public FileNameTest(File file, Collection<String> possibleFileNames) {
		this.file = file;
		this.possibleFileNames = possibleFileNames;
	}
	
	public boolean eitherMatch() {
		String fileName = FilenameUtils.getBaseName(this.file.toString());
		return possibleFileNames.contains(fileName);
	}
}