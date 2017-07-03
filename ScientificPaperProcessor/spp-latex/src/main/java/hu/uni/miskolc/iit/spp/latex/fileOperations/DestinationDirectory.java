package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;
import java.io.IOException;

public class DestinationDirectory {

	private static final String FILE_SEPARATOR = "file.separator";
	
	public static File createDestinationDir(File file, String destinationDirectoryName) throws IOException {
		File directory = new File(file.getParentFile().getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + destinationDirectoryName);
		if(directory.exists() == false) {
			if(directory.mkdir() == false) {
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while(new File(directory.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_" + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDirectory = new File(directory.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + versionNo);
		if(destinationDirectory.mkdir() == false) {
			throw new IOException("Could not create directory: " + destinationDirectory.getAbsolutePath());
		}
		return destinationDirectory;
	}
}