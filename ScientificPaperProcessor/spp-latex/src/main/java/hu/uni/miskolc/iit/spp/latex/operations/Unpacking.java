package hu.uni.miskolc.iit.spp.latex.operations;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;
import hu.uni.miskolc.iit.spp.latex.investigations.SubmissionChecker;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Unpacking {

	private static final String DESTIONATION_DIR_NAME = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
	
	public Unpacking() {
	}

	public static File unzip(File zipFile) throws NotSupportedArchiveExtensionException, IOException {
		if(SubmissionChecker.isSupportedArchive(zipFile)) {
			try {
				ZipFile validZipFile = new ZipFile(zipFile);
				File destinationDir = DirectoryOperations.createDestinationDir(zipFile, DESTIONATION_DIR_NAME);
				validZipFile.extractAll(destinationDir.getAbsolutePath());
				return destinationDir;
			} catch (ZipException e) {
				throw new IOException(e.getMessage());
			}
		}
		throw new NotSupportedArchiveExtensionException("Could not extract " + zipFile.getAbsolutePath() + " because not a zip file.");
	}
}