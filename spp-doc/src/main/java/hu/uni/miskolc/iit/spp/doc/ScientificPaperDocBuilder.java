package hu.uni.miskolc.iit.spp.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.ssp.doc.convert.Doc2PDFConverter;

public class ScientificPaperDocBuilder extends AbstractScientificPaperBuilder {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	
	private static Logger LOG = LogManager.getLogger(ScientificPaperDocBuilder.class);
	
	private Doc2PDFConverter converter;
	private HWPFDocument document;
	private WordExtractor extractor;
	
	public ScientificPaperDocBuilder(Doc2PDFConverter converter) {
		this.converter = converter;
	}
	
	@Override
	protected void checkFileExtension(File paper) throws NoMainDocumentFoundException, IOException {
		if(!isDocFile(paper)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: The file's extension is not .doc: " + paper.getName());
			throw new NoMainDocumentFoundException("The file's extension is not .doc: " + paper.getName());
		}
		
		initFields(paper);
	}

	private void initFields(File docFile) throws IOException {
		try {
			document = new HWPFDocument(new FileInputStream(docFile));
			extractor = new WordExtractor(document);
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw forward with the same message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	private boolean isDocFile(File paper) {
		String extension = FilenameUtils.getExtension(paper.getName());

		return extension.equals(SupportedCompileableFileExtensions.DOC.getStringValue());
	}

	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException {
		try {
			File destinationDir = initDestinationDir(paper);
			File pdfFile = converter.generatePDF(paper, destinationDir);
			
			return pdfFile;
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw ConversionToPDFException with the same message: " + System.lineSeparator() + e.getMessage());
			throw new ConversionToPDFException(e.getMessage());
		}
		
	}

	private File initDestinationDir(File rootFile) throws IOException {
		File directory = new File(rootFile.getParentFile().getAbsolutePath() + FILE_SEPARATOR + DEST_DIR_NAME);
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

	@Override
	protected String extractTitle(File paper) throws IOException {
		String title = getTitle(document);
		
		if(title.isEmpty()) {
			LOG.warn("ExtractTitle method return with empty string.");
		}
		
		return title;
	}

	private String getTitle(HWPFDocument document) {
		Range range = document.getRange();
		double avgFontSize = calcAvgFontSize(range);
		String title = "";
		boolean finished = false;
		
		for(int i = 0; i < range.numParagraphs() && !finished; i++) {
			Paragraph paragraph = range.getParagraph(i);
			for(int j = 0; j < paragraph.numCharacterRuns(); j++) {
				CharacterRun run = paragraph.getCharacterRun(j);
				if(isTitle(run, avgFontSize)) {
					title = paragraph.text();
					finished = true;
					
					break;
				}
			}
		}
		
		return title;
	}

	private boolean isTitle(CharacterRun run, double avgFontSize) {
		
		return run.getFontSize() > avgFontSize && run.isBold();
	}

	private double calcAvgFontSize(Range range) {
		int paragraphLimit = setParagraphLimit(range);
		double fontSize = 0;
		double charsNumber = 0;
		
		for(int i = 0; i < paragraphLimit; i++) {
			Paragraph paragraph = range.getParagraph(i);
			for(int j = 0; j < paragraph.numCharacterRuns(); j++) {
				CharacterRun run = paragraph.getCharacterRun(j);
				fontSize = fontSize + run.getFontSize();
				charsNumber++;
			}
		}
		
		return fontSize / charsNumber;
	}

	private int setParagraphLimit(Range range) {
		if(range.numParagraphs() <= 10) {
			
			return range.numParagraphs();
		}
		
		return 10;
	}

	@Override
	protected String extractAbstract(File paper) {
		String resume = getAbstract(document);
		
		if(resume.isEmpty()) {
			LOG.warn("ExtractAbstract method return with empty string.");
		}
		
		return resume;
	}

	private String getAbstract(HWPFDocument document) {
		Range range = document.getRange();
		String title = getTitle(document);
		String resume = "";
		boolean finished = false;
		
		for(int i = 0; i < range.numParagraphs() && !finished; i++) {
			Paragraph paragraph = range.getParagraph(i);
			for(int j = 0; j < paragraph.numCharacterRuns(); j++) {
				CharacterRun run = paragraph.getCharacterRun(j);
				if(!isAbstract(run)) {
					continue;
				}
				if(paragraph.text().equals(title)) {
					continue;
				}
				finished = true;
				resume = paragraph.text(); 
				
				break;
			}
		}
		
		return replaceManualLineBreak(resume, System.lineSeparator());
	}

	private String replaceManualLineBreak(String text, String newCharacter) {
		
		return text.replaceAll("[^a-zA-Z0-9 ,.?!\":-@-()%]", newCharacter);
	}

	private boolean isAbstract(CharacterRun run) {
		 
		return run.text().toLowerCase().contains("abstract") && run.isBold();
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		List<String> keywords = getKeywords(extractor);
		
		if(keywords.isEmpty()) {
			LOG.warn("ExtractKeywords method return with empty list.");
		}
		
		return keywords;
	}

	private List<String> getKeywords(WordExtractor extractor) {
		String[] paragraphsText = extractor.getParagraphText();
		List<String> keywords = new ArrayList<String>();
		
		for(String paragraphText : paragraphsText) {
			if(!containsKeywords(paragraphText)) {
				continue;
			}
			keywords = Arrays.asList(paragraphText.split(","));
			
			break;
		}
		
		return removeUnwantedText(keywords);
	}

	private List<String> removeUnwantedText(List<String> keywords) {
		for(String keyword : keywords) {
			if(!containsKeywords(keyword)) {
				continue;
			}
			int index = keywords.indexOf(keyword);
			keyword = keyword.replace("keywords:", "");
			keyword = keyword.replace("index terms:", "");
			keyword = keyword.substring(1);
			keywords.set(index, keyword);
		
			break;
		}
		
		return keywords;
	}

	private boolean containsKeywords(String paragraphText) {
		
		return paragraphText.toLowerCase().contains("keywords") || paragraphText.toLowerCase().contains("index terms");
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		List<Author> authors = getAuthors(document);
		
		if(authors.isEmpty()) {
			LOG.warn("ExtractTitle method return with empty list.");
		}
		
		return authors;
	}

	private List<Author> getAuthors(HWPFDocument document) {
		Range range = document.getRange();
		String authorParagraphText = getAuthorParagraphText(range);
		String[] cleanedTextArray = cleanTextArray(authorParagraphText);
		List<Author> authors = createAuthors(cleanedTextArray);
		
		return authors;
	}

	private List<Author> createAuthors(String[] cleanedTextArray) {
		String[] names = cleanedTextArray[0].split(",");
		String[] emails = new String[names.length];
		String[] affils = new String[names.length];
		
		for(int i = 1; i < cleanedTextArray.length; i++) {
			String[] emailsAndAffils = cleanedTextArray[i].split(",");
			emails[i-1] = emailsAndAffils[0];
			for(int j = 1; j < emailsAndAffils.length; j++) {
				if(affils[i-1] == null) {
					affils[i-1] = emailsAndAffils[j];
				} else {
					affils[i-1] = affils[i-1] + "," + emailsAndAffils[j];
				}
			}
		}
		
		List<Author> authors = new ArrayList<Author>();
		if(names[0].length() > 0) {
			for(int i = 0; i < names.length; i++) {
				authors.add(new Author(
						removeNeedlessSpaces(names[i]), 
						removeNeedlessSpaces(emails[i]), 
						removeNeedlessSpaces(affils[i])));
			}
		}
		
		return authors;
	}

	private String[] cleanTextArray(String authorParagraphText) {
		authorParagraphText = replaceManualLineBreak(authorParagraphText, "");
		authorParagraphText = authorParagraphText.replaceAll("HYPERLINK", System.lineSeparator());
		
		List<String> textList = Arrays.asList(authorParagraphText.split(System.lineSeparator()));
		for(String text : textList) {
			if(!text.contains("@")) {
				continue;
			}
			if(!text.contains("\"mailto:")) {
				continue;
			}
			int index = textList.indexOf(text);
			int beginIndex = text.indexOf("\"mailto:");
			int lastIndex = text.indexOf("\"", beginIndex + 1);
			text = text.substring(lastIndex + 1, text.length());
			textList.set(index, text);
		}
		
		return textList.toArray(new String[textList.size()]);
	}

	private String getAuthorParagraphText(Range range) {
		String authorParagraphText = "";
		
		for(int i = 0; i < range.numParagraphs(); i++) {
			Paragraph paragraph = range.getParagraph(i);
			if(!paragraph.text().contains("@")) {
				continue;
			}
			authorParagraphText = paragraph.text();
			break;
		}
		
		return authorParagraphText;
	}
	
	private String removeNeedlessSpaces(String string) {
		if (containsNeedlessSpaces(string)) {

			return removeNeedlessSpaces(removeSpaces(string));
		}

		return string;
	}
	
	private boolean containsNeedlessSpaces(String string) {

		return containsNeedlesStartSpace(string) || containsNeedlessEndSpace(string);
	}
	
	private boolean containsNeedlesStartSpace(String string) {

		return string.startsWith(" ");
	}

	private boolean containsNeedlessEndSpace(String string) {

		return string.endsWith(" ");
	}
	
	private String removeSpaces(String string) {
		if (containsNeedlesStartSpace(string)) {
			string = string.replaceFirst(" ", "");
		}
		if (containsNeedlessEndSpace(string)) {
			string = string.substring(0, string.length() - 1);
		}

		return string;
	}
}