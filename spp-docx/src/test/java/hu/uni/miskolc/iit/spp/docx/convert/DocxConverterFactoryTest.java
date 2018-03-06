package hu.uni.miskolc.iit.spp.docx.convert;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class DocxConverterFactoryTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File goodFile;
	private static File wrongFile;
	
	private DocxConverterFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.docx");
		wrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "wrongFile.txt");
	}
	
	@Test(expected = NotSupportedFileExtensionException.class)
	public void testCreateDocxPdfConverter_WindowsWithWrongFile() throws FileNotFoundException, NotSupportedOperationSystemException, NotSupportedFileExtensionException {
		factory = new DocxConverterFactory("windows");
		factory.createDocxPdfConverter(wrongFile);
	}
	
	@Test(expected = NotSupportedFileExtensionException.class)
	public void testCreateDocxPdfConverter_LinuxWithWrongFile() throws FileNotFoundException, NotSupportedOperationSystemException, NotSupportedFileExtensionException {
		factory = new DocxConverterFactory("linux");
		factory.createDocxPdfConverter(wrongFile);
	}
	
	@Test(expected = NotSupportedOperationSystemException.class)
	public void testCreateDocxPdfConverter_WrongOS() throws FileNotFoundException, NotSupportedOperationSystemException, NotSupportedFileExtensionException {
		factory = new DocxConverterFactory("mac_os");
		factory.createDocxPdfConverter(goodFile);
	}
	
	@Test
	public void testCreateDocxPdfConverter_Linux() throws FileNotFoundException, NotSupportedOperationSystemException, NotSupportedFileExtensionException {
		factory = new DocxConverterFactory("LInUX_nextGen");
		factory.createDocxPdfConverter(goodFile);
	}
	
	@Test
	public void testCreateDocxPdfConverter_Windows() throws FileNotFoundException, NotSupportedOperationSystemException, NotSupportedFileExtensionException {
		factory = new DocxConverterFactory("WINdows15_for_Students");
		factory.createDocxPdfConverter(goodFile);
	}
}