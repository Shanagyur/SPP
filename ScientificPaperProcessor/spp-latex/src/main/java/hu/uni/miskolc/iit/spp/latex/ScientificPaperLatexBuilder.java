package hu.uni.miskolc.iit.spp.latex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {

	private static Collection<String> possibleFileExtension;
	private static Collection<String> possibleArchiveExtension;
	private static Collection<String> possibleMainFiles;
	private static Collection<String> possibleGeneratedFileExtension;
	private String latexCompiler;
	private File versionDirWithTexFile;
	private File versionDirWithPdfFile;

	static {
		possibleFileExtension = new HashSet<>();
		possibleFileExtension.add("tex");
		possibleArchiveExtension = new HashSet<>();
		possibleArchiveExtension.add("zip");
		possibleMainFiles = new HashSet<>();
		possibleMainFiles.add("main.tex");
		possibleMainFiles.add("paper.tex");
		possibleGeneratedFileExtension = new HashSet<>();
		possibleGeneratedFileExtension.add("pdf");
	}

	public ScientificPaperLatexBuilder(String latexCompiler) {
		super();
		this.setLatexCompiler(latexCompiler);
	}

	@Override
	protected void checkFileExtension(File paper) throws NotSupportedFileExtensionException, IOException {
		File zipContentDir = unzip(paper);
		File[] listOfFiles = zipContentDir.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleFileExtension)) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException();
	}

	private File unzip(File zipFile) throws NotSupportedFileExtensionException, IOException {
		if (extensionTest(zipFile, possibleArchiveExtension) == false) {
			throw new NotSupportedFileExtensionException();
		}
		try {
			ZipFile validZipFile = new ZipFile(zipFile);
			setVersionDirWithTexFile(createDestinationDirectory(zipFile, "targetDir"));
			validZipFile.extractAll(getVersionDirWithTexFile().getAbsolutePath());
			return getVersionDirWithTexFile();
		} catch (ZipException e) {
			throw new IOException();
		}
	}

	@Override
	protected String extractTitle(File paper) {
		// impl
		return null;
	}

	@Override
	protected String extractAbstarct(File paper) {
		// impl
		return null;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		// impl
		return null;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		// impl
		return null;
	}
	
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException, IOException {
		Process process = Runtime.getRuntime().exec(command(paper));
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String s;
		while ((s = stdInput.readLine()) != null) {
		}

		return returnPDFDoc();
	}
	
	private String command(File paper) throws NoMainDocumentFoundException, IOException {
		String latex = getLatexCompiler() + " ";
		String includeDirParameter = "-include-directory=\"";
		File rootDir = getVersionDirWithTexFile();
		String outputDirParameter = "\" -output-directory=\"";
		File outputDir = createDestinationDirectory(paper, "generatedDir");
		setVersionDirWithPdfFile(outputDir);
		File mainFile = selectMainFile();
		
		String fullCommand = latex + includeDirParameter + rootDir.getAbsolutePath() + outputDirParameter + outputDir.getAbsolutePath() + "\" " + mainFile.getName();
		return fullCommand;
	}
	
	private File selectMainFile() throws NoMainDocumentFoundException {
		File[] listOfFiles = getVersionDirWithTexFile().listFiles();
		for (File file : listOfFiles) {
			if (possibleMainFiles.contains(file.getName())) {
				return file;
			}
		}
		throw new NoMainDocumentFoundException();
	}
	
	private File returnPDFDoc() throws NoMainDocumentFoundException {
		File rootDir = getVersionDirWithPdfFile();
		File[] listOfFiles = rootDir.listFiles();
		for (File file : listOfFiles) {
			if (extensionTest(file, possibleGeneratedFileExtension)) {
				return file;
			}
		}
		throw new NoMainDocumentFoundException();
	}

	// methods with more occurrence
	private boolean extensionTest(File file, Collection<String> possibleExtension) {
		String fileName = file.getName();
		String[] fileNameParts = fileName.split("\\.");
		if (possibleExtension.contains(fileNameParts[fileNameParts.length - 1])) {
			return true;
		}
		return false;
	}
	
	private File createDestinationDirectory(File file, String directoryName) throws IOException {
		File directory = new File(file.getParentFile().getAbsolutePath() + "\\" + directoryName);
		if(directory.exists() == false){
			if(!directory.mkdir()){
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			};
		}
		int versionNo = 0;
		while( new File(directory.getAbsolutePath()+"\\version_" + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDir = new File(directory.getAbsolutePath() + "\\version_" + versionNo);
		if(destinationDir.mkdir() == false){
			throw new IOException("Could not create directory: " + destinationDir.getAbsolutePath());
		}
		return destinationDir;
	}
	
	// getters & setters
	private String getLatexCompiler() {
		return latexCompiler;
	}

	private void setLatexCompiler(String latexCompiler) {
		this.latexCompiler = latexCompiler;
	}
	
	private File getVersionDirWithTexFile() {
		return versionDirWithTexFile;
	}

	private void setVersionDirWithTexFile(File versionDirWithTexFile) {
		this.versionDirWithTexFile = versionDirWithTexFile;
	}

	private File getVersionDirWithPdfFile() {
		return versionDirWithPdfFile;
	}

	private void setVersionDirWithPdfFile(File versionDirWithPdfFile) {
		this.versionDirWithPdfFile = versionDirWithPdfFile;
	}
}
