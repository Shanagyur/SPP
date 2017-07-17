package hu.uni.miskolc.iit.spp.latex.archive;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LatexZipArchiveValidator extends LatexArchiveValidator {

	private static Logger LOG = LogManager.getLogger(LatexZipArchiveValidator.class);

	protected LatexZipArchiveValidator(File archive) {
		super(archive);
	}

	@Override
	protected void unpack(File archive, File destinationDir) throws IOException {
		try {
			ZipFile zipFile = new ZipFile(archive);
			zipFile.extractAll(destinationDir.getAbsolutePath());
		} catch (ZipException e) {
			LOG.fatal("Catch ZipException this message: " + e.getMessage() + "\nAnd throw IOException with the same message");
			throw new IOException(e.getMessage());
		}
	}
}