package com.ironalloygames.ds2cc.server.tsvimporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.Stat;

public class INILikeSerializer {
	public String serializeList(List<Item> list) {
		return "";
	}

	public List<Item> deserializeList(String data) {
		return new ArrayList<Item>();
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
						s.append(matcher.group(1).toString());
						s.append("=");
						s.append("" + m.invoke(item));
						s.append("\n");
					} else {
						// its a map internally, and it takes slots...?
						for (Stat stat : Stat.values()) {
							Object val = m.invoke(item, stat);

							boolean containsData = false;

							if (val instanceof Float && (float) val != 0)
								containsData = true;

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

		s.append("\n");

		System.out.println(s);

		return s.toString();
	}

	public Item deserialize(String data) {
		return null;
	}
}
