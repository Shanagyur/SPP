package hu.uni.miskolc.iit.spp.core.service.archive;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;

import java.io.File;
import java.io.IOException;

public abstract class ArchiveScientificPaperBuilder implements ScientificPaperBuilder {

    private ScientificPaperBuilder builder;

    public ArchiveScientificPaperBuilder(ScientificPaperBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ScientificPaper build(String sourceFilePath) throws NoMainDocumentFoundException, ConversionToPDFException, IOException {
        return this.build(new File(sourceFilePath));
    }

    @Override
    public ScientificPaper build(File paper) throws NoMainDocumentFoundException, ConversionToPDFException, IOException {
        checkFileExtension(paper);
        File extractedArchiveDir = extract(paper);
        return builder.build(extractedArchiveDir);
    }

    protected abstract  void checkFileExtension(File paper) throws NoMainDocumentFoundException, IOException;

    protected abstract File extract(File archive);
}