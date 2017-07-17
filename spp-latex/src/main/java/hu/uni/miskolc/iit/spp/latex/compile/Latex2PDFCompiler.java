package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;

public abstract class Latex2PDFCompiler {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DESTINATION_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SOURCE_DIR_NAME = UsedDirectoryNames.DIR_FOR_EXTRACT_FILES.getStringValue();
	private static final String SUBDIR_NAME = "version_"; 
	protected String compilerArg;
	protected String outputDirArg;
	
	protected Latex2PDFCompiler() {
		init();
	}
	
	public File generatePDFFile(File paper) throws ConversionToPDFException {
		try {
			File destinationDir = initDestinationDir(paper);
			File sourceDir = findSourceDir(paper);
			File pdf = compile(sourceDir, destinationDir);
			return pdf;
		} catch (SearchedFileNotExistsException e) {
			throw new ConversionToPDFException(e.getMessage());
		} catch (IOException e) {
			throw new ConversionToPDFException(e.getMessage());
		}
	}

	protected abstract void init();
	
	protected abstract File compile(File sourceDir, File destinationDir) throws SearchedFileNotExistsException, IOException;
	
	protected abstract String commandForTerminal(File sourceDir, File destinationDir) throws SearchedFileNotExistsException;
	
	private File initDestinationDir(File paper) throws IOException {
		File directory = new File(paper.getParentFile().getAbsolutePath() + FILE_SEPARATOR + DESTINATION_DIR_NAME);
		if(directory.exists() == false) {
			if(directory.mkdir() == false) {
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while(new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUBDIR_NAME + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDir = new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUBDIR_NAME + versionNo);
		if(destinationDir.mkdir() == false) {
			throw new IOException("Could not create directory: " + destinationDir.getAbsolutePath());
		}
		return destinationDir;
	}
	
	private File findSourceDir(File paper) throws IOException {
		File rootDir = new File(paper.getParentFile().getAbsolutePath() + FILE_SEPARATOR + SOURCE_DIR_NAME);
		if(rootDir.exists() == false) {
			throw new IOException("Directory with tex file not exists.");
		}
		int subdirsNo = rootDir.listFiles().length;
		File actualSubdir = new File(rootDir.getAbsolutePath() + FILE_SEPARATOR + SUBDIR_NAME + (subdirsNo - 1));
		return actualSubdir;
	}
}