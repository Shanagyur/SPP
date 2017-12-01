package hu.uni.miskolc.iit.ssp.doc.convert;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;

import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;

public class Doc2PDFConverter {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static Logger LOG = LogManager.getLogger(Doc2PDFConverter.class);
	
	private IConverter converter;

	protected Doc2PDFConverter(IConverter converter) {
		this.converter = converter;
	}
	
	public File generatePDF(File docFile, File destinatioDir) throws IOException {
		
		File pdfFile = createBlankPdfFile(docFile, destinatioDir);
		
		Future<Boolean> conversion = converter
				.convert(docFile).as(DocumentType.MS_WORD)
				.to(pdfFile).as(DocumentType.PDF)
				.prioritizeWith(1000)
				.schedule();
		
		return pdfFile;
	}

	private File createBlankPdfFile(File docFile, File destinatioDir) throws IOException {
		String fileName = FilenameUtils.getBaseName(docFile.getName());
		File blankPdfFile = new File(destinatioDir.getAbsolutePath() + FILE_SEPARATOR + fileName + "." + SupportedGeneratedFileExtensions.PDF.getStringValue());
		if(!blankPdfFile.createNewFile()) {
			LOG.fatal("Throw IOException this message: Could not create file.");
			throw new IOException("Could not create file.");
		}
		
		return blankPdfFile;
	}
}