package hu.uni.miskolc.iit.ssp.doc.convert;

import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedApplicationException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class DocConverterFactoryTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File goodFile;
	private static File wrongFile;
	private static File tempDir;
	
	private DocConverterFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.doc");
		wrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "wrongFile.txt");
		tempDir = new File(SOURCE_DIR + FILE_SEPARATOR + "tempDir");
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		tempDir.deleteOnExit();
	}
	
	@Test
	public void createDocPDFConverter_Windows() throws NotSupportedOperationSystemException, NotSupportedFileExtensionException, IOException {
		try {
			factory = new DocConverterFactory("WINdows15_for_Students");
			factory.createDocPDFConverter(goodFile);
		
		} catch(NotSupportedApplicationException e) {
			assumeNoException("Ignore test, because can't run this machine.", e);
		}
	}
	
	@Test(expected = NotSupportedOperationSystemException.class)
	public void createDocPDFConverter_WrongOS() throws NotSupportedOperationSystemException, NotSupportedFileExtensionException, NotSupportedApplicationException, IOException {
		factory = new DocConverterFactory("LInUX_nextGen");
		factory.createDocPDFConverter(goodFile);
	}
	
	@Test(expected = NotSupportedFileExtensionException.class)
	public void createDocPDFConverter_WindowsWithWrongFile() throws NotSupportedOperationSystemException, NotSupportedFileExtensionException, NotSupportedApplicationException, IOException {
		factory = new DocConverterFactory("WINdows15_for_Students");
		factory.createDocPDFConverter(wrongFile);
	}
}