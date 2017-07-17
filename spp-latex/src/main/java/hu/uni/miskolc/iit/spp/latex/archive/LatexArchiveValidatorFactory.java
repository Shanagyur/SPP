package hu.uni.miskolc.iit.spp.latex.archive;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LatexArchiveValidatorFactory {

	private static Logger LOG = LogManager.getLogger(LatexArchiveValidatorFactory.class);
	private File archive;

	public LatexArchiveValidatorFactory(File archive) {
		this.archive = archive;
	}
	
	public LatexArchiveValidator createArchiveValidator() throws NotSupportedArchiveExtensionException {
		if(isSupportedArchive()) {
			if(isZipFile()) {
				return new LatexZipArchiveValidator(archive);
			}
		}
		LOG.fatal("Throw NotSupportedArchiveExtensionException this message: Could not unpack, because " + archive.getName() + " extension not supported.");
		throw new NotSupportedArchiveExtensionException("Could not unpack, because " + archive.getName() + " extension not supported.");
	}

	private boolean isSupportedArchive() {
		String fileExtension = FilenameUtils.getExtension(archive.getName());
		for(SupportedArchiveExtensions extension : SupportedArchiveExtensions.values()) {
			if(extension.getStringValue().equals(fileExtension)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isZipFile() {
		String fileExtension = FilenameUtils.getExtension(archive.getName());
		return fileExtension.equals(SupportedArchiveExtensions.ZIP.getStringValue());
	}
}