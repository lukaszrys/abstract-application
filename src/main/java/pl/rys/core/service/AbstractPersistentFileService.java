package pl.rys.core.service;

import java.io.File;
import java.io.InputStream;

import pl.rys.core.entity.AbstractPersistentFile;

public interface AbstractPersistentFileService<T extends AbstractPersistentFile> extends AbstractService<T, Long> {

	String getDirectory(T entity);

	File getFile(Long uuid);

	File getFile(T entity);

	byte[] getFileContent(T entity);

	InputStream getInputStream(T entity);

	T persist(T entity, InputStream source);

}
