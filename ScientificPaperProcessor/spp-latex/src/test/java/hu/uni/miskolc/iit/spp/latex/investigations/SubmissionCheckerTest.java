package hu.uni.miskolc.iit.spp.latex.investigations;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import hu.uni.miskolc.iit.spp.core.model.SupportedArchiveExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedCompileableTextFileExtensions;
import hu.uni.miskolc.iit.spp.core.model.SupportedFileNames;
import hu.uni.miskolc.iit.spp.core.model.SupportedGeneratedFileExtensions;

public class SubmissionCheckerTest {

	@Test
	public void testIsAccaptedName() {
		File mainFile = new File(SupportedFileNames.MAIN.getStringValue());
		File paperFile = new File(SupportedFileNames.PAPER.getStringValue());
		File wrongNameFile = new File("wrongName");
		boolean condition_1 = SubmissionChecker.isAccaptedName(mainFile);
		boolean condition_2 = SubmissionChecker.isAccaptedName(paperFile);
		boolean condition_3 = SubmissionChecker.isAccaptedName(wrongNameFile);

		assertTrue(condition_1 == true && condition_2 == true && condition_3 != true);
	}

	@Test
	public void testIsSupportedOS_Linux() {
		String testedOS = "LINUX_nextGen_500";
		assertTrue(SubmissionChecker.isSupportedOS(testedOS));
	}
	
	@Test
	public void testIsSupportedOS_Windows() {
		String testedOS = "WINdows_15_studentVersion";
		assertTrue(SubmissionChecker.isSupportedOS(testedOS));
	}
	
	@Test
	public void testIsSupportedOS_Other() {
		String testedOS = "Mac_OS";
		assertFalse(SubmissionChecker.isSupportedOS(testedOS));
	}
	
	@Test
	public void testIsSupportedArchive() {
		File zipFile = new File("apple." + SupportedArchiveExtensions.ZIP.getStringValue());
		File rarFile = new File("apple.rar");
		boolean condition_1 = SubmissionChecker.isSupportedArchive(zipFile);
		boolean condition_2 = SubmissionChecker.isSupportedArchive(rarFile);

		assertTrue(condition_1 == true && condition_2 != true);
	}

	@Test
	public void testIsGenerated() {
		File pdfFile = new File("apple." + SupportedGeneratedFileExtensions.PDF.getStringValue());
		File txtFile = new File("apple.txt");
		boolean condition_1 = SubmissionChecker.isGenerated(pdfFile);
		boolean condition_2 = SubmissionChecker.isGenerated(txtFile);
		
		assertTrue(condition_1 == true && condition_2 != true);
	}

	@Test
	public void testIsCompileable() {
		File texFile = new File("apple." + SupportedCompileableTextFileExtensions.TEX.getStringValue());
		File txtFile = new File("apple.txt");
		boolean condition_1 = SubmissionChecker.isCompileable(texFile);
		boolean condition_2 = SubmissionChecker.isCompileable(txtFile);
		
		assertTrue(condition_1 == true && condition_2 != true);
	}
}