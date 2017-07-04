package hu.uni.miskolc.iit.spp.latex.investigations;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;

public class FileNameTest {

	private File file;
	private static SupportedFileNames supportedFileNames;
	
	public FileNameTest(File file) {
		this.file = file;
	}
	
	public boolean eitherMatch() {
		String fileName = FilenameUtils.getBaseName(this.file.toString());
		for(SupportedFileNames names : FileNameTest.supportedFileNames.values()) {
			if(names.getStringValue() == fileName.toLowerCase()) {
				return true;
			}
		}
		return false;
	}
}