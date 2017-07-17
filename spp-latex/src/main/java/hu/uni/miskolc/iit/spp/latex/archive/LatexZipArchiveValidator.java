package hu.uni.miskolc.iit.spp.latex.archive;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class LatexZipArchiveValidator extends LatexArchiveValidator {

	protected LatexZipArchiveValidator(File archive) {
		super(archive);
	}

	@Override
	protected void unpack(File archive, File destinationDir) throws IOException {
		try {
			ZipFile zipFile = new ZipFile(archive);
			zipFile.extractAll(destinationDir.getAbsolutePath());
		} catch (ZipException e) {
			throw new IOException(e.getMessage());
		}
	}
}