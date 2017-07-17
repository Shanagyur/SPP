package hu.uni.miskolc.iit.spp.core.service.archive;

import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.ScientificPaperBuilder;

import java.io.File;
import java.io.IOException;

public class ZipArchiveSPBuilder extends ArchiveScientificPaperBuilder {

    public ZipArchiveSPBuilder(ScientificPaperBuilder builder) {
        super(builder);
    }

    @Override
    protected void checkFileExtension(File paper) throws NoMainDocumentFoundException, IOException {
        if(!paper.getName().endsWith("zip")){
            throw  new IOException();
        }
    }

    @Override
    protected File extract(File archive) {
        return null;
    }
}