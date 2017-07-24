package hu.uni.miskolc.iit.spp.latex;

import java.io.*;
import java.util.*;

import com.sun.org.apache.xerces.internal.xs.StringList;
import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	private static Logger LOG = LogManager.getLogger(ScientificPaperLatexBuilder.class);
	private Latex2PDFCompiler compiler;
	private StringBuilder completeTexFile;
	private File destinationDir;

	public ScientificPaperLatexBuilder(Latex2PDFCompiler compiler) {
		this.compiler = compiler;
	}

	@Override
	protected void checkFileExtension(File paper) throws NoMainDocumentFoundException, IOException {
		if(!containsTexFile(paper)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: Could not find main.tex or paper.tex file this directory: " + paper.getAbsolutePath());
			throw new NoMainDocumentFoundException("Could not find main.tex or paper.tex file this directory: " + paper.getAbsolutePath());
		}
	}
	
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException {
		try {
			destinationDir = initDestinationDir(paper);
			File mainTexFile = findTexFile(paper, SupportedFileNames.getStringValues());
			File pdfFile = this.compiler.generatePDFFile(mainTexFile, destinationDir);

			return pdfFile;

		} catch(IOException e) {
			LOG.fatal("Catch IOException this message: " + e.getMessage() + System.lineSeparator() + "And throw ConversionToPDFException with the same message.");
			throw new ConversionToPDFException(e.getMessage());

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException this message: " + e.getMessage() + System.lineSeparator() + "And throw ConversionToPDFException with the same message.");
			throw new ConversionToPDFException(e.getMessage());
		}
	}

	private StringBuilder patchTexFile(File directory, File texFile) throws IOException {
		try {
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(texFile));
			String line = reader.readLine();
			while(line != null) {
				if(line.contains(LatexArgs.INPUT.getArgument())) {
					builder.append(line).append(System.lineSeparator());
					File wantedTexFile = findTexFile(directory, extractFileName(line));
					builder.append(patchTexFile(directory, wantedTexFile));
				}
				builder.append(line).append(System.lineSeparator());
				line = reader.readLine();
			}

			return builder;

		} catch(IOException e) {
			LOG.fatal("Catch IOException this message: " + e.getMessage() + System.lineSeparator() + "Throw new IOException with the same message.");
			throw new IOException(e.getMessage());

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw IOException this message: " + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	@Override
	protected String extractTitle(File paper) throws IOException {
		try {
			File mainTexFile = findTexFile(paper, SupportedFileNames.getStringValues());
			completeTexFile = patchTexFile(paper, mainTexFile);

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw IOException this message: " + e.getMessage());
			throw new IOException(e.getMessage());

		} catch(IOException e) {
			LOG.fatal("Catch IOException this message: " + e.getMessage() + System.lineSeparator() + "And trow IOexception with the same message.");
			throw new IOException(e.getMessage());
		}

		String title = "";
		if(!completeTexFile.toString().contains(LatexArgs.TITLE.getArgument())) {

			LOG.warn("ExtractTitle method return with empty string.");
			return title;
		}

		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());
		for(String line : SBLines) {
			if(line.contains(LatexArgs.TITLE.getArgument())) {
				int beginIndex = line.indexOf(LatexArgs.TITLE.getArgument());
				int endIndex = line.indexOf("}");
				String roughTitle = line.substring(beginIndex + 1, endIndex);
				title = roughTitle;
				break;
			}
		}

//remove latex commands if contains
		return title;
	}

	@Override
	protected String extractAbstract(File paper) {
		String resume = "";
		if(!completeTexFile.toString().contains(LatexArgs.START_ABSTRACT.getArgument())) {

			LOG.warn("ExtractAbstract method return with empty string.");
			return resume;
		}

		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());
		int beginIndex = 0;
		int endIndex = 0;
		for(int i = 0; i < SBLines.length; i++) {
			if(SBLines[i].equals(LatexArgs.START_ABSTRACT.getArgument())) {
				beginIndex = i;
			}
			if(SBLines[i].equals(LatexArgs.END_ABSTRACT.getArgument())) {
				endIndex = i;
				break;
			}
		}
		StringBuilder builder = new StringBuilder();
		for(int i = beginIndex; i <= endIndex; i++) {
			builder.append(SBLines[i]);
		}
		String roughResume = builder.toString();

//remove latex commands if contains
		return resume = roughResume;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		List keywords = new ArrayList<String>();
		if(!completeTexFile.toString().contains(LatexArgs.START_KEYWORD.getArgument())) {
			if(!completeTexFile.toString().contains(LatexArgs.KEYWORDS.getArgument())) {

				LOG.warn("ExtractKeywords method return with empty list.");
				return keywords;
			}
		}

		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());
		String keywordsLine = "";
		int beginIndex = 0;
		int lastIndex = 0;
		for(int i = 0; i < SBLines.length; i++) {
			if(SBLines[i].contains(LatexArgs.KEYWORDS.getArgument())) {
				keywordsLine = SBLines[i];
				break;
			}
			if(SBLines[i].contains(LatexArgs.START_KEYWORD.getArgument())) {
				beginIndex = i;
			}
			if(SBLines[i].contains(LatexArgs.END_KEYWORD.getArgument())) {
				lastIndex = i;
				break;
			}
		}
		String[] roughKeywords;
		if(!keywordsLine.isEmpty()) {
			roughKeywords = keywordsLine.split(",");
			keywords = Arrays.asList(roughKeywords);
		}
		if(beginIndex != lastIndex) {
			roughKeywords = Arrays.copyOfRange(SBLines, beginIndex + 1, lastIndex);
			keywords = Arrays.asList(roughKeywords);
		}

