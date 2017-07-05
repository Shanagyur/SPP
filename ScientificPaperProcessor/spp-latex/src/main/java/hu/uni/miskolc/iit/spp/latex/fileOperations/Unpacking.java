package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;
import hu.uni.miskolc.iit.spp.latex.investigations.ExtensionTest;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Unpacking {

	private static SupportedArchiveExtensions supportedArchiveExtensions;
	private static final String DESTIONATION_DIR_NAME = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
	
	public Unpacking() {
	}

	public static File unzip(File zipFile) throws NotSupportedArchiveExtensionException, IOException {
		ExtensionTest testedFile = new ExtensionTest(zipFile);
		if(testedFile.archiveExtensionSupported() == false) {
			throw new NotSupportedArchiveExtensionException("Could not extract " + zipFile.getAbsolutePath() + " because not a zip file.");
		}
		try {
			ZipFile validZipFile = new ZipFile(zipFile);
			File directoryForExtractedFiles = DestinationDirectory.createDestinationDir(zipFile, DESTIONATION_DIR_NAME);
			validZipFile.extractAll(directoryForExtractedFiles.getAbsolutePath());
			return directoryForExtractedFiles;
		} catch (ZipException e) {
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
}