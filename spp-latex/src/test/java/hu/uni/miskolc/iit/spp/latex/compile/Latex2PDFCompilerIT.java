package hu.uni.miskolc.iit.spp.latex.compile;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

import static org.junit.Assume.assumeNoException;

public abstract class Latex2PDFCompilerIT {

    protected static final String FILE_SEPARATOR = System.getProperty("file.separator");
    protected static final String RESOURCE_DIR = "src" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "compilePackage";

    protected static File texFile;
    protected static File pdfFile;
    protected static File directory;

    protected Latex2PDFCompiler compiler;

    @BeforeClass
    public static void beforeClass() {
        try {
            LatexCompilerFactory factory = new LatexCompilerFactory(System.getProperty("os.name"));
            Latex2PDFCompiler compiler = factory.createLatexPDFCompiler();

        } catch(NotSupportedOperationSystemException e) {
            assumeNoException("Ignore test, because can't run this operation system.", e);
        }

        texFile = new File(RESOURCE_DIR + FILE_SEPARATOR + "main.tex");
        pdfFile = new File(RESOURCE_DIR + FILE_SEPARATOR + "main.pdf");
        directory = new File(RESOURCE_DIR);
    }

    @Before
    public void setUp() throws NotSupportedOperationSystemException {
        LatexCompilerFactory factory = new LatexCompilerFactory(System.getProperty("os.name"));
        compiler = factory.createLatexPDFCompiler();
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