package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FilenameUtils;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LinuxLatexPDFCompiler extends Latex2PDFCompiler {

	private static Logger LOG = LogManager.getLogger(LinuxLatexPDFCompiler.class);

	protected LinuxLatexPDFCompiler() {
		super();
	}

	@Override
	protected void init() {
		this.compilerArg = LinuxArgs.COMPILER.getArgument();
		this.outputDirArg = LinuxArgs.OUTPUT.getArgument();
	}

	@Override
	protected File compile(File sourceDir, File destinationDir) throws SearchedFileNotExistsException, IOException {
		Process process = Runtime.getRuntime().exec(commandForTerminal(sourceDir, destinationDir));
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String string;
		while((string = reader.readLine()) != null) {
		}
		
		return findPDFFile(destinationDir);
	}

	@Override
	protected String commandForTerminal(File sourceDir, File destinationDir) throws SearchedFileNotExistsException {
		File mainFile = findTexFile(sourceDir);
		String fullCommand =	this.compilerArg + 
								this.outputDirArg + 
								destinationDir.getAbsolutePath() + " " + 
								mainFile.getAbsolutePath();
		return fullCommand;
	}

	private File findTexFile(File sourceDir) throws SearchedFileNotExistsException {
		try {
			File texFile = findFile(sourceDir, SupportedCompileableTextFileExtensions.TEX.getStringValue());
			return texFile;
		} catch (SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw a new with this message: Not found main.tex or paper.tex file.");
			throw new SearchedFileNotExistsException("Not found main.tex or paper.tex file.");
		}
	}
	
	private File findPDFFile(File sourceDir) throws SearchedFileNotExistsException {
		try {
			File pdfFile = findFile(sourceDir, SupportedGeneratedFileExtensions.PDF.getStringValue());
			return pdfFile;
		} catch (SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw a new with this message: Not found main.pdf or paper.pdf file.");
			throw new SearchedFileNotExistsException("Not found main.pdf or paper.pdf file.");
		}
	}
	
	private File findFile(File sourceDir, String extension) throws SearchedFileNotExistsException {
		File[] dirFiles = sourceDir.listFiles();
		if(dirFiles != null && dirFiles.length > 0) {
			for(File file : dirFiles) {
				if(file.isDirectory()) {
					return findFile(sourceDir, extension);
				}
				if(isAccaptedName(file) && (isTexFile(file) || isPDFFile(file))) {
					if(isThatExtension(file, extension)) {
						return file;
					}
				}
			}
		}
		LOG.fatal("Throw SearchedFileNotExistsException without message.");
		throw new SearchedFileNotExistsException();
	}

	private boolean isAccaptedName(File file) {
		String fileName = FilenameUtils.getBaseName(file.getName()).toLowerCase();
		for(SupportedFileNames name : SupportedFileNames.values()) {
			if(fileName.equals(name.getStringValue())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isTexFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileExtension.equals(SupportedCompileableTextFileExtensions.TEX.getStringValue());
	}
	
	private boolean isPDFFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileExtension.equals(SupportedGeneratedFileExtensions.PDF.getStringValue());
	}
	
	private boolean isThatExtension(File file, String extension) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileExtension.equals(extension);
	}
}