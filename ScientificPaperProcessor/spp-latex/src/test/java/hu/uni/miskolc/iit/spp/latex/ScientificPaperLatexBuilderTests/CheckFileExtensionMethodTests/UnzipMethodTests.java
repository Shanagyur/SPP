package hu.uni.miskolc.iit.spp.latex.ScientificPaperLatexBuilderTests.CheckFileExtensionMethodTests;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.junit.After;
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

		String testRarPath = "src\\resources\\dummyTexInRar.rar";
		testElement.build(testRarPath);
	}
	
	//setTargetDir method tests
	@Test
	public void createTargetDirTest() throws NotSupportedFileExtensionException, ConversionToPDFException {
		
		String testZipPath = "src\\resources\\dummyTexInZip.zip";
		testElement.build(testZipPath);
		
		File testTargetDir = new File("src\\resources\\targetDir");
		File testTargetDirSubDir = new File(testTargetDir.getPath() + "\\version_0");
		
		Assert.assertTrue("create targetDir and version_0 subdir", 
				(testTargetDir.exists() && testTargetDirSubDir.exists()));
	}

	@After
	public void clear() {
		File targetDir = new File("src\\resources\\targetDir");
		if(targetDir.exists()) {
			System.out.println("targetDir existed & deleted");
			removeDirectory(targetDir);
		}
		File generatedDir = new File("src\\resources\\generatedDir");
		if(generatedDir.exists()) {
			System.out.println("generatedDir existed & deleted");
			removeDirectory(generatedDir);
		}
	}

	private void removeDirectory(File dir) {
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			if(files != null && files.length > 0) {
				for (File aFile : files) {
					removeDirectory(aFile);
				}
	        }
	        dir.delete();
	    } else {
	        dir.delete();
	    }
	}
	
	private ScientificPaperBuilder createTestElement() {
		Collection<AbstractScientificPaperBuilder> testCollection;
		testCollection = new HashSet<>();
		
		ScientificPaperLatexBuilder dummyLatexBuilder  = new ScientificPaperLatexBuilder();
		testCollection.add(dummyLatexBuilder);
		
		ScientificPaperBuilder test = new CompositeScientificPaperBuilder(testCollection);
		
		return test;
	}
}
