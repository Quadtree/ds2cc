package com.ironalloygames.ds2cc.server.tsvimporter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.Slot;
import com.ironalloygames.ds2cc.shared.data.Stat;

public class INILikeSerializerTest {

	@Test
	public void testSerializeList() {
		INILikeSerializer o = new INILikeSerializer();

		ArrayList<Item> lst = new ArrayList<Item>();
		lst.add(makeItem("ITM1", 5));
		lst.add(makeItem("ITM2", 6));

		String serialized = o.serializeList(lst);

		assertEquals("Serialization of 2",
		"[ITM1]\n"+
		"Durability=5\n"+
		"\n"+
		"[ITM2]\n"+
		"Durability=6\n"
				, serialized);

		List<Item> lst2 = o.deserializeList(serialized);

		for (int i = 0; i < Math.max(lst.size(), lst2.size()); i++) {
			assertEquals("List item match " + i, lst.get(i).getName(), lst2.get(i).getName());
		}
	}

	@Test
	public void testSerialize() {
		Item itm = new Item();
		itm.setName("THE NAME");
		itm.setDurability(8);
		itm.setSlot(Slot.HANDS);
		itm.setStatModifier(Stat.ATTUNMENT, 2.5f);
		itm.setStatMultiplier(Stat.DEXTERITY, 5.5f);
		itm.setStatRequirement(Stat.FAITH, 9.5f);

		INILikeSerializer o = new INILikeSerializer();

		String ser = o.serialize(itm);

		Item itm2 = o.deserialize(ser);

		assertEquals("Name", itm.getName(), itm2.getName());
		assertEquals("Durability", itm.getDurability(), itm2.getDurability());
		assertEquals("Slot", itm.getSlot(), itm2.getSlot());
		assertEquals("Stat Modifier", itm.getStatModifier(Stat.ATTUNMENT), itm2.getStatModifier(Stat.ATTUNMENT), 0.01f);
		assertEquals("Stat Multiplier", itm.getStatMultiplier(Stat.DEXTERITY), itm2.getStatMultiplier(Stat.DEXTERITY), 0.01f);
		assertEquals("Stat Requirement", itm.getStatRequirement(Stat.FAITH), itm2.getStatRequirement(Stat.FAITH), 0.01f);
	}

	private Item makeItem(String name, int dur)
	{
		Item item = new Item();
		item.setName(name);
		item.setDurability(dur);
		return item;
	}

}
