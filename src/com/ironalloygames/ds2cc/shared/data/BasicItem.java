package com.ironalloygames.ds2cc.shared.data;

import java.io.Serializable;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;

@PersistenceCapable(detachable = "true")
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class BasicItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7735659911930136928L;

	/**
	 * The armor slot this particular piece is equipped in
	 */
	@Persistent
	protected Slot slot;

	/**
	 * @return the slot
	 */
	@XmlAttribute
	public Slot getSlot() {
		return slot;
	}

	/**
	 * @param slot
	 *            the slot to set
	 */
	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	@PrimaryKey
	@Persistent
	protected String name;

	/**
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
