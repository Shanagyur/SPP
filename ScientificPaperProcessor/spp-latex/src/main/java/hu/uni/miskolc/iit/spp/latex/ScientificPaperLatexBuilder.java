package hu.uni.miskolc.iit.spp.latex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

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
	private ArrayList<String> mainFileContent;

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
			if(extensionTest(file, possibleFileExtension) == true) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException("Could not find .tex file in: " + zipContentDir.getAbsolutePath());
	}

	private File unzip(File zipFile) throws NotSupportedFileExtensionException, IOException {
		if(extensionTest(zipFile, possibleArchiveExtension) == false) {
			throw new NotSupportedFileExtensionException("Could not extract files because not .zip file: " + zipFile.getAbsolutePath());
		}
		try {
			ZipFile validZipFile = new ZipFile(zipFile);
			this.versionDirWithTexFile = createDestinationDirectory(zipFile, "targetDir");
			validZipFile.extractAll(this.versionDirWithTexFile.getAbsolutePath());
			return this.versionDirWithTexFile;
		} catch (ZipException e) {
			throw new IOException();
		}
	}
	
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException, IOException {
		try {
			Process process = Runtime.getRuntime().exec(command(paper));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String s;
			while ((s = stdInput.readLine()) != null) {
			}

			return returnPDFDoc();
		} catch (NoMainDocumentFoundException e) {
			throw new ConversionToPDFException(e.getMessage());
		}
	}
	
	private String command(File paper) throws NoMainDocumentFoundException, IOException {
		String latex = getLatexCompiler() + " ";
		String includeDirParameter = "-include-directory=\"";
		File rootDir = this.versionDirWithTexFile;
		String outputDirParameter = "\" -output-directory=\"";
		File outputDir = createDestinationDirectory(paper, "generatedDir");
		this.versionDirWithPdfFile = outputDir;
		File mainFile = selectMainFile();
		
		String fullCommand = latex + includeDirParameter + rootDir.getAbsolutePath() + outputDirParameter + outputDir.getAbsolutePath() + "\" " + mainFile.getName();
		return fullCommand;
	}
	
	private File selectMainFile() throws NoMainDocumentFoundException {
		File[] listOfFiles = this.versionDirWithTexFile.listFiles();
		for(File file : listOfFiles) {
			if(possibleMainFiles.contains(file.getName())) {
				return file;
			}
		}
		throw new NoMainDocumentFoundException("Could not find correct .tex file (correct files: " + possibleMainFiles.toString() + " ) in directory: " + this.versionDirWithPdfFile.getAbsolutePath());
	}
	
	private File returnPDFDoc() throws NoMainDocumentFoundException {
		File[] listOfFiles = this.versionDirWithPdfFile.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleGeneratedFileExtension) == true) {
				return file;
			}
		}
		throw new NoMainDocumentFoundException("Could not find .pdf file in: " + this.versionDirWithPdfFile.getAbsolutePath());
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
	
	private ArrayList<String> fillUpFromFile(File texFile) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(texFile));
		
		ArrayList<String> allString = new ArrayList<>();
		String currentLine;
		
		while((currentLine = br.readLine()) != null) {
			if(containsInput(currentLine) == true) {
				String texFileName = texFileName(currentLine);
				allString.addAll(fillUpFromFile(searchFile(texFileName, versionDirWithTexFile)));
			} else {
				allString.add(currentLine);
			}
		}
		return allString;
	}
	
	private boolean containsInput(String string) {
		return string.substring(string.indexOf("\\") + 1, string.indexOf("{")).equals("input") == true;
	}
	
	private String texFileName(String string) {
		String name = string.substring(string.indexOf("{") + 1, string.indexOf("}"));
		return name;
	}
	
	private File searchFile(String fileName, File directory) throws Exception {
		File[] listOfFiles = directory.listFiles();
		for(File file : listOfFiles) {
			if(file.isDirectory() == true) {
				return searchFile(fileName, file);
			}
			if(fileName.equals(FilenameUtils.removeExtension(file.getName())) == true) {
				if(extensionTest(file, possibleFileExtension) == true) {
					return file;
				}
			}
		}
		throw new Exception();
	}
	
	// methods with more occurrence
	private boolean extensionTest(File file, Collection<String> possibleExtension) {
		String fileName = file.getName();
		String[] fileNameParts = fileName.split("\\.");
		if(possibleExtension.contains(fileNameParts[fileNameParts.length - 1])) {
			return true;
		}
		return false;
	}
	
	private File createDestinationDirectory(File file, String directoryName) throws IOException {
		File directory = new File(file.getParentFile().getAbsolutePath() + "\\" + directoryName);
		if(directory.exists() == false) {
			if(!directory.mkdir()) {
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while( new File(directory.getAbsolutePath()+"\\version_" + versionNo).exists() == true) {
			versionNo++;
		}
		File destinationDir = new File(directory.getAbsolutePath() + "\\version_" + versionNo);
		if(destinationDir.mkdir() == false) {
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
}
