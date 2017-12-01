package hu.uni.miskolc.iit.ssp.doc.convert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedApplicationException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class DocConverterFactory {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator"); 
	
	private static Logger LOG = LogManager.getLogger(DocConverterFactory.class);
	
	private String osName;

	public DocConverterFactory(String osName) {
		this.osName = osName;
	}

	public Doc2PDFConverter createDocPDFConverter(File docFile) throws NotSupportedOperationSystemException, NotSupportedFileExtensionException, NotSupportedApplicationException, IOException {
		if(!isSupportedOS()) {
			LOG.fatal("Throw NotSupportedOperationSystemException this message: Could not create compiler, because this operation system is not supported.");
			throw new NotSupportedOperationSystemException("Could not create compiler, because this operation system is not supported.");
		}
		if(!isDocFile(docFile)) {
			LOG.fatal("Throw NoMainDocumentFoundException this message: The file's extension is not .docx: " + docFile.getName());
			throw new NotSupportedFileExtensionException("The file's extension is not .docx: " + docFile.getName());
		}
		if(!isCorrectMSOffice()) {
			LOG.fatal("Throw NotSupportedOperationSystemException this message: Could not create compiler, because the current MSOffice or its version is not supported.");
			throw new NotSupportedApplicationException("Could not create compiler, because the current MSOffice or its version is not supported.");
		}

		File baseFolder = new File("");

		IConverter converter = LocalConverter
				.builder()
				.baseFolder(baseFolder)
				.workerPool(20, 25, 2, TimeUnit.SECONDS)
				.processTimeout(15, TimeUnit.SECONDS)
				.build();

		return new Doc2PDFConverter(converter);
	}

	private boolean isSupportedOS() {
		return osName.toLowerCase().contains(SupportedOperationSystems.WINDOWS.getStringValue());
	}

	private boolean isDocFile(File file) {
		String fileExtension = FilenameUtils.getExtension(file.getName());

		return fileExtension.equals(SupportedCompileableFileExtensions.DOC.getStringValue());
	}

	private boolean isCorrectMSOffice() throws IOException {
		try {
			Process process = Runtime.getRuntime().exec("reg query HKEY_CLASSES_ROOT" + FILE_SEPARATOR +"Word.Application" + FILE_SEPARATOR + "CurVer");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
			String string;
			while((string = reader.readLine()) != null) {
				if(string.contains("Word.Application.")) {
					String[] appWithVersion = string.substring(string.indexOf("Word.Application.")).split("\\.");
					int versionNo = getVersionNumber(appWithVersion);
					
					return versionNo >= 12;
				}
			}
			
			return false;
			
		} catch (IOException e) {
			LOG.fatal("Catch IOException and throw forward with the same message: " + System.lineSeparator() + e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

	private int getVersionNumber(String[] appWithVersion) {
		for(String string : appWithVersion) {
			if(!string.matches("[0-9]*")) {
				continue;
			}
			
			return Integer.parseInt(string);
		}
		
		return 0;
	}
}