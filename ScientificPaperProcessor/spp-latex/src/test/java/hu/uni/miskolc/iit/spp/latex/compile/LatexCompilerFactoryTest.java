package hu.uni.miskolc.iit.spp.latex.compile;

import static org.junit.Assert.*;

import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class LatexCompilerFactoryTest {

	private String osName;
	private LatexCompilerFactory testFactory;
	
	@Test
	public void testCreateLatexPDFCompiler_ReturnLinuxLatexPDFCompiler() throws NotSupportedOperationSystemException {
		osName = "linux";
		testFactory = new LatexCompilerFactory(osName);
		Latex2PDFCompiler actual = testFactory.createLatexPDFCompiler();
		assertEquals(LinuxLatexPDFCompiler.class, actual.getClass());
	}
	
	@Test
	public void testCreateLatexPDFCompiler_ReturnWindowsLatexPDFCompiler() throws NotSupportedOperationSystemException {
		osName = "windows";
		testFactory = new LatexCompilerFactory(osName);
		Latex2PDFCompiler actual = testFactory.createLatexPDFCompiler();
		assertEquals(WindowsLatexPDFCompiler.class, actual.getClass());
	}
	
	@Test(expected = NotSupportedOperationSystemException.class)
	public void testCreateLatexPDFCompiler_ReturnException() throws NotSupportedOperationSystemException {
		osName = "Mac_OS";
		testFactory = new LatexCompilerFactory(osName);
		Latex2PDFCompiler actual = testFactory.createLatexPDFCompiler();
	}
}