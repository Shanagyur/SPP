package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

public abstract class Latex2PDFCompilerTest {

    protected static final String FILE_SEPARATOR = System.getProperty("file.separator");
    protected static final String RESOURCE_DIR = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "compilePackage";

    protected static File texFile;
    protected static File pdfFile;
    protected static File directory;

    protected Latex2PDFCompiler compilerLinux;
    protected Latex2PDFCompiler compilerWindows;

    @BeforeClass
    public static void beforeClass() {
        texFile = new File(RESOURCE_DIR + FILE_SEPARATOR + "main.tex");
        pdfFile = new File(RESOURCE_DIR + FILE_SEPARATOR + "main.pdf");
        directory = new File(RESOURCE_DIR);
    }

    @Before
    public void setUp() {
        compilerLinux = new LinuxLatexPDFCompiler();
        compilerWindows = new WindowsLatexPDFCompiler();
    }

    @After
    public void tearDown() {
        File[] dirFiles = directory.listFiles();
        for(File file : dirFiles) {
            if(!file.getName().equals(texFile.getName())) {
                file.delete();
            }
        }
    }
}