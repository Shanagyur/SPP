package hu.uni.miskolc.iit.spp.latex.operations;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.investigations.SubmissionChecker;

public class FileOperations {

	private static SupportedFileNames fileNames;
	private static SupportedGeneratedFileExtensions generatedFileExtensions;
	private static SupportedCompileableTextFileExtensions textFileExtensions;
	
	public FileOperations() {
	}
	
	public static File findPDFFile(File sourceDir) throws SearchedFileNotExistsException {
		return FileOperations.findFile(sourceDir, FileOperations.fileNames, FileOperations.generatedFileExtensions.PDF.getStringValue());
	}
	
	public static File findTexFile(File sourceDir) throws SearchedFileNotExistsException {
		return FileOperations.findFile(sourceDir, FileOperations.fileNames, FileOperations.textFileExtensions.TEX.getStringValue());
	}
	
	private static File findFile(File sourceDir, SupportedFileNames fileNames, String extension) throws SearchedFileNotExistsException {
		File[] dirFiles = sourceDir.listFiles();
		if(dirFiles != null && dirFiles.length > 0) {
			for(File file : dirFiles) {
				if(file.isDirectory()) {
					return FileOperations.findFile(file, fileNames, extension);
				}
				boolean isAccaptedName = SubmissionChecker.isAccaptedName(file);
				boolean isCompileable = SubmissionChecker.isCompileable(file);
				boolean isGenerated = SubmissionChecker.isGenerated(file); 
				if(isAccaptedName && (isCompileable || isGenerated)) {
					if(isThatExtension(file, extension)) {
						return file;
					}
				}
			}
		}
		throw new SearchedFileNotExistsException();
	}
	
	private static boolean isThatExtension(File testedFile, String extension) {
		return extension.equals(FilenameUtils.getExtension(testedFile.getAbsolutePath()));
	}
}