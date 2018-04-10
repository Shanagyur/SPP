package hu.uni.miskolc.iit.spp.core.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.UseableBuilderNotExistingException;

public class CompositeScientificPaperBuilderTest {

	private CompositeScientificPaperBuilder cSPBuilder;
	private ArrayList<AbstractScientificPaperBuilder> mockBuilders;
	private CompositeScientificPaperBuilder mockCSPBuilder;
	
	@Before
	public void setUp() throws Exception {
		ArrayList<AbstractScientificPaperBuilder> builders = new ArrayList<>();
		cSPBuilder = new CompositeScientificPaperBuilder(builders);
		mockBuilders = new ArrayList<>();
	}
	
	@Test(expected = UseableBuilderNotExistingException.class)
	public void testBuild_StringEmptyList() throws ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		cSPBuilder.build("apple");
	}
	
	@Test(expected = UseableBuilderNotExistingException.class)
	public void testBuild_StringOnlyWrongBuilders() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		AbstractScientificPaperBuilder mockBuilder_0 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_1 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_2 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		
		EasyMock.expect(mockBuilder_0.build("apple")).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_1.build("apple")).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_2.build("apple")).andThrow(new NoMainDocumentFoundException());
		EasyMock.replay(mockBuilder_0);
		EasyMock.replay(mockBuilder_1);
		EasyMock.replay(mockBuilder_2);
		
		mockBuilders.add(mockBuilder_0);
		mockBuilders.add(mockBuilder_1);
		mockBuilders.add(mockBuilder_2);
		mockCSPBuilder = new CompositeScientificPaperBuilder(mockBuilders);
		
		mockCSPBuilder.build("apple");
	}
	
	@Test
	public void testBuild_StringOneGoodBuilderInCollection() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		AbstractScientificPaperBuilder mockBuilder_0 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_1 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_2 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		ScientificPaper expected = new ScientificPaper("title", "paperAbstract", null, null, new File("paper"));
		
		EasyMock.expect(mockBuilder_0.build("apple")).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_1.build("apple")).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_2.build("apple")).andReturn(expected);
		EasyMock.replay(mockBuilder_0);
		EasyMock.replay(mockBuilder_1);
		EasyMock.replay(mockBuilder_2);
		
		mockBuilders.add(mockBuilder_0);
		mockBuilders.add(mockBuilder_1);
		mockBuilders.add(mockBuilder_2);
		mockCSPBuilder = new CompositeScientificPaperBuilder(mockBuilders);
		
		ScientificPaper actual = mockCSPBuilder.build("apple");
		
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = UseableBuilderNotExistingException.class)
	public void testBuild_FileEmptyList() throws ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		cSPBuilder.build(new File("apple"));
	}
	
	@Test(expected = UseableBuilderNotExistingException.class)
	public void testBuild_FileOnlyWrongBuilders() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		AbstractScientificPaperBuilder mockBuilder_0 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_1 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_2 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		
		EasyMock.expect(mockBuilder_0.build(new File("apple.txt"))).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_1.build(new File("apple.txt"))).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_2.build(new File("apple.txt"))).andThrow(new NoMainDocumentFoundException());
		EasyMock.replay(mockBuilder_0);
		EasyMock.replay(mockBuilder_1);
		EasyMock.replay(mockBuilder_2);
		
		mockBuilders.add(mockBuilder_0);
		mockBuilders.add(mockBuilder_1);
		mockBuilders.add(mockBuilder_2);
		mockCSPBuilder = new CompositeScientificPaperBuilder(mockBuilders);
		
		mockCSPBuilder.build(new File("apple.txt"));
	}
	
	@Test
	public void testBuild_FileOneGoodBuilderInCollection() throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		AbstractScientificPaperBuilder mockBuilder_0 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_1 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		AbstractScientificPaperBuilder mockBuilder_2 = EasyMock.mock(AbstractScientificPaperBuilder.class);
		ScientificPaper expected = new ScientificPaper("title", "paperAbstract", null, null, new File("paper"));
		
		EasyMock.expect(mockBuilder_0.build(new File("apple"))).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_1.build(new File("apple"))).andThrow(new NoMainDocumentFoundException());
		EasyMock.expect(mockBuilder_2.build(new File("apple"))).andReturn(expected);
		EasyMock.replay(mockBuilder_0);
		EasyMock.replay(mockBuilder_1);
		EasyMock.replay(mockBuilder_2);
		
		mockBuilders.add(mockBuilder_0);
		mockBuilders.add(mockBuilder_1);
		mockBuilders.add(mockBuilder_2);
		mockCSPBuilder = new CompositeScientificPaperBuilder(mockBuilders);
		
		ScientificPaper actual = mockCSPBuilder.build(new File("apple"));
		
		assertTrue(actual.equals(expected));
	}
}