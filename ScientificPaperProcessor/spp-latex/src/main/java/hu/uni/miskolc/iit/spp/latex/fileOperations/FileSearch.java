package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;

import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.investigations.ExtensionTest;
import hu.uni.miskolc.iit.spp.latex.investigations.FileNameTest;

public class FileSearch {

	private File directory;
	
	public FileSearch(File directory) {
		this.directory = directory;
	}
	
	public File findTexFile() throws SearchedFileNotExistsException {
		File[] files = this.directory.listFiles();
		for(File file : files) {
			if(file.isDirectory() == true) {
				FileSearch directory = new FileSearch(file);
				directory.findTexFile();
			} else {
				FileNameTest testedFileName = new FileNameTest(file);
				ExtensionTest testedExtension = new ExtensionTest(file);
				if(testedFileName.eitherMatch() && testedExtension.fileExtensionSupported()) {
					return file;
				}
			}
		}
		throw new SearchedFileNotExistsException("Could not find tex file with supported name in this directory: " + this.directory.getAbsolutePath());
	}
}