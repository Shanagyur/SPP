package hu.uni.miskolc.iit.spp.latex;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.latex.archive.LatexArchiveValidatorFactory;
import hu.uni.miskolc.iit.spp.latex.compile.LatexCompilerFactory;

public class ScientificPaperLatexBuilderTest {
	
	private static final String ASSUME_EXCEPTION_CONSTANS = "pdflatex -version";
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String RESOURCES_PATH = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "latexPackages" + FILE_SEPARATOR;
	private static File mainTexZip;
	private static File wrongTexZip;
	private static File pdfFile;
	private LatexCompilerFactory compilerFact;
	private ScientificPaperLatexBuilder latexBuilder;
	
	@Test
	public void testCheckFileExtension() throws NotSupportedArchiveExtensionException, NotSupportedOperationSystemException, NoMainDocumentFoundException, IOException {
		assumeTrue("Ignore test, because can't run this operation system.", 
				(OS_NAME.contains(SupportedOperationSystems.LINUX.getStringValue())) 
				|| 
				(OS_NAME.contains(SupportedOperationSystems.WINDOWS.getStringValue()))
				);
		LatexArchiveValidatorFactory validatorFact = new LatexArchiveValidatorFactory(mainTexZip);
		latexBuilder = new ScientificPaperLatexBuilder(compilerFact.createLatexPDFCompiler(), validatorFact.createArchiveValidator());
		latexBuilder.checkFileExtension(mainTexZip);
	}
	
	@Test(expected = NoMainDocumentFoundException.class)
	public void testCheckFileExtension_ThrowNoMainDocumentFoundException() throws NotSupportedArchiveExtensionException, NotSupportedOperationSystemException, NoMainDocumentFoundException, IOException {
		assumeTrue("Ignore test, because can't run this operation system.", 
				(OS_NAME.contains(SupportedOperationSystems.LINUX.getStringValue())) 
				|| 
				(OS_NAME.contains(SupportedOperationSystems.WINDOWS.getStringValue()))
				);
		LatexArchiveValidatorFactory validatorFact = new LatexArchiveValidatorFactory(wrongTexZip);
		latexBuilder = new ScientificPaperLatexBuilder(compilerFact.createLatexPDFCompiler(), validatorFact.createArchiveValidator());
		latexBuilder.checkFileExtension(wrongTexZip);
	}

	@Test
	public void testGeneratePDF() throws NotSupportedArchiveExtensionException, NotSupportedOperationSystemException, ConversionToPDFException, IOException, NoMainDocumentFoundException {
		assumeTrue("Ignore test, because can't run this operation system.", 
				(OS_NAME.contains(SupportedOperationSystems.LINUX.getStringValue())) 
				|| 
				(OS_NAME.contains(SupportedOperationSystems.WINDOWS.getStringValue()))
				);
		try {
			Runtime.getRuntime().exec(ASSUME_EXCEPTION_CONSTANS);
		} catch (Exception e) {
			assumeNoException("Ignore test, because can't compile .tex files.", e);
		}
		LatexArchiveValidatorFactory validatorFact = new LatexArchiveValidatorFactory(mainTexZip);
		latexBuilder = new ScientificPaperLatexBuilder(compilerFact.createLatexPDFCompiler(), validatorFact.createArchiveValidator());
		latexBuilder.checkFileExtension(mainTexZip);
		latexBuilder.generatePDF(mainTexZip);
		assertTrue(pdfFile.exists());
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExtractTitle() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExtractAbstarct() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExtractKeywords() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore("Not yet implemented")
	public void testExtractAuthors() {
		fail("Not yet implemented");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mainTexZip = new File(RESOURCES_PATH + "mainTex.zip");
		wrongTexZip = new File(RESOURCES_PATH + "wrongTex.zip");
		pdfFile = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0" + FILE_SEPARATOR + "main.pdf");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		compilerFact = new LatexCompilerFactory(OS_NAME);
	}

	@After
	public void tearDown() throws Exception {
		File targetDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
		if (targetDir.exists()) {
			removeDirectory(targetDir);
		}
		File generatedDir = new File(RESOURCES_PATH + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
		if (generatedDir.exists()) {
			removeDirectory(generatedDir);
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