package hu.uni.miskolc.iit.spp.core.service.archive;

import org.junit.BeforeClass;

import java.io.File;

public abstract class ArchiveScientificPaperBuilderTest {

    protected static final String FILE_SEPARATOR = System.getProperty("file.separator");
    protected static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "resources";

    protected static File zipFile;
    protected static File notZipFile;
    protected static File zipFileWithPassword;
    
    @BeforeClass
    public static void beforeClass() {
        zipFile = new File(SOURCE_DIR + FILE_SEPARATOR + "txtInZip.zip");
        notZipFile = new File(SOURCE_DIR + FILE_SEPARATOR + "txtInRar.rar");
        zipFileWithPassword = new File(SOURCE_DIR + FILE_SEPARATOR + "txtInProtectedZip.zip");
    }
}