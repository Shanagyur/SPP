package hu.uni.miskolc.iit.spp.latex.operations;

import java.io.File;

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
		return FileOperations.findFile(sourceDir, fileNames, FileOperations.generatedFileExtensions.PDF.getStringValue());
	}
	
	public static File findTexFile(File sourceDir) throws SearchedFileNotExistsException {
		return FileOperations.findFile(sourceDir, FileOperations.fileNames, FileOperations.textFileExtensions.TEX.getStringValue());
	}
	
	private static File findFile(File sourceDir, SupportedFileNames fileNames, String extension) throws SearchedFileNotExistsException {
		if(sourceDir.isDirectory()) {
			for(File dirFiles : sourceDir.listFiles()) {
				File wantedFile = FileOperations.findFile(dirFiles, fileNames, extension);
				if(wantedFile != null) {
					return wantedFile;
				}
			}
		}
		boolean isAccaptedName = SubmissionChecker.isAccaptedName(sourceDir);
		boolean isCompileable = SubmissionChecker.isCompileable(sourceDir);
		boolean isGenerated = SubmissionChecker.isGenerated(sourceDir); 
		if(isAccaptedName && (isCompileable || isGenerated)) {
			return sourceDir;
		}
		throw new SearchedFileNotExistsException();
	}
}