package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileExtensions;

public class ExtensionTest {

	private File file;
	private static SupportedArchiveExtensions supportedArchiveExtensions;
	private static SupportedFileExtensions supportedFileExtensions;

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
	
	public boolean fileExtensionSupported() {
		String fileExtension = FilenameUtils.getExtension(this.file.toString());
		for(SupportedFileExtensions extension : ExtensionTest.supportedFileExtensions.values()) {
			if(extension.getStringValue() == fileExtension) {
				return true;
			}
		}
		return  false;
	}
}