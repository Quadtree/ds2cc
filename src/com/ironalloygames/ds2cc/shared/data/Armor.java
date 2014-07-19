package com.ironalloygames.ds2cc.shared.data;

import java.util.HashMap;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Armor extends Item {
	public enum Slot {
		HEAD,
		CHEST,
		LEGS,
		HANDS
	}

	public enum ResistanceType {
		PHYSICAL,
		STRIKE,
		SLASH,
		THRUST,
		MAGIC,
		FIRE,
		LIGHTNING,
		DARK,
		POISE,
		POISON,
		BLEED,
		PETRIFY,
		CURSE
	}

	public enum Stat {
		VIGOR,
		ENDURANCE,
		VITALITY,
		ATTUNMENT,
		STRENGTH,
		DEXTERITY,
		ADAPTABILITY,
		INTELLIGENCE,
		FAITH
	}

	/**
	 * The armor slot this particular piece is equipped in
	 */
	private Slot slot;

	/**
	 * Maximum durability. Durability is completely restored at bonfires
	 */
	private int durability;

	/**
	 * Weight of this item, only matters if it is equipped
	 */
	private float weight;

	/**
	 * Absolute modifiers to stats
	 */
	HashMap<Stat, Integer> statModifiers;

	/**
	 * Minimum stats to use this item. Some items can be used below these
	 * minimums, but the item will be less uesful
	 */
	HashMap<Stat, Integer> statRequirements;

	/**
	 * Bonus resistances when this item is equipped
	 */
	HashMap<ResistanceType, Float> resistanceBonus;

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

	/**
	 * @return the durability
	 */
	public int getDurability() {
		return durability;
	}

	/**
	 * @param durability
	 *            the durability to set
	 */
	public void setDurability(int durability) {
		this.durability = durability;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

}
