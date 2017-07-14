package hu.uni.miskolc.iit.spp.latex.archive;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;

public class LatexZipArchiveValidatorTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String RESOURCES_PATH = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "archivePackages" + FILE_SEPARATOR;
	private static File zipForException;
	private LatexZipArchiveValidator validator;
	
	@Test(expected = IOException.class)
	public void testUnpack_CatchZipException() throws NotSupportedArchiveExtensionException, IOException {
		File directory = new File("");
		validator = new LatexZipArchiveValidator(zipForException);
		validator.unpack(zipForException, directory);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		zipForException = new File(RESOURCES_PATH + "zipForException.zip");
	}
}