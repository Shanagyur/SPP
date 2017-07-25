package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

public class WindowsLatexPDFCompilerTest extends Latex2PDFCompilerTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown()  {
        super.tearDown();
    }

    @Test
    public void initTest() {
        boolean condition_1 = compilerWindows.compilerArg.equals(WindowsArgs.COMPILER.getArgument());
        boolean condition_2 = compilerWindows.outputDirArg.equals(WindowsArgs.OUTPUT.getArgument());

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4TerminalTest() {
        File testTexFile = new File("test.tex").getAbsoluteFile();
        File testDirectory = new File("directory");
        String command = compilerWindows.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(WindowsArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(WindowsArgs.INCLUDE.getArgument());
        boolean condition_3 = command.contains(testTexFile.getParentFile().getAbsolutePath());
        boolean condition_4 = command.contains(WindowsArgs.OUTPUT.getArgument());
        boolean condition_5 = command.contains(testDirectory.getName());
        boolean condition_6 = command.contains(testTexFile.getName());

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4 && condition_5 && condition_6);
    }

    @Test
    public void generatePDFFileTest_Windows() throws IOException {
        assumeTrue("Ignore test, because can't run this operation system.", System.getProperty("os.name").toLowerCase().contains("windows"));
        try {
            Runtime.getRuntime().exec("pdflatex -version");
        } catch(Exception e) {
            assumeNoException("Ignore test, because can't compile .tex files.", e);
        }

        compilerWindows.generatePDFFile(texFile, directory);

        assertTrue(pdfFile.exists());
    }
}