package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Latex2PDFCompiler {

	private static Logger LOG = LogManager.getLogger(Latex2PDFCompiler.class);
	
	protected String compilerArg;
	protected String outputDirArg;

	protected Latex2PDFCompiler() {
		init();
	}

	public File generatePDFFile(File texFile, File destinationDir) throws IOException {
		try {
			Process process = Runtime.getRuntime().exec(command4Terminal(texFile, destinationDir));
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String string;
			while((string = reader.readLine()) != null) {
			}

			String fileName = FilenameUtils.getBaseName(texFile.getName());

			return findPDFFile(destinationDir, fileName);

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException this message: " + e.getMessage() + System.lineSeparator() + "And throw IOException with the same message.");
			throw new IOException(e.getMessage());
		}
	}

	protected abstract void init();
	protected abstract String command4Terminal(File texFile, File destinationDir);

	private File findPDFFile(File sourceDir, String fileName) throws SearchedFileNotExistsException {
		File[] dirFiles = sourceDir.listFiles();
		if(dirFiles != null && dirFiles.length > 0) {
			for(File file : dirFiles) {
				if(file.isDirectory()) {
					
					return findPDFFile(file, fileName);
				}
				if(isSameName(file, fileName) && isPDFFile(file)) {
					
					return file;
				}
			}
		}
		LOG.fatal("Throw SearchedFileNotExistsException this message: Could not find .pdf file with this name: " + fileName);
		throw new SearchedFileNotExistsException("Could not find .pdf file with this name: " + fileName);
	}

	private boolean isSameName(File file, String name) {
		String fileName = FilenameUtils.getBaseName(file.getName());

		return fileName.equals(name);
	}

	private boolean isPDFFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());

		return fileExtension.equals(SupportedGeneratedFileExtensions.PDF.getStringValue());
	}
}