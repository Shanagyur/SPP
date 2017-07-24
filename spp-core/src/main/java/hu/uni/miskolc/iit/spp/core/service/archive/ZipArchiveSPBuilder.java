package hu.uni.miskolc.iit.spp.core.service.archive;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ZipArchiveSPBuilder extends ArchiveScientificPaperBuilder {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
    private static final String SUB_DIR_NAME = "version_";
    private static Logger LOG = LogManager.getLogger(ZipException.class);

    public ZipArchiveSPBuilder(ScientificPaperBuilder builder) {
        super(builder);
    }

    @Override
    protected File extract(File archive) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(archive);
            File extractedArchiveDir = initDestinationDir(archive);
            zipFile.extractAll(extractedArchiveDir.getAbsolutePath());

            return extractedArchiveDir;

        } catch (ZipException e) {
            LOG.fatal("Catch ZipException this message: " + e.getMessage() + System.lineSeparator() + "And throw IOException with the same message.");
            throw new IOException(e.getMessage());
        }
    }

    private File initDestinationDir(File rootFile) throws IOException {
        File directory = new File(rootFile.getParentFile().getAbsolutePath() + FILE_SEPARATOR + DEST_DIR_NAME);
        if(!directory.exists()) {
            if(!directory.mkdir()) {
                LOG.fatal("Throw IOException this message: Could not create directory: " + directory.getAbsolutePath());
                throw new IOException("Could not create directory: " + directory.getAbsolutePath());
            }
        }
        int versionNo = 0;
        while(new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + versionNo).exists() == true) {
            versionNo++;
        }
        File destinationDir = new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + versionNo);
        if(!destinationDir.mkdir()) {
            LOG.fatal("Throw IOException this message: Could not create directory: " + destinationDir.getAbsolutePath());
            throw new IOException("Could not create directory: " + destinationDir.getAbsolutePath());
        }

        return destinationDir;
    }
}