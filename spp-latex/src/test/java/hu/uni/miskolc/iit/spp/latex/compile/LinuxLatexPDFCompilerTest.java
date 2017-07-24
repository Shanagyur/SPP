package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class LinuxLatexPDFCompilerTest {

    private Latex2PDFCompiler compiler;

    @Test
    public void init() throws Exception {
        boolean condition_1 = LinuxArgs.COMPILER.getArgument().equals(compiler.compilerArg);
        boolean condition_2 = LinuxArgs.OUTPUT.getArgument().equals(compiler.outputDirArg);

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4Terminal() throws Exception {
        File tesTexFile = new File("test.tex").getAbsoluteFile();
        File testDirectory = new File("directory");
        String command = compiler.command4Terminal(tesTexFile, testDirectory);

        boolean condition_1 = command.contains(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(LinuxArgs.OUTPUT.getArgument());
        boolean condition_3 = command.contains("test.tex");

        assertTrue(condition_1 && condition_2 && condition_3);
    }

    @Before
    public void setUp() throws Exception {
        compiler = new LinuxLatexPDFCompiler();
    }

    @After
    public void tearDown() throws Exception {
        File testTexFile = new File("test.tex");
        if(testTexFile.exists()) {
            testTexFile.delete();
        }
    }
}