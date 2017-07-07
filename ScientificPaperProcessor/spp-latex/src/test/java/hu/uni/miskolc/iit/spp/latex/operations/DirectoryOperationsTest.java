package hu.uni.miskolc.iit.spp.latex.operations;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;

public class DirectoryOperationsTest {

	private static final String FILE_SEPARATOR = "file.separator";
	private static final String RESOURCES_DIR_PATH = 	"src" + System.getProperty(FILE_SEPARATOR) + 
														"resources" + System.getProperty(FILE_SEPARATOR);
	private static File startFile;
	private static String targetDirName;
	private static String generatedDirName;

	@Test
	public void testCreateDestinationDir_OneDirectoryWithOneSubdir() throws IOException {
		DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.targetDirName);
		
		File targetDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.targetDirName);
		File version_0_Subdir = new File(targetDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_0");
		
		assertTrue(targetDir.exists() && version_0_Subdir.exists());
	}
	
	@Test
	public void testCreateDestinationDir_OneDirectoryWithMoreSubdirs() throws IOException {
		for(int i = 0; i < 2; i++) {
			DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.targetDirName);
		}
		File targetDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.targetDirName);
		File version_1_Subdir = new File(targetDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_1");
		
		assertTrue(targetDir.exists() && version_1_Subdir.exists());
	}
	
	@Test
	public void testCreateDestinationDir_MoreDirectoriesWithOneSubdir() throws IOException {
		DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.targetDirName);
		DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.generatedDirName);
		
		File targetDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.targetDirName);
		File targetDirSubdir = new File(targetDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_0");
		File generatedDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.generatedDirName);
		File generatedDirSubdir = new File(generatedDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_0");
		
		assertTrue(	targetDir.exists() && targetDirSubdir.exists() && 
					generatedDir.exists() && generatedDirSubdir.exists());
	}
	
	@Test
	public void testCreateDestinationDir_MoreDirectoriesWithMoreSubdirs() throws IOException {
		for(int i = 0; i < 2; i++) {
			DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.targetDirName);
			DirectoryOperations.createDestinationDir(DirectoryOperationsTest.startFile, DirectoryOperationsTest.generatedDirName);
		}
		File targetDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.targetDirName);
		File targetDirSecondSubdir = new File(targetDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_1");
		File generatedDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.generatedDirName);
		File generatedDirSecondSubdir = new File(generatedDir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "version_1");
		
		assertTrue(	targetDir.exists() && targetDirSecondSubdir.exists() && 
					generatedDir.exists() && generatedDirSecondSubdir.exists());
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DirectoryOperationsTest.startFile = new File(RESOURCES_DIR_PATH + "start.txt");
		DirectoryOperationsTest.targetDirName = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
		DirectoryOperationsTest.generatedDirName = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	}

	@After
	public void tearDown() throws Exception {
		File targetDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.targetDirName);
		if (targetDir.exists()) {
			removeDirectory(targetDir);
		}
		File generatedDir = new File(RESOURCES_DIR_PATH + DirectoryOperationsTest.generatedDirName);
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