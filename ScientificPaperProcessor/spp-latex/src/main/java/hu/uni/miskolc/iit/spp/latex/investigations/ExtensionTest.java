package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

public class ExtensionTest {

	private File file;
	private Collection<String> possibleExtensions;

	public ExtensionTest(File file, Collection<String> possibleExtension) {
		this.file = file;
		this.possibleExtensions = possibleExtension;
	}

	public boolean extensionTest() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		return this.possibleExtensions.contains(fileExtension);
	}
}