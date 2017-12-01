package hu.uni.miskolc.iit.spp.docx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.docx.convert.Docx2PDFConverter;

public class ScientificPaperDocxBuilder extends AbstractScientificPaperBuilder {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEST_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	private static final String SUB_DIR_NAME = "version_";
	
	private static Logger LOG = LogManager.getLogger(ScientificPaperDocxBuilder.class);
	
	private Docx2PDFConverter converter;
	private List<XWPFParagraph> docxParagraphs;
	
	public ScientificPaperDocxBuilder(Docx2PDFConverter converter) {
		this.converter = converter;
	}
	
	@Override
	protected void checkFileExtension(File paper) throws NoMainDocumentFoundException {
		if(!isDocxFile(paper)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: The file's extension is not .docx: " + paper.getName());
			throw new NoMainDocumentFoundException("The file's extension is not .docx: " + paper.getName());
		}
	}

	private boolean isDocxFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());
		
		return fileExtension.equals(SupportedCompileableFileExtensions.DOCX.getStringValue());
	}

	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException {
		try {
			File destinationDir = initDestinationDir(paper);
			File pdfFile = converter.generatePDF(destinationDir);
			
			return pdfFile;
			
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw ConversionToPDFException with the same message: " + System.lineSeparator() + e.getMessage());
			throw new ConversionToPDFException(e.getMessage());
		} catch (SearchedFileNotExistsException e) {
			LOG.fatal("Catch SearchedFileNotExistsException and throw ConversionToPDFException with the same message: " + System.lineSeparator() + e.getMessage());
			throw new ConversionToPDFException(e.getMessage());
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

	@Override
	protected String extractTitle(File paper) throws IOException {
		docxParagraphs = patchDocxParagraphs(paper);
		String title = getTitle(docxParagraphs);
		
		if(title.isEmpty()) {
			LOG.warn("ExtractTitle method return with empty string.");
		}
		
		return title;
	}
	
	private String getTitle(List<XWPFParagraph> paragraphs) {
		for(XWPFParagraph paragraph : paragraphs) {
			if(!paragraph.getAlignment().equals(ParagraphAlignment.CENTER)) {
				continue;
			}
			for(XWPFRun run : paragraph.getRuns()) {
				if(run.isBold()) {
					
					return paragraph.getText();
				}
			}
		}
		
		return "";
	}
	
	private List<XWPFParagraph> patchDocxParagraphs(File docxFile) throws IOException {
		XWPFDocument document = new XWPFDocument(new FileInputStream(docxFile));
		List<XWPFParagraph> listOfParagraphs = document.getParagraphs();
		
		return listOfParagraphs;
	}

	@Override
	protected String extractAbstract(File paper) {
		String resume = getAbstract(docxParagraphs);
		
		if(resume.isEmpty()) {
			LOG.warn("ExtractAbstract method return with empty string.");
		}
		
		return resume;
	}

	private String getAbstract(List<XWPFParagraph> paragraphs) {
		for(XWPFParagraph paragraph : paragraphs) {
			if(!paragraph.getText().toLowerCase().contains("abstract")) {
				continue;
			}
			if(paragraph.getText().equals(getTitle(paragraphs))) {
				continue;
			}
			for(XWPFRun run : paragraph.getRuns()) {
				if(run.isBold()) {
					
					return paragraph.getText();
				}
			}
		}
		
		return "";
	}
	

	@Override
	protected List<String> extractKeywords(File paper) {
		List<String> keywords = getKeywords(docxParagraphs);
		
		if(keywords.isEmpty()) {
			LOG.warn("ExtractKeywords method return with empty list.");
		}
		
		return keywords;
	}

	private List<String> getKeywords(List<XWPFParagraph> paragraphs) {
		List<String> keywords = new ArrayList<String>();
		
		for(XWPFParagraph paragraph : paragraphs) {
			if(paragraph.getText().toLowerCase().contains("keywords") ||
				paragraph.getText().toLowerCase().contains("index terms")) {
				keywords = Arrays.asList(paragraph.getText().split(","));
				
				break;
			}
		}
		
		for(String word : keywords) {
			if(word.contains("keywords") || word.contains("index terms")) {
				word.replace("keywords", "");
				word.replace("index terms", "");
				word = word.substring(2);
				
				break;
			}
		}
		
		return keywords;
	}
	

	@Override
	protected List<Author> extractAuthors(File paper) throws IOException {
		List<Author> authors = getAuthors(docxParagraphs);
			
		if(authors.isEmpty()) {
			LOG.warn("ExtractTitle method return with empty list.");
		}
		
		return authors;
	}

	private List<Author> getAuthors(List<XWPFParagraph> paragraphs) throws IOException {
		XWPFParagraph authorParaghraph = findAuthorParagraph(paragraphs);
		
		List<Author> authorsList = new ArrayList<Author>();
		if(authorParaghraph != null) {
			if(moreAuthor(authorParaghraph)) {
				authorsList = getAllAuthors(authorParaghraph);
			
			} else {
				authorsList.add(getSingleAuthor(authorParaghraph));
			}
		}
			
		return authorsList;
	}

	private XWPFParagraph findAuthorParagraph(List<XWPFParagraph> paragraphs) {
		for(XWPFParagraph paragraph : paragraphs) {
			if(paragraph.getText().contains("@")) {
				
				return paragraph;
			}
		}
		
		return null;
	}

	private List<Author> getAllAuthors(XWPFParagraph authorParaghraph) throws IOException {
		List<String> authorsData = getAuthorData(authorParaghraph);
		String[] names = authorsData.get(0).split(",");
		List<Author> authors = new ArrayList<Author>();
		for(int i = 0; i < names.length; i++) {
			String[] emailAndAffil = authorsData.get(i + 1).split(",");
			
			String name = names[i];
			String email = emailAndAffil[0];
			String affiliation = emailAndAffil[1] + "," + emailAndAffil[2];
			
			authors.add(new Author(name, email, affiliation));
		}
		
		return authors;
	}

	private Author getSingleAuthor(XWPFParagraph authorParaghraph) throws IOException {
		List<String> authorsData = getAuthorData(authorParaghraph);
		String[] emailAndAffil = authorsData.get(1).split(",");
		
		String name = authorsData.get(0);
		String email = emailAndAffil[0];
		String affiliation = emailAndAffil[1] + "," + emailAndAffil[2];
		
		return new Author(name, email, affiliation);
	}

	private List<String> getAuthorData(XWPFParagraph authorParaghraph) throws IOException {
		File tempFile = createTempFile();
		writeToFile(tempFile, authorParaghraph.getText());
		List<String> authorData = readFromFile(tempFile);
		deleteTempFile(tempFile);
		
		return authorData;
	}

	private void deleteTempFile(File tempFile) {
		tempFile.delete();
	}

	private List<String> readFromFile(File tempFile) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(tempFile));
			List<String> dataFromFile = new ArrayList<String>();
			String line;
			while((line = reader.readLine()) != null) {
				dataFromFile.add(line);
			}
			
			return dataFromFile;
		
		} catch(FileNotFoundException e) {
			LOG.fatal("Catch FileNotFoundException and throw IOException with the same message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		} catch(IOException e) {
			LOG.fatal("Catch IOException and throw forward with this message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	private void writeToFile(File file, String paragraphText) throws IOException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(paragraphText);
			writer.close();
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw forward with this message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	private File createTempFile() throws IOException {
		try {
			File tempFile = new File("temp.txt");
			tempFile.createNewFile();
		
			return tempFile;
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw forward with this message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	private boolean moreAuthor(XWPFParagraph authorParaghraph) {
		String authorParaghraphText = authorParaghraph.getText();
		int firstAtPosition = authorParaghraphText.indexOf("@");
		int lastAtPosition = authorParaghraphText.lastIndexOf("@");
		
		return firstAtPosition != lastAtPosition;
	}
}