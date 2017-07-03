package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.WantedFileNotExistsException;

public class WantedFile {

	private File directory;
	private Collection<String> possibleFileNames;
	
	private WantedFile(File directory, Collection<String> possibleFileNames) {
		this.directory = directory;
		this.possibleFileNames = possibleFileNames;
	}
	
	public static WantedFile wantedFileClassMorePossibleFileNames(File directory, Collection<String> possibleFileNames) {
		return new WantedFile(directory, possibleFileNames);
	}
	
	public static WantedFile wantedFileClassOnePossibleFileName(File directory, String possibleFileName) {
		Collection<String> possibleFileNames = new HashSet<>();
		possibleFileNames.add(possibleFileName);
		return new WantedFile(directory, possibleFileNames);
	}
	
	public File getWantedFile() throws WantedFileNotExistsException {
		File[] files = this.directory.listFiles();
		for(File file : files) {
			if(file.isDirectory() == false) {
				if(this.possibleFileNames.contains(file.getName()) == true) {
					return file;
				}
			} else {
				WantedFile dir = new WantedFile(file, this.possibleFileNames);
				dir.getWantedFile();
			}
		}
		throw new WantedFileNotExistsException();
	}
}
