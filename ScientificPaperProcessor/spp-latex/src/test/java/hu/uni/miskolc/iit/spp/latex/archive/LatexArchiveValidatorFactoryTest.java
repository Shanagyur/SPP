package hu.uni.miskolc.iit.spp.latex.archive;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;

public class LatexArchiveValidatorFactoryTest {

	private static File zipFile;
	private static File rarFile;
	private LatexArchiveValidatorFactory testFactory;
	
	@Test
	public void testCreateArchiveValidator_ZipFile() throws NotSupportedArchiveExtensionException {
		testFactory = new LatexArchiveValidatorFactory(zipFile);
		LatexArchiveValidator condition = testFactory.createArchiveValidator();
		assertTrue(condition.getClass().equals(LatexZipArchiveValidator.class));
	}
	
	@Test(expected = NotSupportedArchiveExtensionException.class)
	public void testCreateArchiveValidator_RarFile() throws NotSupportedArchiveExtensionException {
		testFactory = new LatexArchiveValidatorFactory(rarFile);
		testFactory.createArchiveValidator();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		zipFile = new File("file.zip");
		rarFile = new File("file.rar");
	}
}