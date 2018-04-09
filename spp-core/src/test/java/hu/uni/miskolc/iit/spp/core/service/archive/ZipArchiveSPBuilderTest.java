package hu.uni.miskolc.iit.spp.core.service.archive;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.UseableBuilderNotExistingException;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;

public class ZipArchiveSPBuilderTest extends ArchiveScientificPaperBuilderTest {

	private ScientificPaperBuilder mockBuilder;
	private ZipArchiveSPBuilder zipArchiveBuilder;
	
    @Before
    public void setUp() {
        mockBuilder = EasyMock.mock(ScientificPaperBuilder.class);
    	zipArchiveBuilder = new ZipArchiveSPBuilder(mockBuilder);
    }
    
    @After
    public void tearDown() throws Exception {
		File destDir = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
        if(destDir.exists()) {
            removeDirectory(destDir);
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
   
    @Test
    public void extractTest_InitDestDir() throws IOException {
    	zipArchiveBuilder.extract(zipFile);
    	
    	File expectedDir = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
    	File expectedSubdir = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0");
    	
    	assertTrue(expectedDir.exists() && expectedSubdir.exists());
    }
    
    @Test
    public void extractTest_InitMoreDestDir() throws IOException {
    	zipArchiveBuilder.extract(zipFile);
    	zipArchiveBuilder.extract(zipFile);
    	
    	File expectedDir = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue());
    	File expectedSubdir_0 = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0");
    	File expectedSubdir_1 = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_1");
    	
    	assertTrue(expectedDir.exists() && expectedSubdir_0.exists() && expectedSubdir_1.exists());
    }
    
    @Test
    public void extractTest() throws IOException {
    	zipArchiveBuilder.extract(zipFile);
    	
    	File expectedUnzipedFile = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0" + FILE_SEPARATOR + "dummyTXT.txt");
    	
    	assertTrue(expectedUnzipedFile.exists() && expectedUnzipedFile.length() >= 1);
    }
    
    @Test(expected = IOException.class)
    public void extractTest_ProtectedZipFile() throws IOException {
    	zipArchiveBuilder.extract(zipFileWithPassword);
    }
    
    @Test
    public void buildTest_StringWithZip() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
    	zipArchiveBuilder.build(zipFile.getAbsolutePath());
    	
    	File expectedUnzipedFile = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0" + FILE_SEPARATOR + "dummyTXT.txt");
    	
    	assertTrue(expectedUnzipedFile.exists() && expectedUnzipedFile.length() >= 1);
    }
 
    @Test(expected = IOException.class)
    public void buildTest_StringWithRar() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
    	zipArchiveBuilder.build(notZipFile.getAbsolutePath());
    }

    @Test
    public void buildTest_FileWithZip() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
    	zipArchiveBuilder.build(zipFile);
    	
    	File expectedUnzipedFile = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue() + FILE_SEPARATOR + "version_0" + FILE_SEPARATOR + "dummyTXT.txt");
    	
    	assertTrue(expectedUnzipedFile.exists() && expectedUnzipedFile.length() >= 1);
    }
    
    @Test(expected = IOException.class)
    public void buildTest_FileWithRar() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
    	zipArchiveBuilder.build(notZipFile);
    }
}