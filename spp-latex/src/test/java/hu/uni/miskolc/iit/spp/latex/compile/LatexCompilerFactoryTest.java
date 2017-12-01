package hu.uni.miskolc.iit.spp.latex.compile;

import static org.junit.Assert.*;

import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class LatexCompilerFactoryTest {

	private LatexCompilerFactory factory;
	private Latex2PDFCompiler compiler;
	
	@Test(expected = NotSupportedOperationSystemException.class)
	public void testCreateLatexPDFCompiler_WrongOS() throws NotSupportedOperationSystemException {
		factory = new LatexCompilerFactory("mac_os");
		compiler = factory.createLatexPDFCompiler();
	}
	
	@Test
	public void testCreateLatexPDFCompiler_LinuxCompiler() throws NotSupportedOperationSystemException {
		factory = new LatexCompilerFactory("LInUX_nextGen");
		compiler = factory.createLatexPDFCompiler();
		
		assertEquals(LinuxLatexPDFCompiler.class, compiler.getClass());
	}
	
	@Test
	public void testCreateLatexPDFCompiler_WindowsCompiler() throws NotSupportedOperationSystemException {
		factory = new LatexCompilerFactory("WINdows15_for_Students");
		compiler = factory.createLatexPDFCompiler();
		
		assertEquals(WindowsLatexPDFCompiler.class, compiler.getClass());
	}
}