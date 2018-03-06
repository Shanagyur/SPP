package hu.uni.miskolc.iit.spp.docx.convert;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class Docx2PDFConverterIT {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File testFolder;
	private static File docxFile;
	
	private Docx2PDFConverter converter;
	private DocxConverterFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testFolder = new File(SOURCE_DIR + FILE_SEPARATOR + "directory");
		testFolder.mkdir();
		docxFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.docx");
	}
	
	@Before
	public void setUp() throws FileNotFoundException, NotSupportedFileExtensionException {
		try {
			factory = new DocxConverterFactory(System.getProperty("os.name"));
			converter = factory.createDocxPdfConverter(docxFile);
			
		} catch(NotSupportedOperationSystemException e) {
			assumeNoException("Ignore tests, because can't run this operation system.", e);
		}
	}

	@After
	public void tearDown() throws Exception {
		if(testFolder.exists()) {
			removeDirectory(testFolder);
		}
	}
	
	private void removeDirectory(File dir) {
		 if(dir.isDirectory()) {
			 File[] files = dir.listFiles();
			 if(files != null && files.length > 0) {
				 for(File aFile : files) {
					 removeDirectory(aFile);
				 }
			 }
			 dir.delete();
		 } else {
			 dir.delete();
		 }
	 }

	@Test
	public void generatePDF() throws IOException, SearchedFileNotExistsException {
		File pdf = converter.generatePDF(testFolder);
		
		assertTrue(pdf.exists() && pdf.length() > 0);
	}
}