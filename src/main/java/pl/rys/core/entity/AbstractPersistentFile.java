package pl.rys.core.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPersistentFile extends BaseEntity<Long> {

	private static final long serialVersionUID = -7123783630031758570L;

	private String name;

	private String mediaType;

	private Long size;

	public String getMediaType() {
		return mediaType;
	}

	public String getName() {
		return name;
	}

	public Long getSize() {
		return size;
	}

	public void setMediaType(String type) {
		this.mediaType = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
