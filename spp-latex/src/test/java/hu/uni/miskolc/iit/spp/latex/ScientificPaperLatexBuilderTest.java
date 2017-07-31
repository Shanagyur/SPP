package hu.uni.miskolc.iit.spp.latex;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;
import org.easymock.EasyMock;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScientificPaperLatexBuilderTest {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "SPLBFolder";

    private static File dirGoodFile;
    private static File dirWrongFile;
    private static File dirOneAuthorAndKeyword;
    private static File dirMoreAuthorsAndKeyword;

    private ScientificPaperLatexBuilder mockBuilder;

    @BeforeClass
    public static void beforeClass() {
        dirGoodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "folderGoodFile");
        dirWrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "folderWrongFile");
        dirOneAuthorAndKeyword = new File(SOURCE_DIR + FILE_SEPARATOR + "oneAuthorAndKeyword");
        dirMoreAuthorsAndKeyword = new File(SOURCE_DIR + FILE_SEPARATOR + "moreAuthorsAndKeyword");
    }

    @Before
    public void setUp() {
        mockBuilder = new ScientificPaperLatexBuilder(EasyMock.mock(Latex2PDFCompiler.class));
    }

    @After
    public void tearDown() {
        File destDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
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
    public void checkFileExtension() throws IOException, NoMainDocumentFoundException {
        mockBuilder.checkFileExtension(dirGoodFile);
    }

    @Test(expected = NoMainDocumentFoundException.class)
    public void checkFileExtension_ThrowException() throws IOException, NoMainDocumentFoundException {
        mockBuilder.checkFileExtension(dirWrongFile);
    }

    @Test
    public void generatePDF_InitDestinationDir() throws IOException, ConversionToPDFException {
        Latex2PDFCompiler mockCompiler = EasyMock.mock(Latex2PDFCompiler.class);
        File texFile = new File(dirGoodFile.getPath() + FILE_SEPARATOR + "main.tex");
        File destDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");
        EasyMock.expect(mockCompiler.generatePDFFile(texFile, destDir)).andReturn(new File("main.pdf"));
        EasyMock.replay(mockCompiler);

        mockBuilder = new ScientificPaperLatexBuilder(mockCompiler);
        mockBuilder.generatePDF(dirGoodFile);

        File expectedDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
        boolean condition_1 = expectedDir.exists();
        File expectedSubDir = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
        boolean condition_2 = expectedSubDir.exists();

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void generatePDF_InitMoreDestinationDir() throws IOException, ConversionToPDFException {
        File texFile = new File(dirGoodFile.getPath() + FILE_SEPARATOR + "main.tex");
        File destDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");

        Latex2PDFCompiler mockCompiler_0 = EasyMock.mock(Latex2PDFCompiler.class);
        EasyMock.expect(mockCompiler_0.generatePDFFile(texFile, destDir)).andReturn(new File("main.pdf"));

        Latex2PDFCompiler mockCompiler_1 = EasyMock.mock(Latex2PDFCompiler.class);
        EasyMock.expect(mockCompiler_1.generatePDFFile(texFile, destDir)).andReturn(new File("main.pdf"));

        mockBuilder = new ScientificPaperLatexBuilder(mockCompiler_0);
        mockBuilder.generatePDF(dirGoodFile);
        mockBuilder = new ScientificPaperLatexBuilder(mockCompiler_1);
        mockBuilder.generatePDF(dirGoodFile);

        File expectedDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
        boolean condition_1 = expectedDir.exists();
        File expectedSubDir_0 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
        boolean condition_2 = expectedSubDir_0.exists();
        File expectedSubDir_1 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_1");
        boolean condition_3 = expectedSubDir_1.exists();

        assertTrue(condition_1 && condition_2 && condition_3);
    }

    @Test
    public void generatePDF_FindTexFile() throws IOException, ConversionToPDFException {
        Latex2PDFCompiler mockCompiler = EasyMock.mock(Latex2PDFCompiler.class);
        File texFile = new File(dirGoodFile.getPath() + FILE_SEPARATOR + "main.tex");
        File destDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue() + FILE_SEPARATOR + "version_0");
        EasyMock.expect(mockCompiler.generatePDFFile(texFile, destDir)).andReturn(new File("main.pdf"));
        EasyMock.replay(mockCompiler);

        mockBuilder = new ScientificPaperLatexBuilder(mockCompiler);
        mockBuilder.generatePDF(dirGoodFile);
    }

    @Test(expected = ConversionToPDFException.class)
    public void generatePDF_FindTexFile_ThrowException() throws IOException, ConversionToPDFException {
        mockBuilder.generatePDF(dirWrongFile);
    }

    @Test(expected = IOException.class)
    public void extractTitle_FindTexFile_ThrowException() throws IOException {
        mockBuilder.extractTitle(dirWrongFile);
    }

    @Test
    public void extractTitle_EmptyTitle() throws IOException {
        String title = mockBuilder.extractTitle(dirGoodFile);

        assertTrue(title.isEmpty());
    }

    @Test
    public void extractTitle() throws IOException {
        String actual = mockBuilder.extractTitle(dirOneAuthorAndKeyword);
        String expected = "Document Title";

        assertTrue(actual.contains(expected));
    }

    @Test
    public void extractAbstract_EmptyAbstract() throws IOException {
        mockBuilder.extractTitle(dirGoodFile);
        String resume = mockBuilder.extractAbstract(dirGoodFile);

        assertTrue(resume.isEmpty());
    }

    @Test
    public void extractAbstract() throws IOException {
        mockBuilder.extractTitle(dirOneAuthorAndKeyword);
        String actual = mockBuilder.extractAbstract(dirOneAuthorAndKeyword);
        String expected = "Abstract text in more lines and used input command.";

        assertTrue(actual.contains(expected));
    }

    @Test
    public void extractKeywords_EmptyKeyword() throws IOException {
        mockBuilder.extractTitle(dirGoodFile);
        List<String> keywords = mockBuilder.extractKeywords(dirGoodFile);

        assertTrue(keywords.isEmpty());
    }

    @Test
    public void extractKeywords_OneKeyword() throws IOException {
        mockBuilder.extractTitle(dirOneAuthorAndKeyword);
        List<String> actualKeywords = mockBuilder.extractKeywords(dirOneAuthorAndKeyword);

        boolean conditionSize = actualKeywords.size() == 1;
        boolean condition = actualKeywords.toString().contains("keyword1");

        assertTrue(conditionSize && condition);
    }

    @Test
    public void extractKeywords_MoreKeyword() throws IOException {
        mockBuilder.extractTitle(dirMoreAuthorsAndKeyword);
        List<String> keywords = mockBuilder.extractKeywords(dirMoreAuthorsAndKeyword);

        boolean conditionSize = keywords.size() == 3;
        boolean condition_1 = keywords.get(0).toString().contains("keyword1");
        boolean condition_2 = keywords.get(1).toString().contains("keyword2");
        boolean condition_3 = keywords.get(2).toString().contains("keyword3");

        assertTrue(conditionSize && condition_1 && condition_2 && condition_3);
    }

    @Test
    public void extractAuthors_EmptyAuthor() throws IOException {
        mockBuilder.extractTitle(dirGoodFile);
        List<Author> authors = mockBuilder.extractAuthors(dirGoodFile);

        assertTrue(authors.isEmpty());
    }

    @Test
    public void extractAuthors_OneAuthor() throws IOException {
        mockBuilder.extractTitle(dirOneAuthorAndKeyword);
        List<Author> actualAuthors = mockBuilder.extractAuthors(dirOneAuthorAndKeyword);

        Author expectedAuthor = new Author("John Smith", "john.s@mail.com", "University of Mordor, Middle-earth");
        boolean conditionSize = actualAuthors.size() == 1;
        boolean condition = actualAuthors.get(0).toString().equals(expectedAuthor.toString());

        assertTrue(conditionSize && condition);
    }

    @Test
    public void extractAuthors_MoreAuthor() throws IOException {
        mockBuilder.extractTitle(dirMoreAuthorsAndKeyword);
        List<Author> actualAuthors = mockBuilder.extractAuthors(dirMoreAuthorsAndKeyword);

        List<Author> expectedAuthors = new ArrayList();
        expectedAuthors.add(new Author("Jean Smith", "jean.s@mail.com", "University of Gondor, Middle-earth"));
        expectedAuthors.add(new Author("Jane Smith", "jane.s@mail.com", "University of Gondor, Middle-earth"));
        expectedAuthors.add(new Author("John Smith", "john.s@mail.com", "University of Mordor, Middle-earth"));

        boolean conditionSize = actualAuthors.size() == 3;
        boolean condition_1 = expectedAuthors.contains(actualAuthors.get(0));
        boolean condition_2 = expectedAuthors.contains(actualAuthors.get(1));
        boolean condition_3 = expectedAuthors.contains(actualAuthors.get(2));

        assertTrue(conditionSize && condition_1 && condition_2 && condition_3);
    }
}