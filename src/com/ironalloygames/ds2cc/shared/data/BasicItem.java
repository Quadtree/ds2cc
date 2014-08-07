package com.ironalloygames.ds2cc.shared.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(detachable = "true")
public class BasicItem implements IsSerializable {

	/**
	 * The armor slot this particular piece is equipped in
	 */
	@Persistent
	private Slot slot;

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
	private String name;

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
