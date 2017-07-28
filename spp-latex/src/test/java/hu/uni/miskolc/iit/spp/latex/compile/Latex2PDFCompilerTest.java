package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

public abstract class Latex2PDFCompilerTest {

    protected static File testTexFile;
    protected static File testDirectory;

    protected Latex2PDFCompiler compilerLinux;
    protected Latex2PDFCompiler compilerWindows;

    @BeforeClass
    public static void beforeClass() {
        testTexFile = new File("test.tex").getAbsoluteFile();
        testDirectory = new File("directory");
    }

    @Before
    public void setUp() {
        compilerLinux = new LinuxLatexPDFCompiler();
        compilerWindows = new WindowsLatexPDFCompiler();
    }
}