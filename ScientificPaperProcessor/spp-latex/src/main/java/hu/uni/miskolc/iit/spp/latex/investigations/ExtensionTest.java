package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;

public class ExtensionTest {

	private File file;
	private static SupportedArchiveExtensions supportedArchiveExtensions;
	private static SupportedGeneratedFileExtensions supportedGeneratedFileExtensions;
	private static SupportedCompileableTextFileExtensions supportedCompileableTextFileExtensions;

	public ExtensionTest(File file) {
		this.file = file;
	}

	public boolean archiveExtensionSupported() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		for(SupportedArchiveExtensions extension : ExtensionTest.supportedArchiveExtensions.values()) {
			if(extension.getStringValue() == fileExtension) {
				return true;
			}
		}
		return false;
	}
	
	public boolean compileableTextFileExtensionSupported() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		for(SupportedCompileableTextFileExtensions extension : ExtensionTest.supportedCompileableTextFileExtensions.values()) {
			if(extension.getStringValue() == fileExtension) {
				return true;
			}
		}
		return false;
	}
	
	public boolean generatedFileExtensionSupported() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		for(SupportedGeneratedFileExtensions extension : ExtensionTest.supportedGeneratedFileExtensions.values()) {
			if(extension.getStringValue() == fileExtension) {
				return true;
			}
		}
		return false;
	}
}