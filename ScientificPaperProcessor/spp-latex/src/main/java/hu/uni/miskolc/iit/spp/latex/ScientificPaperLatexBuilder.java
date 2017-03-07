package hu.uni.miskolc.iit.spp.latex;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.core.service.FileUnzip;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder implements FileUnzip {

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
		unzip(paper);
		File dir = new File(paper.getParent());
		File[] listOfFiles = dir.listFiles();
		for(File file : listOfFiles) {
			if(extensionTest(file, possibleFileExtension)) {
				return;
			}
		}
		throw new NotSupportedFileExtensionException();
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

	@Override
	public void unzip(File zipFile) throws NotSupportedFileExtensionException {
		try {
			ZipFile validZipFile = new ZipFile(checkArchiveExtension(zipFile));
			validZipFile.extractAll(zipFile.getParent());
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	@Override
	public File checkArchiveExtension(File zipFile) throws NotSupportedFileExtensionException {
		if(extensionTest(zipFile, possibleArchiveExtension)) {
			return zipFile;
		} else {
			throw new NotSupportedFileExtensionException();
		}
	}
	
	private boolean extensionTest(File file, Collection<String> possibleExtension) {
		String fileName = file.getName();
		String[] fileNameParts = fileName.split("\\.");
		if(possibleExtension.contains(fileNameParts[fileNameParts.length-1])) {
			return true;
		}
		return false;
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
