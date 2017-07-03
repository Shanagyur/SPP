package hu.uni.miskolc.iit.spp.latex.fileOperations;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.latex.investigations.ExtensionTest;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Unpacking {

	private static Collection<String> possibleArchiveExtension;
	private static final String DESTIONATION_DIR_NAME = "targetDir";
	
	static {
		possibleArchiveExtension = new HashSet<>();
		possibleArchiveExtension.add("zip");
	}
	
	public static File unzip(File zipFile) throws NotSupportedFileExtensionException, IOException {
		ExtensionTest testedFile = new ExtensionTest(zipFile, possibleArchiveExtension);
		if(testedFile.extensionTest() == false) {
			throw new NotSupportedFileExtensionException("Could not extract the file, because not a .zip file: " + zipFile.getAbsolutePath());
		}
		try {
			ZipFile validZipFile = new ZipFile(zipFile);
			File directoryWithExtractFiles = DestinationDirectory.createDestinationDir(zipFile, DESTIONATION_DIR_NAME);
			validZipFile.extractAll(directoryWithExtractFiles.getAbsolutePath());
			return directoryWithExtractFiles; 
		} catch (ZipException e) {
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
}