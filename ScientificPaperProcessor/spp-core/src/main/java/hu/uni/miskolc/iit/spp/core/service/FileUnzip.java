package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;

import hu.uni.miskolc.iit.spp.core.model.NotSupportedFileExtensionException;

public interface FileUnzip {
	void unzip(File zipFile) throws NotSupportedFileExtensionException;
	
	File checkArchiveExtension(File zipFile) throws NotSupportedFileExtensionException;
}
