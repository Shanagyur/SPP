package hu.uni.miskolc.iit.spp.docx;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.docx.convert.Docx2PDFConverter;
import hu.uni.miskolc.iit.spp.docx.convert.DocxConverterFactory;

public class ScientificPaperDocxBuilderIT {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	
	private static File goodFile;
	private static File wrongFile;
	private static File goodFileWithEmptyItems;
	private static File goodFileWithSingleAuthor;
	
	private DocxConverterFactory factory;
	private Docx2PDFConverter converter;
	private ScientificPaperDocxBuilder builder;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFile.docx");
		wrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "wrongFile.txt");
		goodFileWithEmptyItems = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFileWithEmptyItems.docx");
		goodFileWithSingleAuthor = new File(SOURCE_DIR + FILE_SEPARATOR + "goodFileWithSingleAuthor.docx");
	}

	@Before
	public void setUp() throws Exception {
		try {
			factory = new DocxConverterFactory(System.getProperty("os.name"));
			converter = factory.createDocxPdfConverter(goodFile);
			builder = new ScientificPaperDocxBuilder(converter);
			
		} catch(NotSupportedOperationSystemException e) {
			assumeNoException("Ignore test, because can't run this operation system.", e);
		}
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
		builder.extractTitle(file);
	}

	@Test
	public void checkFileExtension() throws NoMainDocumentFoundException {
		builder.checkFileExtension(goodFile);
	}
	
	@Test(expected = NoMainDocumentFoundException.class)
	public void checkFileExtension_ThrowsException() throws NoMainDocumentFoundException {
		builder.checkFileExtension(wrongFile);
	}

	@Test
	public void generatePDF_InitDestinationDir() throws IOException, SearchedFileNotExistsException, ConversionToPDFException {
		builder.generatePDF(goodFile);

		File destDir = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME);
		File subDir = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "0");
		
		assertTrue(destDir.exists() && subDir.exists());
	}
	
	@Test
	public void generatePDF_InitMoreDestinationDir() throws Exception {
		builder.generatePDF(goodFile);
		setUp();
		builder.generatePDF(goodFile);
		
		File destDir = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME);
		File subDir_0 = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "0");
		File subDir_1 = new File(SOURCE_DIR + FILE_SEPARATOR + DEST_DIR_NAME + FILE_SEPARATOR + SUB_DIR_NAME + "1");
		
		assertTrue(destDir.exists() && subDir_0.exists() && subDir_1.exists());
	}

	@Test
	public void testExtractTitle() throws IOException {
		String actual = builder.extractTitle(goodFile);
		String expected = "Title";
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void extractTitle_emptyTitle() throws IOException {
		String title = builder.extractTitle(goodFileWithEmptyItems);
		
		assertTrue(title.isEmpty());
	}

	@Test
	public void extractAbstract() throws IOException {
		initDocxParagraphs(goodFile);
		String actual = builder.extractAbstract(goodFile);
		String expected = "Abstract\n" + "Abstract text lorem ipsum.";

		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void extractAbstract_emptyAbstract() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		String actual = builder.extractAbstract(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testExtractKeywords() throws IOException {
		initDocxParagraphs(goodFile);
		List<String> actual = builder.extractKeywords(goodFile);
		
		boolean conditionSize = actual.size() == 3;
		boolean conditoin_1 = actual.get(0).contains("alma");
		boolean conditoin_2 = actual.get(1).contains("korte");
		boolean conditoin_3 = actual.get(2).contains("szilva");
		
		assertTrue(conditionSize && conditoin_1 && conditoin_2 && conditoin_3);
	}
	
	@Test
	public void testExtractKeywords_emptyKeywords() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		List<String> actual = builder.extractKeywords(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testExtractAuthors_EmptyAuthor() throws IOException {
		initDocxParagraphs(goodFileWithEmptyItems);
		List<Author> actual = builder.extractAuthors(goodFileWithEmptyItems);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testExtractAuthors_SingleAuthor() throws IOException {
		initDocxParagraphs(goodFileWithSingleAuthor);
		List<Author> actual = builder.extractAuthors(goodFileWithSingleAuthor);
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
		List<Author> actual = builder.extractAuthors(goodFile);
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