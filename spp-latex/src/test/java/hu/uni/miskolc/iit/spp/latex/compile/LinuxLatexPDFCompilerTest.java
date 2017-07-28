package hu.uni.miskolc.iit.spp.latex.compile;

import org.junit.*;

import static org.junit.Assert.*;

public class LinuxLatexPDFCompilerTest extends Latex2PDFCompilerTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void initTest() {
        boolean condition_1 = compilerLinux.compilerArg.equals(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = compilerLinux.outputDirArg.equals(LinuxArgs.OUTPUT.getArgument());

        assertTrue(condition_1 && condition_2);
    }

    @Test
    public void command4TerminalTest() {
        String command = compilerLinux.command4Terminal(testTexFile, testDirectory);

        boolean condition_1 = command.contains(LinuxArgs.COMPILER.getArgument());
        boolean condition_2 = command.contains(LinuxArgs.OUTPUT.getArgument());
        boolean condition_3 = command.contains(testDirectory.getName());
        boolean condition_4 = command.contains(testTexFile.getName());

        assertTrue(condition_1 && condition_2 && condition_3 && condition_4);
    }
}