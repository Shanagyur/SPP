package hu.uni.miskolc.iit.ssp.doc.convert;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;

public class Doc2PDFConverterTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	
	private static File destDir;
	private static File tempDir;
	private static File goodFile;
	
	private DocConverterFactory mockFactory;
	private Doc2PDFConverter mockConverter;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		destDir = new File(SOURCE_DIR + FILE_SEPARATOR + "destDir");
		tempDir = new File(SOURCE_DIR + FILE_SEPARATOR + "tempDir");
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.doc");
	}
	
	@Before
	public void setUp() throws Exception {
		assumeTrue("Ignore test, because can't run this machine.", isSupportedOS(System.getProperty("os.name")) && isCorrectMSOffice());
		
		destDir.mkdir();
		tempDir.mkdir();
		
		IConverter converter = LocalConverter
				.builder()
				.baseFolder(tempDir)
				.workerPool(20, 25, 2, TimeUnit.SECONDS)
				.processTimeout(15, TimeUnit.SECONDS)
				.build();
		
		mockFactory = EasyMock.mock(DocConverterFactory.class);
		EasyMock.expect(mockFactory.createDocPDFConverter(goodFile)).andReturn(new Doc2PDFConverter(converter, tempDir));
		EasyMock.replay(mockFactory);
		mockConverter = mockFactory.createDocPDFConverter(goodFile);
	}

	@After
	public void tearDownAfterClass() throws Exception {
		if(destDir.exists()) {
			removeDirectory(destDir);
		}
	}

	private boolean isSupportedOS(String property) {
		return property.toLowerCase().contains(SupportedOperationSystems.WINDOWS.getStringValue());
	}
	
	private boolean isCorrectMSOffice() throws IOException {
		Process process = Runtime.getRuntime().exec("reg query HKEY_CLASSES_ROOT" + FILE_SEPARATOR +"Word.Application" + FILE_SEPARATOR + "CurVer");
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
		String string;
		while((string = reader.readLine()) != null) {
			if(string.contains("Word.Application.")) {
				String[] appWithVersion = string.substring(string.indexOf("Word.Application.")).split("\\.");
				int versionNo = getVersionNumber(appWithVersion);
				
				return versionNo >= 12;
			}
		}
		
		return false;
	}

	private int getVersionNumber(String[] appWithVersion) {
		for(String string : appWithVersion) {
			if(!string.matches("[0-9]*")) {
				continue;
			}
			
			return Integer.parseInt(string);
		}
		
		return 0;
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
		File pdfFile = mockConverter.generatePDF(goodFile, destDir);
		
		assertTrue(pdfFile.exists() && pdfFile.length() > 0);
	}
}