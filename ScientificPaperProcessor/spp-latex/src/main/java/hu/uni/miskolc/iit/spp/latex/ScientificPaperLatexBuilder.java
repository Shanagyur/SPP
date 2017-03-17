package hu.uni.miskolc.iit.spp.latex;

import java.io.BufferedReader;
import java.io.File;
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
	
	private String actualTargetDirSubDirPath;
	private String actualGeneratedDirSubDirPath;

	{
		possibleFileExtension = new HashSet<>();
		possibleFileExtension.add(".tex");
		possibleArchiveExtension = new HashSet<>();
		possibleArchiveExtension.add(".zip");
		possibleMainFiles = new HashSet<>();
		possibleMainFiles.add("main.tex");
		possibleMainFiles.add("paper.tex");
	}

	@Override
	protected void checkFileExtension(File paper) throws NotSupportedFileExtensionException {
		unzip(paper);
		File dir = new File(getActualTargetDirSubDirPath());
		File[] listOfFiles = dir.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleFileExtension)) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException();
	}
	
	private void unzip(File zipFile) throws NotSupportedFileExtensionException {
		try {
			ZipFile validZipFile = new ZipFile(checkArchiveExtension(zipFile));
			String destinationDir = setTargetDir(zipFile);
			validZipFile.extractAll(destinationDir);
			setActualTargetDirSubDirPath(destinationDir);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	private File checkArchiveExtension(File zipFile) throws NotSupportedFileExtensionException {
		if(extensionTest(zipFile, possibleArchiveExtension)) {
			return zipFile;
		} else {
			throw new NotSupportedFileExtensionException();
		}
	}
	
	private String setTargetDir(File file) {
		if(targetDirIsExist(file)) {
			return createNewSubDirForTargetDir(file);
		}
		return createTargetDir(file);
	}
	
	private boolean targetDirIsExist(File file) {
		File targetDir = new File(file.getParent() + "\\targetDir");
		if(targetDir.exists()) {
			return true;
		}
		return false;
	}
	
	private String createNewSubDirForTargetDir(File file) {
		File targetDir = new File(file.getParent() + "\\targetDir");
		String[] subDirs = targetDir.list();
		String newSubDir = targetDir.getPath() + "\\version_" + subDirs.length;
		new File(newSubDir).mkdir();
		return newSubDir;
	}
	
	private String createTargetDir(File file) {
		String targetDirWithFirstSubDir = file.getParent() + "\\targetDir\\version_0";
		new File(targetDirWithFirstSubDir).mkdir();
		return targetDirWithFirstSubDir;
	}
	
	private boolean extensionTest(File file, Collection<String> possibleExtension) {
		String fileName = file.getName();
		String[] fileNameParts = fileName.split("\\.");
		if(possibleExtension.contains(fileNameParts[fileNameParts.length-1])) {
			return true;
		}
		return false;
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
		//impl
		Process process = Runtime.getRuntime().exec(command(paper));
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String s;
		while((s = stdInput.readLine()) != null)
		return null;
	}
	
	private String command(File file) throws ConversionToPDFException {
		String latex = callLatexExecuter();
		String includeDirParameter = "-include-directory=\"";
		String rootDir = getActualTargetDirSubDirPath();
		String outputDirParameter = "\" -output-directory=\"";
		setGeneratedDir(file);
		String outputDir = getActualGeneratedDirSubDirPath();
		String mainFile = selectMainFile(file);
		String fullCommand = latex + includeDirParameter + rootDir + outputDirParameter + outputDir + mainFile;
		
		return fullCommand;
	}
	
	private String callLatexExecuter() {
		String latexExecuter = "pdflatex" + " ";
		return latexExecuter;
	}
	
	private void setGeneratedDir(File file) {
		if(generatedDirIsExists(file)) {
			createNewSubDirForGeneratedDir(file);
		}
		createGeneratedDir(file);
	}
	
	private boolean generatedDirIsExists(File file) {
		File generatedDir = new File(file.getParent() + "\\generatedDir");
		if(generatedDir.exists()) {
			return true;
		}
		return false;
	}
	
	private void createNewSubDirForGeneratedDir(File file) {
		File generatedDir = new File(file.getParent() + "\\generatedDir");
		String[] subDirs = generatedDir.list();
		String newSubDir = generatedDir.getPath() + "\\version_" + subDirs.length;
		new File(newSubDir).mkdir();
		setActualGeneratedDirSubDirPath(newSubDir);
	}
	
	private void createGeneratedDir(File file) {
		String generatedDirWithFirstSubDir = file.getParent() + "\\generatedDir" + "\\version_0";
		new File(generatedDirWithFirstSubDir).mkdir();
		setActualGeneratedDirSubDirPath(generatedDirWithFirstSubDir);
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
