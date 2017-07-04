package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;
import java.util.Collection;

import hu.uni.miskolc.iit.spp.core.model.exception.WantedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.investigations.ExtensionTest;
import hu.uni.miskolc.iit.spp.latex.investigations.FileNameTest;

public class WantedFile {

	private File directory;
	private Collection<String> possibleFileNames;
	private Collection<String> possibleFileExtensions;
	
	public WantedFile(File directory, Collection<String> possibleFileNames, Collection<String> possibleFileExtensions) {
		this.directory = directory;
		this.possibleFileNames = possibleFileNames;
		this.possibleFileExtensions = possibleFileExtensions;
	}

	public File getWantedFile() throws WantedFileNotExistsException {
		File[] files = this.directory.listFiles();
		for(File file : files) {
			if(file.isDirectory() == true) {
				WantedFile dir = new WantedFile(file, this.possibleFileNames, this.possibleFileExtensions);
				dir.getWantedFile();
			} else {
				FileNameTest testedFileName = new FileNameTest(file, this.possibleFileNames);
				ExtensionTest testedExtension = new ExtensionTest(file, this.possibleFileExtensions);
				if((testedFileName.eitherMatch() && testedExtension.extensionTest()) == true) {
					return file;
				}
			}
		}
		throw new WantedFileNotExistsException("Could not find file with that name(s): " + this.possibleFileNames.toString() + 
												"and with that extension(s): " + this.possibleFileExtensions.toString());
	}
}