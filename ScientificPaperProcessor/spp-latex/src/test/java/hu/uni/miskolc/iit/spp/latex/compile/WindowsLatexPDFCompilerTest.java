package hu.uni.miskolc.iit.spp.latex.compile;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class WindowsLatexPDFCompilerTest {

	private static final String ASSUME_EXCEPTION_CONSTANS = "pdflatex -version";
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String RESOURCES_PATH = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "compilePackages" + FILE_SEPARATOR;
	private static File dirWithWrongFiles;
	private static File dirWithGoodFiles;
	private WindowsLatexPDFCompiler compiler;
	
	@Test
	public void testCompile() throws SearchedFileNotExistsException, IOException {
		assumeTrue("Ignore test, because can't run this operation system.", OS_NAME.contains(SupportedOperationSystems.WINDOWS.getStringValue()));
		try {
			Runtime.getRuntime().exec(ASSUME_EXCEPTION_CONSTANS);
		} catch (Exception e) {
			assumeNoException("Ignore test, because can't compile .tex files.", e);
		}
		compiler.compile(dirWithGoodFiles, dirWithGoodFiles);
		File pdf = new File(dirWithGoodFiles.getAbsolutePath() + FILE_SEPARATOR + "main.pdf");
		assertTrue(pdf.exists());
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testCompile_CatchSearchedFileNotExistsException() throws SearchedFileNotExistsException, IOException {
		assumeTrue("Ignore test, because can't run this operation system.", OS_NAME.contains(SupportedOperationSystems.WINDOWS.getStringValue()));
		try {
			Runtime.getRuntime().exec(ASSUME_EXCEPTION_CONSTANS);
		} catch (Exception e) {
			assumeNoException("Ignore test, because can't compile .tex files.", e);
		}
		WindowsLatexPDFCompiler mock = EasyMock.createMockBuilder(WindowsLatexPDFCompiler.class).addMockedMethod("commandForTerminal").createMock();
		EasyMock.expect(mock.commandForTerminal(dirWithWrongFiles, dirWithGoodFiles)).andReturn("pdflatex " + "-include-directory=" + dirWithWrongFiles.getAbsolutePath() + " -output-directory=" + dirWithGoodFiles.getAbsolutePath() + " " + "wrong.tex");
		EasyMock.replay(mock);
		mock.compile(dirWithWrongFiles, dirWithGoodFiles);
	}

	@Test
	public void testCommandForTerminal() throws SearchedFileNotExistsException {
		File destinationDir = new File(RESOURCES_PATH + "desDir");
		String command = compiler.commandForTerminal(dirWithGoodFiles, destinationDir);
		File wantedFile = new File(dirWithGoodFiles.getAbsolutePath() + FILE_SEPARATOR + "main.tex");
		assertTrue(command.contains(wantedFile.getName()));
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testCommandForTerminal_WrongFileName() throws SearchedFileNotExistsException {
		File destinationDir = new File(RESOURCES_PATH + "desDir");
		compiler.commandForTerminal(dirWithWrongFiles, destinationDir);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testCommandForTerminal_WrongFileExtension() throws SearchedFileNotExistsException {
		File destinationDir = new File(RESOURCES_PATH + "desDir");
		compiler.commandForTerminal(dirWithWrongFiles, destinationDir);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dirWithWrongFiles = new File(RESOURCES_PATH + "dirWithWrongFiles");
		dirWithGoodFiles = new File(RESOURCES_PATH + "dirWithGoodFiles");
	}

	@Before
	public void setUp() throws Exception {
		compiler = new WindowsLatexPDFCompiler();
	}

	@After
	public void tearDown() throws Exception {
		File[] files = dirWithGoodFiles.listFiles();
		for(File file : files) {
			String extesion = FilenameUtils.getExtension(file.getName());
			if(extesion.equals(SupportedCompileableTextFileExtensions.TEX.getStringValue())) {
				continue;
			}
			file.delete();
		}
	}
}