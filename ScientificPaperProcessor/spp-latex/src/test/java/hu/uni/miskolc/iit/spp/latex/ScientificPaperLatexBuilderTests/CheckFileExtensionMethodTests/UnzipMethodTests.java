package hu.uni.miskolc.iit.spp.latex.ScientificPaperLatexBuilderTests.CheckFileExtensionMethodTests;



import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.core.service.CompositeScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.latex.ScientificPaperLatexBuilder;


public class UnzipMethodTests {
	
	private ScientificPaperBuilder testElement = createTestElement();
	
	//checkArchiveExtension method test
	@Test(expected = NotSupportedFileExtensionException.class)
	public void checkArchiveExtensionTest() throws NotSupportedFileExtensionException, ConversionToPDFException {

		String testRarPath = "\\resources\\dummyTexInRar.rar";
		testElement.build(testRarPath);
	}
	
	//setTargetDir method tests
	@Test
	@Ignore("cant't use yet")
	public void createTargetDirTest() throws NotSupportedFileExtensionException, ConversionToPDFException {
		
		String testZipPath = "\\resources\\dummyTexInZip.zip";
		testElement.build(testZipPath);
		
		File testTargetDir = new File("\\resources\\targetDir");
		File testTargetDirSubDir = new File(testTargetDir.getPath() + "\\version_0");
		
		Assert.assertTrue("create targetDir and version_0 subdir", 
				(testTargetDir.exists() && testTargetDirSubDir.exists()));
	}
	
//-----------------------------------------------------------------------------------------------	
	
	private ScientificPaperBuilder createTestElement() {
		Collection<AbstractScientificPaperBuilder> testCollection;
		testCollection = new HashSet<>();
		
		ScientificPaperLatexBuilder dummyLatexBuilder  = new ScientificPaperLatexBuilder();
		testCollection.add(dummyLatexBuilder);
		
		ScientificPaperBuilder test = new CompositeScientificPaperBuilder(testCollection);
		
		return test;
	}
}
