package hu.uni.miskolc.iit.spp.docx.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class Docx2PDFConverter {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static Logger LOG = LogManager.getLogger(Docx2PDFConverter.class);
	
	private String fileName;
	private InputStream inputStream;
	
	protected Docx2PDFConverter(String fileName, FileInputStream fileInputStream) {
		this.fileName = fileName;
		this.inputStream = fileInputStream;
	}
	
	public File generatePDF(File destinationDir) throws IOException, SearchedFileNotExistsException {
		XWPFDocument document = new XWPFDocument(inputStream);
		OutputStream outputStream = createOutputStream(destinationDir);
		PdfOptions options = PdfOptions.create();
		PdfConverter.getInstance().convert(document, outputStream, options);
		
		return getPdfFile(destinationDir);
	}

	private File getPdfFile(File destinationDir) throws SearchedFileNotExistsException {
		File[] dirFiles = destinationDir.listFiles();
		for(File file : dirFiles) {
			if(isWantedPdfFile(file)) {
				
				return file;
			}
		}
		LOG.fatal("Throw SearchedFileNotExistsException with this message: Could not found file with this name: " + this.fileName);
		throw new SearchedFileNotExistsException("Could not found file with this name: " + this.fileName);
	}

	private boolean isWantedPdfFile(File file) {
		String name = FilenameUtils.getBaseName(file.getName());
		String extension = FilenameUtils.getExtension(file.getName());
		
		if(this.fileName.equals(name) && extension.equals(SupportedGeneratedFileExtensions.PDF.getStringValue())) {
			
			return true;
		}
		
		return false;
	}

	private OutputStream createOutputStream(File destinationDir) throws IOException, FileNotFoundException {
		File blankPdfFile = new File(destinationDir.getAbsolutePath() + FILE_SEPARATOR + fileName + "." + SupportedGeneratedFileExtensions.PDF.getStringValue());
		if(!blankPdfFile.createNewFile()) {
			LOG.fatal("Throw IOException in createOutputStream method.");
		}
		FileOutputStream outputStream = new FileOutputStream(blankPdfFile);
		
		return outputStream;
	}
}