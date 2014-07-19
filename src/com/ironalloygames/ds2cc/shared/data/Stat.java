package com.ironalloygames.ds2cc.shared.data;

public enum Stat {
	VIGOR("VIG"),
	ENDURANCE("END"),
	VITALITY("VIT"),
	ATTUNMENT("ATN"),
	STRENGTH("STR"),
	DEXTERITY("DEX"),
	ADAPTABILITY("ADP"),
	INTELLIGENCE("INT"),
	FAITH("FTH"),
	PHYSICAL_RESISTANCE,
	STRIKE_RESISTANCE,
	SLASH_RESISTANCE,
	THRUST_RESISTANCE,
	MAGIC_RESISTANCE,
	FIRE_RESISTANCE,
	LIGHTNING_RESISTANCE,
	DARK_RESISTANCE,
	POISE_RESISTANCE,
	POISON_RESISTANCE,
	BLEED_RESISTANCE,
	PETRIFY_RESISTANCE,
	CURSE_RESISTANCE;

	String abbrev;

	public String getAbbrev() {
		return abbrev;
	}

	private Stat(String abbrev) {
		this.abbrev = abbrev;
	}

	private Stat() {
		this.abbrev = null;
	}

}