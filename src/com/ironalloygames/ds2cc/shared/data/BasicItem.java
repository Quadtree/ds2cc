package com.ironalloygames.ds2cc.shared.data;

import java.io.Serializable;

public class BasicItem implements Serializable, ItemKey {

	/**
	 *
	 */
	private static final long serialVersionUID = 7735659911930136928L;

	/**
	 * The armor slot this particular piece is equipped in
	 */
	protected Slot slot;

	/**
	 * @return the slot
	 */
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

	protected String name;

	/**
	 * @return the name
	 */
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
