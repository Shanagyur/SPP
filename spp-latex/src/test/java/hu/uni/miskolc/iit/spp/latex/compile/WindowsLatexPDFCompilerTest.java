package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WindowsLatexPDFCompilerTest {

    private Latex2PDFCompiler compiler;

    @Test
    public void init() throws Exception {
        boolean condition_1 = WindowsArgs.COMPILER.getArgument().equals(compiler.compilerArg);
        boolean condition_2 = WindowsArgs.OUTPUT.getArgument().equals(compiler.outputDirArg);

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4Terminal() throws Exception {
        File testTexFile = new File("test.tex").getAbsoluteFile();
        File testDirectory = new File("directory");
        String command = compiler.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(WindowsArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(WindowsArgs.INCLUDE.getArgument());
        boolean condition_3 = command.contains(WindowsArgs.OUTPUT.getArgument());
        boolean condition_4 = command.contains("test.tex");

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4);
    }

    @Before
    public void setUp() throws Exception {
        compiler = new WindowsLatexPDFCompiler();
    }

    @After
    public void tearDown() throws Exception {
        File testTexFile = new File("test.tex");
        if(testTexFile.exists()) {
            testTexFile.delete();
        }
    }
}