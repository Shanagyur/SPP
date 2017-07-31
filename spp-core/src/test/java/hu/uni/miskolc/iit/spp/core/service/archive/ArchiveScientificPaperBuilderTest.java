package hu.uni.miskolc.iit.spp.core.service.archive;

import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public abstract class ArchiveScientificPaperBuilderTest {

    protected static final String FILE_SEPARATOR = System.getProperty("file.separator");
    protected static final String SOURCE_DIR = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "archivePackage";

    protected static File zipFile;
    protected static File notZipFile;

    protected ScientificPaperBuilder mockSPB;
    protected ArchiveScientificPaperBuilder mockBuilder;

    @BeforeClass
    public static void beforeClass() {
        zipFile = new File(SOURCE_DIR + FILE_SEPARATOR + "txtInZip.zip");
        notZipFile = new File(SOURCE_DIR + FILE_SEPARATOR + "txtInRar.rar");
    }

    @Before
    public void setUp() {
    }
}