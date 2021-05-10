package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FDS_DESCRIPTION database table.
 * 
 */
@Entity
@Table(name="FDS_DESCRIPTION")
public class FdsDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=50)
	private String description;

	@Id
	@Column(nullable=false, length=3)
	private String id;

	@Column(length=50)
	private String type;
	
	public FdsDescription() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}