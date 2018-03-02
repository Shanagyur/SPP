package hu.uni.miskolc.iit.spp.docx;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.docx.convert.Docx2PDFConverter;

public class ScientificPaperDocxBuilderTest {
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	
	private static File goodFile;
	private static File wrongFile;
	private static File goodFileWithEmptyItems;
	private static File goodFileWithSingleAuthor;
	
	private ScientificPaperDocxBuilder mockBuilder;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.docx");
		wrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "wrongFile.txt");
		goodFileWithEmptyItems = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFileWithEmptyItems.docx");
		goodFileWithSingleAuthor = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFileWithSingleAuthor.docx");
	}
	
	@Before
	public void setUp() throws Exception {
		mockBuilder = new ScientificPaperDocxBuilder(EasyMock.mock(Docx2PDFConverter.class));
	}

	@After
	public void tearDown() throws Exception {
		File destDir = new File(SOURCE_DIR + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
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
	
	private void initDocxParagraphs(File file) throws IOException {
		mockBuilder.extractTitle(file);
	}

	@Test
	public void checkFileExtension() throws NoMainDocumentFoundException {
		mockBuilder.checkFileExtension(goodFile);
	}
	
	@Test(expected = NoMainDocumentFoundException.class)
	public void checkFileExtension_ThrowsException() throws NoMainDocumentFoundException {
		mockBuilder.checkFileExtension(wrongFile);
	}

	@Test
	public void generatePDF_InitDestinationDir() throws IOException, SearchedFileNotExistsException, ConversionToPDFException {
		File destDir = new File(new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "0").getAbsolutePath());
		Docx2PDFConverter mockConverter = EasyMock.mock(Docx2PDFConverter.class);
		
		EasyMock.expect(mockConverter.generatePDF(destDir)).andReturn(new File("main.pdf"));
		EasyMock.replay(mockConverter);
		
		mockBuilder = new ScientificPaperDocxBuilder(mockConverter);
		mockBuilder.generatePDF(goodFile);
		
		File expectedDir = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME);
		File expectedSubdir = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + "0");
		boolean condition_1 = expectedDir.exists();
		boolean condition_2 = expectedSubdir.exists();
		
		assertTrue(condition_1 && condition_2);
	}
	
	@Test
	public void generatePDF_InitMoreDestinationDir() throws IOException, SearchedFileNotExistsException, ConversionToPDFException {
		File destDir_0 = new File(new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "0").getAbsolutePath());
		File destDir_1 = new File(new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "1").getAbsolutePath());
		Docx2PDFConverter mockConverter_0 = EasyMock.mock(Docx2PDFConverter.class);
		Docx2PDFConverter mockConverter_1 = EasyMock.mock(Docx2PDFConverter.class);
		
		EasyMock.expect(mockConverter_0.generatePDF(destDir_0)).andReturn(new File("main.pdf"));
		EasyMock.replay(mockConverter_0);
		EasyMock.expect(mockConverter_1.generatePDF(destDir_1)).andReturn(new File("main.pdf"));
		EasyMock.replay(mockConverter_1);
		
		mockBuilder = new ScientificPaperDocxBuilder(mockConverter_0);
		mockBuilder.generatePDF(goodFile);
		mockBuilder = new ScientificPaperDocxBuilder(mockConverter_1);
		mockBuilder.generatePDF(goodFile);
		
		File expectedDir = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME);
		File expectedSubdir_0 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + "0");
		File expectedSubdir_1 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + "1");
		boolean condition_1 = expectedDir.exists();
		boolean condition_2 = expectedSubdir_0.exists();
		boolean condition_3 = expectedSubdir_1.exists();
		
		assertTrue(condition_1 && condition_2 && condition_3);
	}

	@Test
	public void extractTitle() throws IOException {
		String actual = mockBuilder.extractTitle(goodFile);
		String expected = "Title";
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void extractTitle_emptyTitle() throws IOException {
		String title = mockBuilder.extractTitle(goodFileWithEmptyItems);
		
		assertTrue(title.isEmpty());
	}

	@Test
	public void extractAbstract() throws IOException {
		mockBuilder.extractTitle(goodFile);
		String actual = mockBuilder.extractAbstract(goodFile);
		String expected = "Abstract\n" + "Abstract text lorem ipsum.";

		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void extractAbstract_emptyAbstract() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		String actual = mockBuilder.extractAbstract(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testExtractKeywords() throws IOException {
		initDocxParagraphs(goodFile);
		List<String> actual = mockBuilder.extractKeywords(goodFile);
		
		boolean conditionSize = actual.size() == 3;
		boolean conditoin_1 = actual.get(0).contains("alma");
		boolean conditoin_2 = actual.get(1).contains("korte");
		boolean conditoin_3 = actual.get(2).contains("szilva");
		
		assertTrue(conditionSize && conditoin_1 && conditoin_2 && conditoin_3);
	}
	
	@Test
	public void testExtractKeywords_emptyKeywords() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		List<String> actual = mockBuilder.extractKeywords(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testExtractAuthors_EmptyAuthor() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		List<Author> actual = mockBuilder.extractAuthors(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testExtractAuthors_SingleAuthor() throws IOException {
		initDocxParagraphs(goodFileWithSingleAuthor);
		List<Author> actual = mockBuilder.extractAuthors(goodFileWithSingleAuthor);
		Author expected = new Author("Jean Smith", "alma@alma.hu", "University of Gondor, Middle-Earth");
		
		boolean conditionSize = actual.size() == 1;
		boolean condition = 
				actual.get(0).getName().contains(expected.getName()) && 
				actual.get(0).getEmail().contains(expected.getEmail()) &&
				actual.get(0).getAffiliation().contains(expected.getAffiliation());
		
		assertTrue(conditionSize && condition);
	}

	@Test
	public void testExtractAuthors_MoreAuthor() throws IOException {
		initDocxParagraphs(goodFile);
		List<Author> actual = mockBuilder.extractAuthors(goodFile);
		List<Author> expected = new ArrayList<Author>();
		expected.add(new Author("Jean Smith", "alma@alma.hu", "University of Gondor, Middle-Earth"));
		expected.add(new Author("Jane Smith", "korte@alma.hu", "University of Gondor, Middle-Earth"));
		expected.add(new Author("John Smith", "szilva@alma.hu", "University of Mordor, Middle-Earth"));
		
		boolean conditionSize = actual.size() == 3;
		boolean condition_1 = actual.contains(expected.get(0));
		boolean condition_2 = actual.contains(expected.get(1));
		boolean condition_3 = actual.contains(expected.get(2));
		
		assertTrue(conditionSize && condition_1 && condition_2 && condition_3);
	}
}