package hu.uni.miskolc.iit.spp.latex.compile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class Latex2PDFCompilerTest {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String RESOURCES_PATH = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "compilePackages" + FILE_SEPARATOR;
	private static File startFile;
	private LatexCompilerFactory factory;
	private Latex2PDFCompiler compiler;
	
	@Test
	public void testLatex2PDFCompiler_LinuxInit() throws NotSupportedOperationSystemException {
		factory = new LatexCompilerFactory(SupportedOperationSystems.LINUX.getStringValue());
		compiler = factory.createLatexPDFCompiler();
		boolean condition_1 = compiler.compilerArg.equals(LinuxArgs.COMPILER.getArgument());
		boolean condition_2 = compiler.outputDirArg.equals(LinuxArgs.OUTPUT.getArgument());
		assertTrue(condition_1 && condition_2);
	}
	
	@Test
	public void testLatex2PDFCompiler_WindowsInit() throws NotSupportedOperationSystemException {
		factory = new LatexCompilerFactory(SupportedOperationSystems.WINDOWS.getStringValue());
		compiler = factory.createLatexPDFCompiler();
		boolean condition_1 = compiler.compilerArg.equals(WindowsArgs.COMPILER.getArgument());
		boolean condition_2 = compiler.outputDirArg.equals(WindowsArgs.OUTPUT.getArgument());
		assertTrue(condition_1 && condition_2);
	}

	@Test
	public void testGeneratePDFFile_DirectoryGenerated() throws SearchedFileNotExistsException, IOException, ConversionToPDFException {
		Latex2PDFCompiler mock = EasyMock.createMockBuilder(Latex2PDFCompiler.class).addMockedMethod("compile").createMock();
		File sourceDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0");
		File destinationDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");
		File pdf = new File("file.pdf");
		EasyMock.expect(mock.compile(sourceDir, destinationDir)).andReturn(pdf);
		mock.generatePDFFile(startFile);
		boolean condition_1 = destinationDir.getParentFile().exists();
		boolean condition_2 = destinationDir.exists();
		assertTrue(condition_1 && condition_2);
	}
	
	@Test
	public void testGeneratePDFFile_MoreDirectoryGenerated() throws SearchedFileNotExistsException, IOException, ConversionToPDFException {
		File sourceDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0");
		File destinationDir_0 = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");
		File destinationDir_1 = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_1");
		File pdf = new File("file.pdf");
		
		Latex2PDFCompiler mock1 = EasyMock.createMockBuilder(Latex2PDFCompiler.class).addMockedMethod("compile").createMock();
		Latex2PDFCompiler mock2 = EasyMock.createMockBuilder(Latex2PDFCompiler.class).addMockedMethod("compile").createMock();
		
		EasyMock.expect(mock1.compile(sourceDir, destinationDir_0)).andReturn(pdf);
		EasyMock.expect(mock2.compile(sourceDir, destinationDir_1)).andReturn(pdf);
		
		mock1.generatePDFFile(startFile);
		mock2.generatePDFFile(startFile);
		
		boolean condition_1 = destinationDir_0.getParentFile().exists();
		boolean condition_2 = destinationDir_0.exists();
		boolean condition_3 = destinationDir_1.exists();
		assertTrue(condition_1 && condition_2 && condition_3);
	}
	
	@Test(expected = ConversionToPDFException.class)
	public void testGeneratePDFFile_CatchIOException() throws SearchedFileNotExistsException, IOException, ConversionToPDFException {
		Latex2PDFCompiler mock = EasyMock.createMockBuilder(Latex2PDFCompiler.class).addMockedMethod("compile").createMock();
		File sourceDir = new File(RESOURCES_PATH + "dir4IOException");
		File destinationDir = new File(RESOURCES_PATH + "dir4IOException" + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");
		File pdf = new File("file.pdf");
		EasyMock.expect(mock.compile(sourceDir, destinationDir)).andReturn(pdf);
		File file4Exception = new File(sourceDir.getAbsolutePath() + FILE_SEPARATOR + "file4Exception.txt");
		mock.generatePDFFile(file4Exception);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		startFile = new File(RESOURCES_PATH + "startFile.txt");
	}

	@After
	public void tearDown() throws Exception {
		File generatedDir_0 = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
		File generatedDir_1 = new File(RESOURCES_PATH + "dir4IOException" + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
		if(generatedDir_0.exists()) {
			removeDirectory(generatedDir_0);
		}
		if(generatedDir_1.exists()) {
			removeDirectory(generatedDir_1);
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
}