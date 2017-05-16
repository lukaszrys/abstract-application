package pl.rys.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import pl.rys.core.entity.AbstractPersistentFile;
import pl.rys.core.exception.NotFoundException;
import pl.rys.core.exception.UploadFailedException;

public abstract class AbstractPersistentFileServiceImpl<T extends AbstractPersistentFile> extends AbstractServiceImpl<T, Long> implements AbstractPersistentFileService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistentFileService.class);

	@Override
	@Transactional
	public void delete(Long uuid) {
		getFile(uuid).delete();
		super.delete(uuid);
	}

	@Override
	@Transactional(readOnly = true)
	public File getFile(Long uuid) {
		T entity = getRepository().findOne(uuid);
		return getFile(entity);
	}

	@Override
	public File getFile(T entity) {
		return new File(getDirectory(entity), entity.getId().toString());
	}

	@Override
	public byte[] getFileContent(T entity) {
		try {
			return FileUtils.readFileToByteArray(getFile(entity));
		} catch (IOException e) {
			LOGGER.error("Unable to read file", e);
			throw new NotFoundException("file", entity);
		}
	}

	@Override
	public InputStream getInputStream(T entity) {
		try {
			return new FileInputStream(getFile(entity));
		} catch (IOException e) {
			LOGGER.error("Unable to read file", e);
			throw new NotFoundException("file", entity.getId());
		}
	}

	@Override
	@Transactional
	public T persist(T entity, InputStream stream) {
		T en = super.save(entity);
		writeFile(entity, stream);
		return en;
	}

	@SuppressWarnings("unchecked")
	protected T createNewPersistentFileEntityInstance() throws InstantiationException, IllegalAccessException {
		return ((Class<T>) genericParam(this.getClass(), 0)).newInstance();
	}

	@PostConstruct
	protected void init() throws InstantiationException, IllegalAccessException {
		new File(getDirectory(createNewPersistentFileEntityInstance())).mkdirs();
	}

	protected void writeFile(T entity, InputStream source) {
		try {
			FileUtils.copyInputStreamToFile(source, getFile(entity));
		} catch (IOException e) {
			LOGGER.error("Unable to write file", e);
			throw new UploadFailedException();
		}
	}

	private Class<?> genericParam(Class<?> clazz, int which) {
		return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[which];
	}
}
