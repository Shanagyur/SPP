package hu.uni.miskolc.iit.spp.latex;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;

public class ScientificPaperLatexBuilderTest {

	private ScientificPaperLatexBuilder testElement = createTestElement();
	
	// try to extract a rar file
	@Test(expected = NotSupportedFileExtensionException.class)
	public void testCheckFileExtension_UnzipMethodWithRarFile() throws NotSupportedFileExtensionException {
		
		File testRar = new File("src\\resources\\dummyTxtInRar.rar");
		testElement.checkFileExtension(testRar);
	}
	
	// create targetDir with version_0 subdir
	@Test
	public void testCheckFileExtension_UnzipMethodSetTargetDirMethod() throws NotSupportedFileExtensionException {
		File testZip = new File("src\\resources\\dummyTexInZip.zip");
		testElement.checkFileExtension(testZip);
			
		File targetDir = new File("src\\resources\\targetDir");
		File version_0Dir = new File("src\\resources\\targetDir\\version_0");
			
		assertTrue(targetDir.exists() && version_0Dir.exists());
	}
	
	// create targetDir with version_0 & version_1 subdir
	@Test
	public void testCheckFileExtension_UnzipMethodSetTargetDirMethodMoreTimes() throws NotSupportedFileExtensionException {
		File testZip = new File("src\\resources\\dummyTexInZip.zip");
		for(int i = 0; i < 2; i++) {
			testElement.checkFileExtension(testZip);
		}
			
		File targetDir = new File("src\\resources\\targetDir");
		File version_1Dir = new File("src\\resources\\targetDir\\version_1");
			
		assertTrue(targetDir.exists() && version_1Dir.exists());
	}
	
	// can extract but don't exist .tex file
	@Test(expected = NotSupportedFileExtensionException.class)
	public void testCheckFileExtension_checkUnzipFilesExtensionMethod() throws NotSupportedFileExtensionException {
		File testZip = new File("src\\resources\\dummyTxtInZip.zip");
		testElement.checkFileExtension(testZip);
	}

	@Ignore("Not yet implemented")
	@Test
	public void testExtractTitle() {
	}

	@Ignore("Not yet implemented")
	@Test
	public void testExtractAbstarct() {
	}

	@Ignore("Not yet implemented")
	@Test
	public void testExtractKeywords() {
	}

	@Ignore("Not yet implemented")
	@Test
	public void testExtractAuthors() {
	}

	@Ignore("Not yet implemented")
	@Test
	public void testGeneratePDF() {
	}

	@Ignore("Not yet implemented")
	@Test
	public void testScientificPaperLatexBuilder() {
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
	
	private ScientificPaperLatexBuilder createTestElement() {
		return new ScientificPaperLatexBuilder();
	}
}
