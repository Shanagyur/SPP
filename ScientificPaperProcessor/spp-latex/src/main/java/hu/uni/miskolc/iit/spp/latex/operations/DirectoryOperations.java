package hu.uni.miskolc.iit.spp.latex.operations;

import java.io.File;
import java.io.IOException;

public class DirectoryOperations {

	private static final String FILE_SEPARATOR = "file.separator";
	private static final String SUBDIR_NAME = "version_";
	
	public DirectoryOperations() {
	}
	
	public static File createDestinationDir(File file, String destinationDirectoryName) throws IOException {
		File directory = new File(file.getParentFile().getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + destinationDirectoryName);
		if(directory.exists() == false) {
			if(directory.mkdir() == false) {
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while(new File(directory.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + SUBDIR_NAME + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDirectory = new File(directory.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + SUBDIR_NAME + versionNo);
		if(destinationDirectory.mkdir() == false) {
			throw new IOException("Could not create directory: " + destinationDirectory.getAbsolutePath());
		}
		return destinationDirectory;
	}
}