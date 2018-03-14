package hu.uni.miskolc.iit.ssp.doc.convert;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedApplicationException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class Doc2PDFConverterIT {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File destDir;
	private static File goodFile;
	
	private Doc2PDFConverter converter;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		destDir = new File(SOURCE_DIR + FILE_SEPARATOR + "destDir");
		destDir.mkdir();
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.doc");
	}

	@Before
	public void setUp() throws Exception {
		try {
			DocConverterFactory factory = new DocConverterFactory(System.getProperty("os.name"));
			converter = factory.createDocPDFConverter(goodFile);
			
		} catch(NotSupportedOperationSystemException | NotSupportedApplicationException e) {
			assumeNoException("Ignore test, because can't run this machine.", e);
		}
	}

	@After
	public void tearDown() throws Exception {
		if(destDir.exists()) {
			removeDirectory(destDir);
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
	public void testGeneratePDF() throws IOException {
		File pdfFile = converter.generatePDF(goodFile, destDir);
		
		assertTrue(pdfFile.exists() && pdfFile.length() > 0);
	}
}