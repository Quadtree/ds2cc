package com.ironalloygames.ds2cc.shared.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemList {
	ArrayList<Item> items = new ArrayList<>();

	@XmlElement(name = "item")
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

}