//remove latex commands if contains
		return keywords;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		List authors = new ArrayList<Author>();
		if(!completeTexFile.toString().contains(LatexArgs.AUTHOR.getArgument())) {

			LOG.warn("ExtractAuthors method return with empty list.");
			return authors;
		}
		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());


		Map<String, String> nameWithPointer = new HashMap<>();
		Map<String, String> nameWithEMail = new HashMap<>();
		Map<String, String> pointerWithAffiliation = new HashMap<>();
		for(String line : SBLines) {
			if(line.contains(LatexArgs.AUTHOR.getArgument())) {
				int beginIndex = line.indexOf("[");
				int lastIndex = line.indexOf("]");
				String pointer = line.substring(beginIndex + 1, lastIndex);

				beginIndex = line.indexOf("{");
				lastIndex = line.indexOf("}");
				String name = line.substring(beginIndex + 1, lastIndex);
				nameWithPointer.put(name, pointer);

				if(line.contains(LatexArgs.EAD_COMMAND.getArgument())) {
					beginIndex = line.indexOf(LatexArgs.EAD_COMMAND.getArgument());
				}
				if(line.contains(LatexArgs.HREF_COMMAND.getArgument())) {
					beginIndex = line.indexOf(LatexArgs.HREF_COMMAND.getArgument());
				}
				String email = line.substring(line.indexOf("{", beginIndex) + 1, line.indexOf("}", beginIndex));
				nameWithEMail.put(name, email);
			}
			if(line.contains(LatexArgs.AFFILIATION.getArgument())) {
				int beginIndex = line.indexOf("[");
				int lastIndex = line.indexOf("]");
				String pointer = line.substring(beginIndex + 1, lastIndex);

				beginIndex = line.indexOf("{");
				lastIndex = line.indexOf("}");
				String affiliation = line.substring(beginIndex + 1, lastIndex);

				pointerWithAffiliation.put(pointer, affiliation);
			}
		}
		for(String key : nameWithEMail.keySet()) {
			String name = key;
			String email = nameWithEMail.get(key);
			String affiliation = pointerWithAffiliation.get(nameWithPointer.get(key));
			Author author = new Author(name, email, affiliation);
			authors.add(author);
		}

//remove latex commands if contains
		return authors;
	}

	private boolean containsTexFile(File directory) {
		try {
			findTexFile(directory, SupportedFileNames.getStringValues());
			return true;

		} catch (SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and return with false.");
			return false;
		}
	}

	private String extractFileName(String text) {
		int beginIndex = text.indexOf("{");
		int endIndex = text.indexOf("}");
		String[] textParts = text.substring(beginIndex + 1, endIndex).split("/");
		String fileName = textParts[textParts.length - 1];

		return fileName;
	}

	private File findTexFile(File directory, String name) throws SearchedFileNotExistsException {
		String[] singletonNames = new String[1];
		singletonNames[0] = name;

		return findTexFile(directory, singletonNames);
	}

	private File findTexFile(File directory, String[] names) throws SearchedFileNotExistsException {
		File[] dirFiles = directory.listFiles();
		if(dirFiles != null && dirFiles.length > 0) {
			for(File file : dirFiles) {
				if(file.isDirectory()) {
					return findTexFile(file, names);
				}
				if(isSearchedName(file, names) && isTexFile(file)) {
					return file;
				}
			}
		}
		LOG.fatal("Throw SearchedFileNotExistsException this message: Could not find .tex file with this name(s).");
		throw new SearchedFileNotExistsException("Could not find .tex file with this name(s).");
	}

	private boolean isSearchedName(File file, String[] names) {
		String fileName = FilenameUtils.getBaseName(file.getName()).toLowerCase();
		for(String name : names) {
			if(fileName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	private boolean isTexFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileExtension.equals(SupportedCompileableTextFileExtensions.TEX.getStringValue());
	}

	private File initDestinationDir(File rootFile) throws IOException {
		File directory = new File(rootFile.getParentFile().getParentFile().getAbsolutePath() + FILE_SEPARATOR + DEST_DIR_NAME);
		if(!directory.exists()) {
			if(directory.mkdir()) {
				LOG.fatal("Throw IOException this message: Could not create directory: " + directory.getAbsolutePath());
				throw new IOException("Could not create directory: " + directory.getAbsolutePath());
			}
		}
		int versionNo = 0;
		while(new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + versionNo).exists() == true) {
			versionNo++;
		}
		File destDir = new File(directory.getAbsolutePath() + FILE_SEPARATOR + SUB_DIR_NAME + versionNo);
		if(!destDir.mkdir()) {
			LOG.fatal("Throw IOException this message: Could not create directory: " + destDir.getAbsolutePath());
			throw new IOException("Could not create directory: " + destDir.getAbsolutePath());
		}

		return destDir;
	}
}