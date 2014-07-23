package com.ironalloygames.ds2cc.server.tsvimporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.Slot;
import com.ironalloygames.ds2cc.shared.data.Stat;

public class INILikeSerializer {
	public String serializeList(List<Item> list) {
		StringBuilder s = new StringBuilder();

		for (Item i : list) {
			s.append(serialize(i));
			s.append("\n");
		}

		return s.toString();
	}

	public List<Item> deserializeList(String data) {
		Pattern p = Pattern.compile("\\[(.*)\\][^\\[]+");

		Matcher matcher = p.matcher(data);

		List<Item> list = new ArrayList<Item>();

		while (matcher.find()) {
			list.add(deserialize(matcher.group()));
		}

		return list;
	}

	public String serialize(Item item) {
		StringBuilder s = new StringBuilder();

		s.append("[");
		s.append(item.getName());
		s.append("]\n");

		Pattern p = Pattern.compile("get(.*)");

		for (Method m : Item.class.getMethods()) {

			try {
				Matcher matcher = p.matcher(m.getName());

				if (matcher.matches()) {
					// we've already serialized the name
					if (matcher.group(1).equals("Name") || matcher.group(1).equals("Class"))
						continue;

					if (m.getParameterTypes().length == 0) {

						Object val = m.invoke(item);

						boolean containsData = objectContainsDataWorthSerializing(val);

						if (containsData) {
							s.append(matcher.group(1).toString());
							s.append("=");
							s.append("" + val);
							s.append("\n");
						}
					} else {
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (Method m : Item.class.getMethods()) {

			try {
				Matcher matcher = p.matcher(m.getName());

				if (matcher.matches()) {
					// we've already serialized the name
					if (matcher.group(1).equals("Name") || matcher.group(1).equals("Class"))
						continue;

					if (m.getParameterTypes().length == 0) {
					} else {
						// its a map internally, and it takes slots...?
						for (Stat stat : Stat.values()) {
							Object val = m.invoke(item, stat);

							boolean containsData = objectContainsDataWorthSerializing(val);

							if (containsData) {
								s.append(matcher.group(1).toString());
								s.append(".");
								s.append(stat);
								s.append("=");
								s.append(val);
								s.append("\n");
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return s.toString();
	}

	private boolean objectContainsDataWorthSerializing(Object val) {
		boolean containsData = false;

		if (val instanceof Float){
			if(Math.abs((float) val) > 0.0001f)
				containsData = true;
		} else if (val instanceof Integer) {
			if((int) val != 0)
				containsData = true;
		} else if (val != null)
			containsData = true;
		return containsData;
	}

	public Item deserialize(String data) {
		Item itm = new Item();

		String[] lines = data.split("\n");

		Pattern nameFinder = Pattern.compile("\\[(.*)\\]");
		Pattern basicLineFinder = Pattern.compile("^([A-Za-z0-9_]+)=(.*)$");
		Pattern complexLineFinder = Pattern.compile("^([A-Za-z0-9_]+)\\.([A-Za-z0-9_]+)=(.*)$");

		for (String line : lines) {
			try {
				Matcher m;

				if ((m = complexLineFinder.matcher(line)).matches()) {

					Stat stat = Stat.valueOf(m.group(2));

					for (Method method : Item.class.getMethods()) {
						if (method.getName().equals("set" + m.group(1))) {
							if (method.getParameterTypes()[1].equals(float.class)) {
								method.invoke(itm, stat, Float.parseFloat(m.group(3)));
							} else if (method.getParameterTypes()[1].equals(float.class)) {
								method.invoke(itm, stat, Integer.parseInt(m.group(3)));
							} else {
								// assume its a string... i guess?
								method.invoke(itm, stat, m.group(3).toString());
							}
						}
					}
				} else if ((m = basicLineFinder.matcher(line)).matches()) {
					for (Method method : Item.class.getMethods()) {
						if (method.getName().equals("set" + m.group(1))) {

							if (method.getParameterTypes()[0].equals(float.class)) {
								method.invoke(itm, Float.parseFloat(m.group(2)));
							} else if (method.getParameterTypes()[0].equals(int.class)) {
								method.invoke(itm, Integer.parseInt(m.group(2)));
							} else if (method.getParameterTypes()[0].equals(Slot.class)) {
								method.invoke(itm, Slot.valueOf(m.group(2)));
							} else {
								// assume its a string... i guess?
								method.invoke(itm, m.group(2).toString());
							}
						}
					}
				} else if ((m = nameFinder.matcher(line)).matches()) {
					itm.setName(m.group(1));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return itm;
	}
}
