package hu.uni.miskolc.iit.spp.latex;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.latex.archive.LatexArchiveValidator;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;

public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {
	
	private Latex2PDFCompiler compiler;
	private LatexArchiveValidator validator;
	
	public ScientificPaperLatexBuilder(Latex2PDFCompiler compiler, LatexArchiveValidator validator) {
		this.compiler = compiler;
		this.validator = validator;
	}

	@Override
	protected void checkFileExtension(File paper) throws NoMainDocumentFoundException, IOException {
		if(!this.validator.validate(paper)) {
			throw new NoMainDocumentFoundException("Could unpack, but directory not contained main.tex or paper.tex file.");
		}
	}
	
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException, IOException {
			File pdfFile = this.compiler.generatePDFFile(paper);
			return pdfFile;
	}
	@Override
	protected String extractTitle(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String extractAbstarct(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		// TODO Auto-generated method stub
		return null;
	}
}