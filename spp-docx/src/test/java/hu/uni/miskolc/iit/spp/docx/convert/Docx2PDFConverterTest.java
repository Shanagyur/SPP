package hu.uni.miskolc.iit.spp.docx.convert;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class Docx2PDFConverterTest {
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File testFolder;
	private static File docxFile;
	
	private Docx2PDFConverter mockConverter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testFolder = new File(SOURCE_DIR + FILE_SEPARATOR + "directory");
		testFolder.mkdir();
		docxFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.docx");
	}

	@Before
	public void setUp() throws Exception {
		DocxConverterFactory mockFactory = EasyMock.mock(DocxConverterFactory.class);
		EasyMock.expect(mockFactory.createDocxPdfConverter(docxFile)).andReturn(new Docx2PDFConverter("goodFile", new FileInputStream(docxFile)));
		EasyMock.replay(mockFactory);
		mockConverter = mockFactory.createDocxPdfConverter(docxFile);
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
		File pdf = mockConverter.generatePDF(testFolder);
		
		assertTrue(pdf.exists() && pdf.length() > 0);
	}
}