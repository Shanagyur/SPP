package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;

public class SubmissionChecker {

	private static SupportedFileNames supportedFileNames;
	private static SupportedOperationSystems supportedOperationSystems;
	private static SupportedArchiveExtensions supportedArchiveExtensions;
	private static SupportedGeneratedFileExtensions supportedGeneratedFileExtensions;
	private static SupportedCompileableTextFileExtensions supportedCompileableTextFileExtensions;

	private SubmissionChecker() {
	}

	public static boolean isAccaptedName(File testedFile) {
		String fileName = FilenameUtils.getBaseName(testedFile.getName());
		for(SupportedFileNames name : SubmissionChecker.supportedFileNames.values()) {
			if(fileName.toLowerCase().equals(name.getStringValue())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSupportedOS(String usedOS) {
		String osName = usedOS.toLowerCase();
		for(SupportedOperationSystems name : SubmissionChecker.supportedOperationSystems.values()) {
			if(osName.contains(name.getStringValue())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSupportedArchive(File testedFile) {
		String fileExtension = FilenameUtils.getExtension(testedFile.toString());
		for(SupportedArchiveExtensions extension : SubmissionChecker.supportedArchiveExtensions.values()) {
			if(extension.getStringValue().equals(fileExtension)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isGenerated(File testedFile) {
		String fileExtension = FilenameUtils.getExtension(testedFile.toString());
		for(SupportedGeneratedFileExtensions extension : SubmissionChecker.supportedGeneratedFileExtensions.values()) {
			if(extension.getStringValue().equals(fileExtension)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCompileable(File testedFile) {
		String fileExtension = FilenameUtils.getExtension(testedFile.toString());
		for(SupportedCompileableTextFileExtensions extension : SubmissionChecker.supportedCompileableTextFileExtensions.values()) {
			if(extension.getStringValue().equals(fileExtension)) {
				return true;
			}
		}
		return false;
	}
}