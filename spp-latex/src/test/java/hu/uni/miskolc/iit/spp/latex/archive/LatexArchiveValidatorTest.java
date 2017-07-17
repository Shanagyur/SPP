package hu.uni.miskolc.iit.spp.latex.archive;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;

public class LatexArchiveValidatorTest {
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String RESOURCES_PATH = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "archivePackages" + FILE_SEPARATOR;
	private static File existingZipFileWithTex; 
	private static File existingZipFileWithoutTex; 
	private LatexArchiveValidatorFactory factory;
	private LatexArchiveValidator validator;
	
	
	
	@Test(expected = IOException.class)
	public void testValidate_EqualsChecking() throws NotSupportedArchiveExtensionException, IOException {
		factory = new LatexArchiveValidatorFactory(existingZipFileWithTex);
		validator = factory.createArchiveValidator();
		validator.validate(existingZipFileWithoutTex);
	}

	@Test
	public void testValidate_DirectoryGenerated() throws NotSupportedArchiveExtensionException, IOException {
		factory = new LatexArchiveValidatorFactory(existingZipFileWithTex);
		validator = factory.createArchiveValidator();
		validator.validate(existingZipFileWithTex);
		File targetDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
		File subdir = new File(targetDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
		assertTrue(targetDir.exists() && subdir.exists());
	}
	
	@Test
	public void testValidate_MoreDirectoryGenerated() throws NotSupportedArchiveExtensionException, IOException {
		factory = new LatexArchiveValidatorFactory(existingZipFileWithTex);
		validator = factory.createArchiveValidator();
		for(int i = 0; i < 2; i++) {
			validator.validate(existingZipFileWithTex);
		}
		File targetDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
		File subdir_0 = new File(targetDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
		File subdir_1 = new File(targetDir.getAbsolutePath() + FILE_SEPARATOR + "version_1");
		assertTrue(targetDir.exists() && subdir_0.exists() && subdir_1.exists());
	}
	
	@Test
	public void testValidate_UnpackWithoutTexFile() throws NotSupportedArchiveExtensionException, IOException {
		factory = new LatexArchiveValidatorFactory(existingZipFileWithoutTex);
		validator = factory.createArchiveValidator();
		assertFalse(validator.validate(existingZipFileWithoutTex));
	}
	
	@Test
	public void testValidate_UnpackWithTexFile() throws NotSupportedArchiveExtensionException, IOException {
		factory = new LatexArchiveValidatorFactory(existingZipFileWithTex);
		validator = factory.createArchiveValidator();
		assertTrue(validator.validate(existingZipFileWithTex));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		existingZipFileWithTex = new File(RESOURCES_PATH + "texInZip.zip");
		existingZipFileWithoutTex = new File(RESOURCES_PATH + "txtInZip.zip");
	}

	@After
	public void tearDown() throws Exception {
		File targetDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
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