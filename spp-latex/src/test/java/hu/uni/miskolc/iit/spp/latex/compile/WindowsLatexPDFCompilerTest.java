package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WindowsLatexPDFCompilerTest extends Latex2PDFCompilerTest {

    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void initTest() {
        boolean condition_1 = compilerWindows.compilerArg.equals(WindowsArgs.COMPILER.getArgument());
        boolean condition_2 = compilerWindows.outputDirArg.equals(WindowsArgs.OUTPUT.getArgument());

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4TerminalTest() {
        String command = compilerWindows.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(WindowsArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(WindowsArgs.INCLUDE.getArgument());
        boolean condition_3 = command.contains(testTexFile.getParentFile().getAbsolutePath());
        boolean condition_4 = command.contains(WindowsArgs.OUTPUT.getArgument());
        boolean condition_5 = command.contains(testDirectory.getName());
        boolean condition_6 = command.contains(testTexFile.getName());

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4 && condition_5 && condition_6);
    }
}