package hu.uni.miskolc.iit.spp.latex.archive;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;

public abstract class LatexArchiveValidator {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DESTINATION_DIR_NAME = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
	private static final String SUBDIR_NAME = "version_";
	
	private File archiveFromFactory;
	
	protected LatexArchiveValidator(File archive) {
		this.archiveFromFactory = archive;
	}

	public final boolean validate(File archive) throws IOException {
		if(archiveFromFactory.equals(archive) == true) {
			File destinationDir = initDestinationDir(archive);
			unpack(archive, destinationDir);
			return isMainTeXFileContained(destinationDir);
		}
		throw new IOException("This and file from factory is not same.");
	}
	
	protected abstract void unpack(File archive, File destinationDir) throws IOException;

	private File initDestinationDir(File archive) throws IOException {
		File directory = new File(archive.getParentFile().getAbsolutePath() + FILE_SEPARATOR + DESTINATION_DIR_NAME);
		if(directory.exists() == false) {
			if(directory.mkdir() == false) {
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while(new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUBDIR_NAME + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDir = new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUBDIR_NAME + versionNo);
		if(destinationDir.mkdir() == false) {
			throw new IOException("Could not create directory: " + destinationDir.getAbsolutePath());
		}
		return destinationDir;
	}
	
	private boolean isMainTeXFileContained(File manuscriptRootDir) {
		File[] dirFiles = manuscriptRootDir.listFiles();
		if(dirFiles != null && dirFiles.length > 0) {
			for(File file : dirFiles) {
				if(file.isDirectory()) {
					return isMainTeXFileContained(file);
				}
				if(isAcceptedName(file) && isTexFile(file)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isAcceptedName(File file) {
		String fileName = FilenameUtils.getBaseName(file.getName()).toLowerCase();
		for(SupportedFileNames name : SupportedFileNames.values()) {
			if(fileName.equals(name.getStringValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean isTexFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileExtension.equals(SupportedCompileableTextFileExtensions.TEX.getStringValue());
	}
}