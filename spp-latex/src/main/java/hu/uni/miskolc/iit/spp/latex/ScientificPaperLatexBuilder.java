package hu.uni.miskolc.iit.spp.latex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;

public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	
	private static Logger LOG = LogManager.getLogger(ScientificPaperLatexBuilder.class);
	
	private Latex2PDFCompiler compiler;
	private StringBuilder completeTexFile;

	public ScientificPaperLatexBuilder(Latex2PDFCompiler compiler) {
		this.compiler = compiler;
	}

	@Override
	protected void checkFileExtension(File paper) throws NoMainDocumentFoundException {
		if(!containsTexFile(paper)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: Could not find main.tex or paper.tex file this directory: " + paper.getAbsolutePath());
			throw new NoMainDocumentFoundException("Could not find main.tex or paper.tex file this directory: " + paper.getAbsolutePath());
		}
	}
	
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException {
		try {
			File destinationDir = initDestinationDir(paper);
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

	@Override
	protected String extractTitle(File paper) throws IOException {
		try {		
			File mainTexFile = findTexFile(paper, SupportedFileNames.getStringValues());			
			completeTexFile = patchTexFile(paper, mainTexFile);
			
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

			return title;

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw IOException this message: " + e.getMessage());
			throw new IOException(e.getMessage());

		} catch(IOException e) {
			LOG.fatal("Catch IOException this message: " + e.getMessage() + System.lineSeparator() + "And throw IOexception with the same message.");
			throw new IOException(e.getMessage());
		}		
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

		return resume = roughResume;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		List<String> keywords = new ArrayList<String>();
		if(!completeTexFile.toString().contains(LatexArgs.KEYWORDS.getArgument())) {
			LOG.warn("ExtractKeywords method return with empty list.");

			return keywords;
		}

		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());
		String keywordsLine = "";
		for(String line : SBLines) {
			if(line.contains(LatexArgs.KEYWORDS.getArgument())) {
				keywordsLine = line;
				break;
			}
		}
		if(!keywordsLine.contains(",")) {
			String roughKeywords = keywordsLine;
			keywords.add(roughKeywords);
		}
		else {
			String[] roughKeywords = keywordsLine.split(",");
			keywords = Arrays.asList(roughKeywords);
		}
		
		return keywords;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		List<Author> authors = new ArrayList<Author>();
		if(!completeTexFile.toString().contains(LatexArgs.AUTHOR.getArgument())) {
			LOG.warn("ExtractAuthors method return with empty list.");

			return authors;
		}
		String[] SBLines = completeTexFile.toString().split(System.lineSeparator());

		int counter = 0;
		boolean moreAuthor = false;
		for(String line : SBLines) {
			if(line.contains(LatexArgs.AUTHOR.getArgument())) {
				counter++;
				if(counter == 2) {
					moreAuthor = true;
					break;
				}
			}
		}
		Map<String, String> nameWithPointer = new HashMap<>();
		Map<String, String> nameWithEmail = new HashMap<>();
		Map<String, String> pointerWithAffiliation = new HashMap<>();
		if(moreAuthor) {
			for(String line : SBLines) {
				if(line.contains(LatexArgs.AUTHOR.getArgument())) {
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String pointer = line.substring(beginIndex + 1, endIndex);

					beginIndex = line.indexOf(LatexArgs.HREF.getArgument());
					endIndex = line.lastIndexOf("}");
					String emailAndName = line.substring(beginIndex + 5, endIndex);

					beginIndex = emailAndName.indexOf("{");
					endIndex = emailAndName.indexOf("}");
					String email = emailAndName.substring(beginIndex + 1, endIndex);

					for(int i = emailAndName.lastIndexOf("}"); i > 0; i--) {
						Character character = emailAndName.charAt(i);
						if(character.toString().equals("{")) {
							beginIndex = i;
							break;
						}
					}
					endIndex = emailAndName.lastIndexOf("}");
					String name = emailAndName.substring(beginIndex +1, endIndex);

					nameWithPointer.put(name, pointer);
					nameWithEmail.put(name, email);
				}
				if(line.contains(LatexArgs.AFFILIATION.getArgument())) {
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String pointer = line.substring(beginIndex + 1, endIndex);

					beginIndex = line.indexOf("{");
					endIndex = line.indexOf("}");
					String affiliation = line.substring(beginIndex + 1, endIndex);

					pointerWithAffiliation.put(pointer, affiliation);
				}
			}
			for(String key : nameWithEmail.keySet()) {
				String name = key;
				String email = nameWithEmail.get(key);
				String affiliation = pointerWithAffiliation.get(nameWithPointer.get(key));
				Author author = new Author(name, email, affiliation);
				authors.add(author);
			}
		}
		else {
			Author author = new Author("","","");
			for(String line : SBLines) {
				if(line.contains(LatexArgs.AUTHOR.getArgument())) {
					int beginIndex = line.indexOf(LatexArgs.HREF.getArgument());
					int endIndex = line.lastIndexOf("}");
					String emailAndName = line.substring(beginIndex + 5, endIndex);

					beginIndex = emailAndName.indexOf("{");
					endIndex = emailAndName.indexOf("}");
					String email = emailAndName.substring(beginIndex + 1, endIndex);
					author.setEmail(email);

					for(int i = emailAndName.lastIndexOf("}"); i > 0; i--) {
						Character character = emailAndName.charAt(i);
						if(character.toString().equals("{")) {
							beginIndex = i;
							break;
						}
					}
					endIndex = emailAndName.lastIndexOf("}");
					String name = emailAndName.substring(beginIndex + 1, endIndex);
					author.setName(name);
				}
				if(line.contains(LatexArgs.AFFILIATION.getArgument())) {
					int beginIndex = line.indexOf("{");
					int endIndex = line.indexOf("}");
					String affiliation = line.substring(beginIndex + 1, endIndex);
					author.setAffiliation(affiliation);
					break;
				}
			}
			authors.add(author);
		}

		return authors;
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
			reader.close();
			
			return builder;

		} catch(IOException e) {
			LOG.fatal("Catch IOException this message: " + e.getMessage() + System.lineSeparator() + "Throw new IOException with the same message.");
			throw new IOException(e.getMessage());

		} catch(SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw IOException this message: " + e.getMessage());
			throw new IOException(e.getMessage());
		}
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
		String[] singletonName = new String[1];
		singletonName[0] = name;

		return findTexFile(directory, singletonName);
	}
	
	private File findTexFile(File directory, String[] names) throws SearchedFileNotExistsException {
		try {		
			String extension = "." + SupportedCompileableFileExtensions.TEX.getStringValue();
			Stream<Path> pathStream = Files.find(directory.toPath(), Integer.MAX_VALUE, 
					(path, attrs) -> attrs.isRegularFile() && path.toString().endsWith(extension), 
					FileVisitOption.FOLLOW_LINKS);
	
			File wantedFile = new File("");			
			boolean founded = false;
			List<Object> pathList = Arrays.asList(pathStream.toArray());
			for(Object object : pathList) {				
				if(founded) {
					break;
				}
				Path path = (Path) object;
				for(String name : names) {
					if(!path.toString().endsWith(name + extension)) {
						continue;
					}
					founded = true;
					wantedFile = path.toFile();
					break;
				}
			}
			pathStream.close();

			if(!wantedFile.exists()) {
				throw new SearchedFileNotExistsException("Could not find .tex file with this name(s): " + Arrays.toString(names));
			}
			
			return wantedFile;
		
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw SearchedFileNotExistsException with same message: " + e.getMessage());
			throw new SearchedFileNotExistsException(e.getMessage());
		}
	}

	private File initDestinationDir(File rootFile) throws IOException {
		File directory = new File(rootFile.getParentFile().getParentFile().getAbsolutePath() + FILE_SEPARATOR + DEST_DIR_NAME);
		if(!directory.exists()) {
			if(!directory.mkdir()) {
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