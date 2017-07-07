package hu.uni.miskolc.iit.spp.latex.operations;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public class FileOperationsTest {

	private static final String FILE_SEPARATOR = "file.separator";
	private static final String RESOURCES_DIR_PATH = 	"src" + System.getProperty(FILE_SEPARATOR) + 
														"resources" + System.getProperty(FILE_SEPARATOR) + 
														"fileOperationsTestDirectory" + System.getProperty(FILE_SEPARATOR);
	
	private static File expectedTex;
	private static File expectedPDF;
	private static File expectedTexInSubdir;
	private static File expectedPDFInSubdir;
	private static File sourceDirCorrectFiles;
	private static File sourceDirIncorrectFiles;
	private static File sourceDirCorrectFilesInSubdir;
	private static File sourceDirInorrectFilesInSubdir;
	
	@Test
	public void testFindPDFFile() throws SearchedFileNotExistsException {
		File actual = FileOperations.findPDFFile(sourceDirCorrectFiles);
		assertEquals(expectedPDF.getAbsolutePath(), actual.getAbsolutePath());
	}
	
	@Test
	public void testFindPDFFile_InSubdir() throws SearchedFileNotExistsException {
		File actual = FileOperations.findPDFFile(sourceDirCorrectFilesInSubdir);
		assertEquals(expectedPDFInSubdir.getAbsolutePath(), actual.getAbsolutePath());
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_WrongName() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_InSubdir_WrongName() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirInorrectFilesInSubdir);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_WrongExtension() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_InSubdir_WrongExtension() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirInorrectFilesInSubdir);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_WrongNameAndExtension() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindPDFFile_InSubdir_WrongNameAndExtension() throws SearchedFileNotExistsException {
		FileOperations.findPDFFile(sourceDirInorrectFilesInSubdir);
	}
	
	@Test
	public void testFindTexFile() throws SearchedFileNotExistsException {
		File actual = FileOperations.findTexFile(sourceDirCorrectFiles);
		assertEquals(expectedTex.getAbsolutePath(), actual.getAbsolutePath());
	}
	
	@Test
	public void testFindTexFile_InSubdir() throws SearchedFileNotExistsException {
		File actual = FileOperations.findTexFile(sourceDirCorrectFilesInSubdir);
		assertEquals(expectedTexInSubdir.getAbsolutePath(), actual.getAbsolutePath());
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_WrongName() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_InSubdir_WrongName() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirInorrectFilesInSubdir);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_WrongExtension() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_InSubdir_WrongExtension() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirInorrectFilesInSubdir);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_WrongNameAndExtension() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirIncorrectFiles);
	}
	
	@Test(expected = SearchedFileNotExistsException.class)
	public void testFindTexFile_InSubdir_WrongNameAndExtension() throws SearchedFileNotExistsException {
		FileOperations.findTexFile(sourceDirInorrectFilesInSubdir);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FileOperationsTest.sourceDirCorrectFiles = new File(RESOURCES_DIR_PATH + "sourceDirCorrectFiles" + System.getProperty(FILE_SEPARATOR));
		FileOperationsTest.sourceDirIncorrectFiles = new File(RESOURCES_DIR_PATH + "sourceDirIncorrectFiles" + System.getProperty(FILE_SEPARATOR));
		FileOperationsTest.sourceDirCorrectFilesInSubdir = new File(RESOURCES_DIR_PATH + "sourceDirCorrectFilesInSubdir");
		FileOperationsTest.sourceDirInorrectFilesInSubdir = new File(RESOURCES_DIR_PATH + "sourceDirInorrectFilesInSubdir");
		FileOperationsTest.expectedTexInSubdir = new File(sourceDirCorrectFilesInSubdir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "directory" + System.getProperty(FILE_SEPARATOR) + "main.tex");
		FileOperationsTest.expectedPDFInSubdir = new File(sourceDirCorrectFilesInSubdir.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "directory" + System.getProperty(FILE_SEPARATOR) + "main.pdf");
		FileOperationsTest.expectedTex = new File(sourceDirCorrectFiles.getAbsolutePath()+ System.getProperty(FILE_SEPARATOR) + "main.tex");
		FileOperationsTest.expectedPDF = new File(sourceDirCorrectFiles.getAbsolutePath() + System.getProperty(FILE_SEPARATOR) + "main.pdf");
	}
}