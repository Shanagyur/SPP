package hu.uni.miskolc.iit.spp.latex.operations;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;


public class UnpackingTest {
	
	private static final String FILE_SEPARATOR = "file.separator";
	private static final String RESOURCES_DIR_PATH = "src" + System.getProperty(FILE_SEPARATOR) + 
													"resources" + System.getProperty(FILE_SEPARATOR) + 
													"unpackingTestDirectory" + System.getProperty(FILE_SEPARATOR);;
	
	private static File zipFile;
	private static File rarFile;
	
	@Test
	public void testUnzip() throws NotSupportedArchiveExtensionException, IOException {
		Unpacking.unzip(zipFile);
		File expected = new File(RESOURCES_DIR_PATH + System.getProperty(FILE_SEPARATOR) + "targetDir" + System.getProperty(FILE_SEPARATOR) + "version_0" + System.getProperty(FILE_SEPARATOR) + "dummyTXT.txt");
		assertTrue(expected.exists());
	}
	
	@Test(expected = NotSupportedArchiveExtensionException.class)
	public void testUnzip_WithRar() throws NotSupportedArchiveExtensionException, IOException {
		Unpacking.unzip(rarFile);
	}
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		UnpackingTest.zipFile = new File(RESOURCES_DIR_PATH + "zipFile.zip");
		UnpackingTest.rarFile = new File(RESOURCES_DIR_PATH + "rarFile.rar");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		File targetDir = new File(RESOURCES_DIR_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
		if (targetDir.exists()) {
			removeDirectory(targetDir);
		}
	}
	
	private void removeDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File aFile : files) {
					removeDirectory(aFile);
				}
			}
			dir.delete();
		} else {
			dir.delete();
		}
	}
}