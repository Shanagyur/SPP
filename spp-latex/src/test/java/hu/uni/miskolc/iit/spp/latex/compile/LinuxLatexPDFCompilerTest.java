package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.*;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

public class LinuxLatexPDFCompilerTest extends Latex2PDFCompilerTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void initTest() {
        boolean condition_1 = compilerLinux.compilerArg.equals(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = compilerLinux.outputDirArg.equals(LinuxArgs.OUTPUT.getArgument());

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4TerminalTest() {
        File testTexFile = new File("test.tex");
        File testDirectory = new File("directory");
        String command = compilerLinux.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(LinuxArgs.OUTPUT.getArgument());
        boolean condition_3 = command.contains(testDirectory.getName());
        boolean condition_4 = command.contains(testTexFile.getName());

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4);
    }

    @Test
    public void generatePDFFileTest_Linux() throws IOException {
        assumeTrue("Ignore test, because can't run this operation system.", System.getProperty("os.name").toLowerCase().contains("linux"));
        try {
            Runtime.getRuntime().exec("pdflatex -version");
        } catch(Exception e) {
            assumeNoException("Ignore test, because can't compile .tex files.", e);
        }

        compilerLinux.generatePDFFile(texFile, directory);

        assertTrue(pdfFile.exists());
    }
}