package com.ironalloygames.ds2cc.shared.data;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@PersistenceCapable(detachable = "true")
public class Item implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5875281027292212566L;

	@Persistent(serialized = "true")
	private String encodedImageData;

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

	/**
	 * Gets this item's image data as a base64 encoded PNG string
	 *
	 * @return
	 */
	@XmlElement(name = "encodedImage")
	public String getEncodedImageData() {
		return encodedImageData;
	}

	/**
	 * Sets this item's image data as a base64 encoded PNG string
	 *
	 * @param encodedImageData
	 */
	public void setEncodedImageData(String encodedImageData) {
		this.encodedImageData = encodedImageData;
	}

	/**
	 * Gets a URL that will display this item's image It may be a data URL or a
	 * URL to another endpoint on the server
	 *
	 * @return
	 */
	public String getImageSrc() {
		return "/itemimage?itemName=" + this.getName();
	}

	public void filterInternalData() {
		for (Stat s : Stat.values()) {
			if (statModifiers.containsKey(s) && Math.abs(statModifiers.get(s)) < 0.0001f)
				statModifiers.remove(s);
			if (statMultipliers.containsKey(s) && Math.abs(statMultipliers.get(s)) < 0.0001f)
				statMultipliers.remove(s);
			if (statRequirements.containsKey(s) && Math.abs(statRequirements.get(s)) < 0.0001f)
				statRequirements.remove(s);
		}
	}

	public Map<Stat, Float> getStatModifiers() {
		return statModifiers;
	}

	public void setStatModifiers(Map<Stat, Float> statModifiers) {
		this.statModifiers = statModifiers;
	}

	public Map<Stat, Float> getStatMultipliers() {
		return statMultipliers;
	}

	public void setStatMultipliers(Map<Stat, Float> statMultipliers) {
		this.statMultipliers = statMultipliers;
	}

	public Map<Stat, Float> getStatRequirements() {
		return statRequirements;
	}

	public void setStatRequirements(Map<Stat, Float> statRequirements) {
		this.statRequirements = statRequirements;
	}

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

	/**
	 * @return the durability
	 */
	@XmlAttribute
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
	@XmlAttribute
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
