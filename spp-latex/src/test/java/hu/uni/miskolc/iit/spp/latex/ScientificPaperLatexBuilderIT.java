package hu.uni.miskolc.iit.spp.latex;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;
import hu.uni.miskolc.iit.spp.latex.compile.LatexCompilerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class ScientificPaperLatexBuilderIT {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "SPLBFolder";

    private static File dirGoodFile;
    private static File dirWrongFile;
    private static File dirOneAuthorAndKeyword;
    private static File dirMoreAuthorsAndKeyword;

    private ScientificPaperLatexBuilder builder;

    @BeforeClass
    public static void beforeClass() {
        try {
            LatexCompilerFactory factory = new LatexCompilerFactory(System.getProperty("os.name"));
            Latex2PDFCompiler compiler = factory.createLatexPDFCompiler();
            Runtime.getRuntime().exec("pdflatex -version");

        } catch(NotSupportedOperationSystemException e) {
            assumeNoException("Ignore test, because can't run this operation system.", e);

        } catch (IOException e) {
            assumeNoException("Ignore test, because can't compile .tex files.", e);
        }

        dirGoodFile = new File(SOURCE_DIR + FILE_SEPARATOR + "folderGoodFile");
        dirWrongFile = new File(SOURCE_DIR + FILE_SEPARATOR + "folderWrongFile");
        dirOneAuthorAndKeyword = new File(SOURCE_DIR + FILE_SEPARATOR + "oneAuthorAndKeyword");
        dirMoreAuthorsAndKeyword = new File(SOURCE_DIR + FILE_SEPARATOR + "moreAuthorsAndKeyword");
    }

    @Before
    public void setUp() throws Exception {
        LatexCompilerFactory factory = new LatexCompilerFactory(System.getProperty("os.name"));
        Latex2PDFCompiler compiler = factory.createLatexPDFCompiler();
        builder = new ScientificPaperLatexBuilder(compiler);
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
        builder.checkFileExtension(dirGoodFile);
    }

    @Test(expected = NoMainDocumentFoundException.class)
    public void checkFileExtension_ThrowException() throws IOException, NoMainDocumentFoundException {
        builder.checkFileExtension(dirWrongFile);
    }

    @Test
    public void generatePDF_InitDestinationDir() throws ConversionToPDFException {
        builder.generatePDF(dirGoodFile);

        File expectedDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
        boolean condition_1 = expectedDir.exists();
        File expectedSubDir = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
        boolean condition_2 = expectedSubDir.exists();

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void generatePDF_InitMoreDestinationDir() throws ConversionToPDFException {
        for(int i = 0; i < 2; i++) {
            builder.generatePDF(dirGoodFile);
        }

        File expectedDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
        boolean condition_1 = expectedDir.exists();
        File expectedSubDir_0 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
        boolean condition_2 = expectedSubDir_0.exists();
        File expectedSubDir_1 = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_1");
        boolean condition_3 = expectedSubDir_1.exists();

        assertTrue(condition_1 && condition_2 && condition_3);
    }

    @Test
    public void generatePDF_FindTexFile() throws ConversionToPDFException {
        builder.generatePDF(dirGoodFile);
    }

    @Test(expected = ConversionToPDFException.class)
    public void generatePDF_FindTexFile_ThrowException() throws ConversionToPDFException {
        builder.generatePDF(dirWrongFile);
    }

    @Test
    public void generatePDF() throws ConversionToPDFException {
        builder.generatePDF(dirGoodFile);

        File expectedDir = new File(new File(SOURCE_DIR).getParentFile().getAbsolutePath() + FILE_SEPARATOR + UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue());
        boolean condition_1 = expectedDir.exists();
        File expectedSubDir = new File(expectedDir.getAbsolutePath() + FILE_SEPARATOR + "version_0");
        boolean condition_2 = expectedSubDir.exists();
        File expectedPDF = new File(expectedSubDir.getAbsolutePath() + FILE_SEPARATOR + "main.pdf");
        boolean condition_3 = expectedPDF.exists();

        assertTrue(condition_1 && condition_2 && condition_3);
    }

    @Test(expected = IOException.class)
    public void extractTitle_FindTexFile_ThrowException() throws IOException {
        builder.extractTitle(dirWrongFile);
    }

    @Test
    public void extractTitle_EmptyTitle() throws IOException {
        String title = builder.extractTitle(dirGoodFile);

        assertTrue(title.isEmpty());
    }

    @Test
    public void extractTitle() throws IOException {
        String actual = builder.extractTitle(dirOneAuthorAndKeyword);
        String expected = "Document Title";

        assertTrue(actual.contains(expected));
    }

    @Test
    public void extractAbstract_EmptyAbstract() throws Exception {
        builder.extractTitle(dirGoodFile);
        String resume = builder.extractAbstract(dirGoodFile);

        assertTrue(resume.isEmpty());
    }

    @Test
    public void extractAbstract() throws IOException {
        builder.extractTitle(dirOneAuthorAndKeyword);
        String actual = builder.extractAbstract(dirOneAuthorAndKeyword);
        String expected = "Abstract text in more lines and used input command.";

        assertTrue(actual.contains(expected));
    }

    @Test
    public void extractKeywords_EmptyKeyword() throws IOException {
        builder.extractTitle(dirGoodFile);
        List<String> keywords = builder.extractKeywords(dirGoodFile);

        assertTrue(keywords.isEmpty());
    }

    @Test
    public void extractKeywords_OneKeyword() throws IOException {
        builder.extractTitle(dirOneAuthorAndKeyword);
        List<String> actualKeywords = builder.extractKeywords(dirOneAuthorAndKeyword);

        boolean conditionSize = actualKeywords.size() == 1;
        boolean condition = actualKeywords.toString().contains("keyword1");

        assertTrue(conditionSize && condition);
    }

    @Test
    public void extractKeywords_MoreKeyword() throws IOException {
        builder.extractTitle(dirMoreAuthorsAndKeyword);
        List<String> keywords = builder.extractKeywords(dirMoreAuthorsAndKeyword);

        boolean conditionSize = keywords.size() == 3;
        boolean condition_1 = keywords.get(0).toString().contains("keyword1");
        boolean condition_2 = keywords.get(1).toString().contains("keyword2");
        boolean condition_3 = keywords.get(2).toString().contains("keyword3");

        assertTrue(conditionSize && condition_1 && condition_2 && condition_3);
    }

    @Test
    public void extractAuthors_EmptyAuthor() throws IOException {
        builder.extractTitle(dirGoodFile);
        List<Author> authors = builder.extractAuthors(dirGoodFile);

        assertTrue(authors.isEmpty());
    }

    @Test
    public void extractAuthors_OneAuthor() throws IOException {
        builder.extractTitle(dirOneAuthorAndKeyword);
        List<Author> actualAuthors = builder.extractAuthors(dirOneAuthorAndKeyword);

        Author expectedAuthor = new Author("John Smith", "john.s@mail.com", "University of Mordor, Middle-earth");
        boolean conditionSize = actualAuthors.size() == 1;
        boolean condition = actualAuthors.get(0).toString().equals(expectedAuthor.toString());

        assertTrue(conditionSize && condition);
    }

    @Test
    public void extractAuthors_MoreAuthor() throws IOException {
        builder.extractTitle(dirMoreAuthorsAndKeyword);
        List<Author> actualAuthors = builder.extractAuthors(dirMoreAuthorsAndKeyword);

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