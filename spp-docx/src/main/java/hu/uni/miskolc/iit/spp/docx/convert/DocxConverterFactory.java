package hu.uni.miskolc.iit.spp.docx.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class DocxConverterFactory {

	private static Logger LOG = LogManager.getLogger(DocxConverterFactory.class);
	
	private String osName;
	
	public DocxConverterFactory(String osName) {
		this.osName = osName;
	}
	
	public Docx2PDFConverter createDocxPdfConverter(File docxFile) throws NotSupportedOperationSystemException, NotSupportedFileExtensionException, FileNotFoundException {
		if(!isSupportedOS()) {
			LOG.fatal("Throw NotSupportedOperationSystemException this message: Could not create compiler, because this operation system is not supported.");
			throw new NotSupportedOperationSystemException("Could not create compiler, because this operation system is not supported.");
		}
		
		if(!isDocxFile(docxFile)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: The file's extension is not .docx: " + docxFile.getName());
			throw new NotSupportedFileExtensionException("The file's extension is not .docx: " + docxFile.getName());
		}
		
		String fileName = FilenameUtils.getBaseName(docxFile.getName());
		FileInputStream inputStream = new FileInputStream(docxFile);
		
		return new Docx2PDFConverter(fileName, inputStream);
	}

	private boolean isSupportedOS() {
		for(SupportedOperationSystems system : SupportedOperationSystems.values()) {
			if(osName.toLowerCase().contains(system.getStringValue())) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isDocxFile(File docxFile) {
		String extension = FilenameUtils.getExtension(docxFile.getName());
		
		return extension.equals(SupportedCompileableFileExtensions.DOCX.getStringValue());
	}
}