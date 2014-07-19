package com.ironalloygames.ds2cc.shared.data;

import java.util.EnumMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Item implements IsSerializable {

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
	@Persistent(serialized = "true")
	Map<Stat, Float> statModifiers = new EnumMap<>(Stat.class);

	/**
	 * Percentage modifiers to stats
	 */
	@Persistent(serialized = "true")
	Map<Stat, Float> statMultipliers = new EnumMap<>(Stat.class);

	/**
	 * Minimum stats to use this item. Some items can be used below these
	 * minimums, but the item will be less uesful
	 */
	@Persistent(serialized = "true")
	Map<Stat, Float> statRequirements = new EnumMap<>(Stat.class);

	public float getStatRequirement(Stat stat)
	{
		if (statRequirements == null)
			statRequirements = new EnumMap<>(Stat.class);

		if (statRequirements.containsKey(stat))
			return statRequirements.get(stat);
		else
			return 0;
	}

	public void setStatRequirement(Stat stat, float amount)
	{
		if (statRequirements == null)
			statRequirements = new EnumMap<>(Stat.class);

		statRequirements.put(stat, amount);
	}

	public float getStatModifier(Stat stat)
	{
		if (statModifiers == null)
			statModifiers = new EnumMap<>(Stat.class);

		if (statModifiers.containsKey(stat))
			return statModifiers.get(stat);
		else
			return 0;
	}

	public void setStatModifier(Stat stat, float amount)
	{
		if (statModifiers == null)
			statModifiers = new EnumMap<>(Stat.class);

		statModifiers.put(stat, amount);
	}

	public float getStatMultiplier(Stat stat)
	{
		if (statMultipliers == null)
			statMultipliers = new EnumMap<>(Stat.class);

		if (statMultipliers.containsKey(stat))
			return statMultipliers.get(stat);
		else
			return 0;
	}

	public void setStatMultiplier(Stat stat, float amount)
	{
		if (statMultipliers == null)
			statMultipliers = new EnumMap<>(Stat.class);

		statMultipliers.put(stat, amount);
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

	@PrimaryKey
	@Persistent
	private String name;

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
