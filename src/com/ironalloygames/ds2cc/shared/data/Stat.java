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
	FAITH("FTH");

	String abbrev;

	public String getAbbrev() {
		return abbrev;
	}

	private Stat(String abbrev) {
		this.abbrev = abbrev;
	}

}