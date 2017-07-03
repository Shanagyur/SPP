package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

public class ExtensionTest {

	private File file;
	private Collection<String> possibleExtension;

	private ExtensionTest(File file, Collection<String> possibleExtension) {
		this.file = file;
		this.possibleExtension = possibleExtension;
	}

	public static ExtensionTest createTexTestClass(File file, Collection<String> possibleTexFileExtension) {
		return new ExtensionTest(file, possibleTexFileExtension);
	}
	
	public static ExtensionTest createArchiveTestClass(File file, Collection<String> possibleArchiveFileExtension) {
		return new ExtensionTest(file, possibleArchiveFileExtension);
	}
	
	public static ExtensionTest createGeneratedFileTestClass(File file, Collection<String> possibleGeneratedFileExtension) {
		return new ExtensionTest(file, possibleGeneratedFileExtension);
	}
	
	public boolean extensionTest() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		return this.possibleExtension.contains(fileExtension);
	}
}
