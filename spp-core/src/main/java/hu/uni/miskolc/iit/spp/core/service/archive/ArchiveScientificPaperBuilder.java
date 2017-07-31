package hu.uni.miskolc.iit.spp.core.service.archive;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public abstract class ArchiveScientificPaperBuilder implements ScientificPaperBuilder {

    private static Logger LOG = LogManager.getLogger(ArchiveScientificPaperBuilder.class);
    private ScientificPaperBuilder builder;

    protected ArchiveScientificPaperBuilder(ScientificPaperBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ScientificPaper build(String sourceFilePath) throws NoMainDocumentFoundException, ConversionToPDFException, IOException {
        return this.build(new File(sourceFilePath));
    }

    @Override
    public ScientificPaper build(File paper) throws NoMainDocumentFoundException, ConversionToPDFException, IOException {
        if(!isSupportedArchive(paper)) {
            LOG.fatal("Throw IOException this message:  Not supported compression format.");
            throw new IOException("Not supported compression format.");
        }
        File extractedArchiveDir = extract(paper);
        return builder.build(extractedArchiveDir);
    }

    protected abstract File extract(File archive) throws IOException;

    private boolean isSupportedArchive(File file) {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        for(SupportedArchiveExtensions extension : SupportedArchiveExtensions.values()) {
            if(fileExtension.equals(extension.getStringValue())) {
                return true;
            }
        }
        return false;
    }
}