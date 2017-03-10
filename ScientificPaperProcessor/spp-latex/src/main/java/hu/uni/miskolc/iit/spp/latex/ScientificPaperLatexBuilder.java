package hu.uni.miskolc.iit.spp.latex;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {

	private Collection<String> possibleFileExtension;
	private Collection<String> possibleArchiveExtension;
	private Collection<String> possibleMainFiles;
	 
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
		File dir = new File(unzip(paper));
		File[] listOfFiles = dir.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleFileExtension)) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException();
	}
	
	private String unzip(File zipFile) throws NotSupportedFileExtensionException {
		String destinationDir = null;
		
		try {
			ZipFile validZipFile = new ZipFile(checkArchiveExtension(zipFile));
			destinationDir = setTargetDir(zipFile);
			validZipFile.extractAll(destinationDir);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		return destinationDir;
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
			return createNewSubDir(file);
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
	
	private String createNewSubDir(File file) {
		File targetDir = new File(file.getParent() + "\\targetDir");
		String[] SubDirs = targetDir.list();
		String newSubDir = targetDir.getPath() + "\\version_" + SubDirs.length;
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
	protected File generatePDF(File paper) {
		//impl
		String executeSystem = "cmd /c ";
		String executeProcess = "pdflatex ";
		String rootDirCommand = "-synctex=1 -interaction=nonstopmode -include-directory=\"";
		/*
		String documentRoot = ;
		String fullCommand;
		*/
		return null;
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
}
