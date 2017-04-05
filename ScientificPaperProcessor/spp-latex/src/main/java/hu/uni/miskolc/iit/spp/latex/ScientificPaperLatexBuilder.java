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

	private Collection<String> possibleFileExtension;
	private Collection<String> possibleArchiveExtension;
	private Collection<String> possibleMainFiles;
	private Collection<String> possibleGeneratedFileExtension;
	private String actualTargetDirSubDirPath;
	private String actualGeneratedDirSubDirPath;

	{
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
	
	public ScientificPaperLatexBuilder() {
	}

	@Override
	protected void checkFileExtension(File paper) throws NotSupportedFileExtensionException {
		unzip(paper);
		File[] listOfFiles = new File(getActualTargetDirSubDirPath()).listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleFileExtension)) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException();
	}
	
	private void unzip(File zipFile) throws NotSupportedFileExtensionException {
		try {
			boolean isValidZipFile = checkArchiveExtension(zipFile);
			if(isValidZipFile) {
				ZipFile validZipFile = new ZipFile(zipFile);
				String destinationDir = setTargetDir(zipFile);
				validZipFile.extractAll(destinationDir);
			} else {
				throw new NotSupportedFileExtensionException();
			}
		} catch (ZipException e) {
			e.printStackTrace();
			throw new NotSupportedFileExtensionException();
		}
	}
	
	private boolean checkArchiveExtension(File zipFile) {
		return extensionTest(zipFile, possibleArchiveExtension);
	}
	
	private String setTargetDir(File file) {
		if(getActualTargetDirSubDirPath() == null) {
			createTargetDir(file);
			return getActualTargetDirSubDirPath();
		} else {
			createNewSubDirForTargetDir();
			return getActualTargetDirSubDirPath();
		}
	}
	
	private void createTargetDir(File file) {
		File targetDir = new File(file.getParentFile().getAbsolutePath() + "\\targetDir");
		targetDir.mkdir();
		setActualTargetDirSubDirPath(targetDir.getAbsolutePath());
		createNewSubDirForTargetDir();
	}
	
	private void createNewSubDirForTargetDir() {
		File file = new File(getActualTargetDirSubDirPath()).getParentFile();
		if(file.getName() != "targetDir") {
			File firstSubDir = new File(getActualTargetDirSubDirPath() + "\\version_0");
			firstSubDir.mkdir();
			setActualTargetDirSubDirPath(firstSubDir.getAbsolutePath());
		} else {
			File[] targetDirFolders = file.listFiles();
			File newSubDir = new File(file.getAbsoluteFile() + "\\version_" + targetDirFolders.length);
			newSubDir.mkdir();
			setActualTargetDirSubDirPath(newSubDir.getAbsolutePath());
		}
	}
	
	@Override
	protected String extractTitle(File paper) {
		//impl
		return null;
	}

	@Override
	protected String extractAbstarct(File paper) {
		//impl
		return null;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		//impl
		return null;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		//impl
		return null;
	}

	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException {
		try {
			Process process = Runtime.getRuntime().exec(command(paper));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
			String s;
			while((s = stdInput.readLine()) != null){
			}
			
			return returnPDFDoc();
		}
		catch (NoMainDocumentFoundException e) {
			e.printStackTrace();
			throw new ConversionToPDFException();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ConversionToPDFException();
		}
	}
	
	private String command(File file) throws NoMainDocumentFoundException {
		String latex = callLatexExecuter();
		String includeDirParameter = "-include-directory=\"";
		String rootDir = new File(getActualTargetDirSubDirPath()).getAbsolutePath();
		String outputDirParameter = "\" -output-directory=\"";
		String outputDir = setGeneratedDir(file);
		String mainFile = selectMainFile(new File(rootDir));
		String fullCommand = latex + includeDirParameter + rootDir + outputDirParameter + outputDir + "\" " + mainFile;
		
		return fullCommand;
	}
	
	private String callLatexExecuter() {
		String latexExecuter = "pdflatex" + " ";
		return latexExecuter;
	}
	
	private String setGeneratedDir(File file) {
		if(getActualGeneratedDirSubDirPath() == null) {
			createGeneratedDir(file);
			return getActualGeneratedDirSubDirPath();
		} else {
			createNewSubDirForGeneratedDir();
			return getActualGeneratedDirSubDirPath();
		}
	}
	
	private void createGeneratedDir(File file) {
		File generatedDir = new File(file.getParentFile().getAbsolutePath() + "\\generatedDir");
		generatedDir.mkdir();
		setActualGeneratedDirSubDirPath(generatedDir.getAbsolutePath());
		createNewSubDirForGeneratedDir();
	}
	
	private void createNewSubDirForGeneratedDir() {
		File file = new File(getActualGeneratedDirSubDirPath()).getParentFile();
		if(file.getName() != "generatedDir") {
			File firstSubDir = new File(getActualGeneratedDirSubDirPath() + "\\version_0");
			firstSubDir.mkdir();
			setActualGeneratedDirSubDirPath(firstSubDir.getAbsolutePath());
		} else {
			File[] generatedDirFolders = file.listFiles();
			File newSubDir = new File(file.getAbsoluteFile() + "\\version_" + generatedDirFolders.length);
			newSubDir.mkdir();
			setActualGeneratedDirSubDirPath(newSubDir.getAbsolutePath());
		}
	}
	
	private String selectMainFile(File dir) throws NoMainDocumentFoundException {
		File[] listOfFiles = dir.listFiles();
		for(File list : listOfFiles) {
			if(possibleMainFiles.contains(list.getName())) {
				return list.getName();
			}
		}
		throw new NoMainDocumentFoundException();
	}
	
	private File returnPDFDoc() throws NoMainDocumentFoundException {
		File rootDir = new File(getActualGeneratedDirSubDirPath());
		File[] listOfFiles = rootDir.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleGeneratedFileExtension)) {
				return file;
			}
		}
		throw new NoMainDocumentFoundException();
	}
	
	//methods with more occurrence
	private boolean extensionTest(File file, Collection<String> possibleExtension) {
		String fileName = file.getName();
		String[] fileNameParts = fileName.split("\\.");
		if(possibleExtension.contains(fileNameParts[fileNameParts.length-1])) {
			return true;
		}
		return false;
	}
	
	//getters & setters
	private String getActualTargetDirSubDirPath() {
		return actualTargetDirSubDirPath;
	}

	private void setActualTargetDirSubDirPath(String actualTargetDirSubDirPath) {
		this.actualTargetDirSubDirPath = actualTargetDirSubDirPath;
	}

	private String getActualGeneratedDirSubDirPath() {
		return actualGeneratedDirSubDirPath;
	}

	private void setActualGeneratedDirSubDirPath(String actualGeneratedDirSubDirPath) {
		this.actualGeneratedDirSubDirPath = actualGeneratedDirSubDirPath;
	}
}
