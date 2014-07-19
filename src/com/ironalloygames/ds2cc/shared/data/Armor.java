package com.ironalloygames.ds2cc.shared.data;

import java.util.EnumMap;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	@Persistent
	private Slot slot;

	/**
	 * Maximum durability. Durability is completely restored at bonfires
	 */
	@Persistent
	private int durability;

	/**
	 * Weight of this item, only matters if it is equipped
	 */
	@Persistent
	private float weight;

	/**
	 * Absolute modifiers to stats
	 */
	@Persistent
	EnumMap<Stat, Integer> statModifiers = new EnumMap<>(Stat.class);

	/**
	 * Minimum stats to use this item. Some items can be used below these
	 * minimums, but the item will be less uesful
	 */
	@Persistent
	EnumMap<Stat, Integer> statRequirements = new EnumMap<>(Stat.class);

	/**
	 * Bonus resistances when this item is equipped
	 */
	@Persistent
	EnumMap<ResistanceType, Float> resistanceBonus = new EnumMap<>(ResistanceType.class);

	public float getResistance(ResistanceType type)
	{
		if (resistanceBonus.containsKey(type))
			return resistanceBonus.get(type);
		else
			return 0;
	}

	public void setResistance(ResistanceType type, float amount)
	{
		resistanceBonus.put(type, amount);
	}

	public float getStatRequirement(Stat stat)
	{
		if (statRequirements.containsKey(stat))
			return statRequirements.get(stat);
		else
			return 0;
	}

	public void setStatRequirement(Stat stat, int amount)
	{
		statRequirements.put(stat, amount);
	}

	public float getStatModifier(Stat stat)
	{
		if (statRequirements.containsKey(stat))
			return statRequirements.get(stat);
		else
			return 0;
	}

	public void setStatModifier(Stat stat, int amount)
	{
		statRequirements.put(stat, amount);
	}

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
