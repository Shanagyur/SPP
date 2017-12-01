package hu.uni.miskolc.iit.spp.latex.compile;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class LinuxLatexPDFCompilerIT extends Latex2PDFCompilerIT {

    @Before
    public void setUp() throws NotSupportedOperationSystemException {
        super.setUp();
        
        assumeTrue("Ignore test, because can't run this operation system.", compiler.getClass().equals(LinuxLatexPDFCompiler.class));
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void initTest() {
        boolean condition_1 = compiler.compilerArg.equals(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = compiler.outputDirArg.equals(LinuxArgs.OUTPUT.getArgument());

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4TerminalTest() {
        File testTexFile = new File("test.tex");
        File testDirectory = new File("directory");
        String command = compiler.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(LinuxArgs.OUTPUT.getArgument());
        boolean condition_3 = command.contains(testDirectory.getName());
        boolean condition_4 = command.contains(testTexFile.getName());

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4);
    }

    @Test
    public void generatePDFFileTest() throws IOException {
        try {
            Runtime.getRuntime().exec("pdflatex -version");
        } catch(Exception e) {
            assumeNoException("Ignore test, because can't compile .tex files.", e);
        }

        compiler.generatePDFFile(texFile, directory);

        assertTrue(pdfFile.exists());
    }
}