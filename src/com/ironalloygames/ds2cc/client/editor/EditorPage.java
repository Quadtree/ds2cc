package com.ironalloygames.ds2cc.client.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.Slot;
import com.ironalloygames.ds2cc.shared.data.Stat;

public class EditorPage extends Composite {

	enum ItemStatType {
		Multiplier,
		Modifier,
		Requirement
	}

	DataServiceAsync dataService = GWT.create(DataService.class);

	private static EditorPageUiBinder uiBinder = GWT.create(EditorPageUiBinder.class);
	@UiField ListBox testItemList;
	@UiField Image itemImage;
	@UiField Label itemNameLabel;
	@UiField Label currentStatusLabel;
	@UiField Grid atribsGrid;
	@UiField Button saveItemButton;
	@UiField TextBox durabilityTextBox;
	@UiField TextBox weightTextBox;
	@UiField ListBox slotListBox;
	@UiField TextBox createNewItemNameBox;
	@UiField Button createNewItemButton;
	@UiField Button button;

	final List<Item> itemList = new ArrayList<>();

	interface EditorPageUiBinder extends UiBinder<Widget, EditorPage> {
	}

	public EditorPage() {
		initWidget(uiBinder.createAndBindUi(this));

		Logger.getLogger("Client").info("Starting up");
		currentStatusLabel.setText("Loading...");

		dataService.getAllItems(new AsyncCallback<List<Item>>() {

			@Override
			public void onFailure(Throwable caught) {
				currentStatusLabel.setText("FAILURE: " + caught);
			}

			@Override
			public void onSuccess(List<Item> result) {
				currentStatusLabel.setText("SUCCESS: " + result.size());

				for (Item itm : result) {
					itemList.add(itm);
				}

				updateItemList();
			}
		});

		for (Slot s : Slot.values()) {
			slotListBox.addItem(s.toString());
		}
	}

	private void updateItemList()
	{
		int originalSelectedIndex = testItemList.getSelectedIndex();

		testItemList.clear();

		for (Item itm : itemList) {
			testItemList.addItem(itm.getName() + " " + itm.getSlot());
		}

		if (originalSelectedIndex != -1)
			testItemList.setSelectedIndex(originalSelectedIndex);
	}

	@UiHandler("testItemList")
	void onTestItemListChange(ChangeEvent event) {
		if (testItemList.getSelectedIndex() != -1) {

			Item bi = itemList.get(testItemList.getSelectedIndex());

			if (bi instanceof Item)
			{
				setSelectedItemTo(bi);
			} else {

			}

		}
	}

	private void setSelectedItemTo(Item item)
	{
		itemBeingEdited = item;

		// Logger.getLogger("Client").info(item.getName());
		itemImage.setUrl(item.getImageSrc());
		itemNameLabel.setText(item.getName());

		int atribsCount = item.getStatModifiers().size() + item.getStatMultipliers().size() + item.getStatRequirements().size();

		atribsGrid.resize(atribsCount, 3);

		int poi = 0;

		poi = generateGridRows(poi, item.getStatModifiers().entrySet(), ItemStatType.Modifier.ordinal());
		poi = generateGridRows(poi, item.getStatMultipliers().entrySet(), ItemStatType.Multiplier.ordinal());
		poi = generateGridRows(poi, item.getStatRequirements().entrySet(), ItemStatType.Requirement.ordinal());

		durabilityTextBox.setText("" + item.getDurability());
		weightTextBox.setText("" + item.getWeight());
		slotListBox.setSelectedIndex(item.getSlot().ordinal());
	}

	private int generateGridRows(int poi, Set<Entry<Stat, Float>> entries, int itemStatTypeOrdinal) {
		for (Entry<Stat, Float> kv : entries) {

			ListBox c1 = createStatTypeComboBox();
			c1.setSelectedIndex(itemStatTypeOrdinal);

			atribsGrid.setWidget(poi, 0, c1);

			ListBox c2 = createStatComboBox();

			for (int i = 0; i < Stat.values().length; i++) {
				if (Stat.values()[i] == kv.getKey()) {
					c2.setSelectedIndex(i);
					break;
				}
			}

			atribsGrid.setWidget(poi, 1, c2);

			TextBox tb = new TextBox();
			tb.setText(kv.getValue().toString());

			atribsGrid.setWidget(poi, 2, tb);

			poi++;
		}
		return poi;
	}

	Item itemBeingEdited = null;

	private ListBox createStatTypeComboBox()
	{
		ListBox c = new ListBox();
		for (ItemStatType ist : ItemStatType.values()) {
			c.addItem(ist.toString());
		}
		return c;
	}

	private ListBox createStatComboBox()
	{
		ListBox c = new ListBox();
		for (Stat ist : Stat.values()) {
			c.addItem(ist.toString());
		}
		return c;
	}

	@UiHandler("saveItemButton")
	void onSaveItemButtonClick(ClickEvent event) {

		currentStatusLabel.setText("Saving...");

		itemBeingEdited.setSlot(Slot.values()[slotListBox.getSelectedIndex()]);

		try {
			itemBeingEdited.setDurability(Integer.parseInt(durabilityTextBox.getText()));
		} catch (NumberFormatException ex) {
		}

		try {
			itemBeingEdited.setWeight(Float.parseFloat(weightTextBox.getText()));
		} catch (NumberFormatException ex) {
		}

		itemBeingEdited.setStatModifiers(new HashMap<Stat, Float>());
		itemBeingEdited.setStatMultipliers(new HashMap<Stat, Float>());
		itemBeingEdited.setStatRequirements(new HashMap<Stat, Float>());
		for (int row = 0; row < atribsGrid.getRowCount(); row++) {
			ItemStatType ist = ItemStatType.values()[((ListBox) atribsGrid.getWidget(row, 0)).getSelectedIndex()];
			Stat stat = Stat.values()[((ListBox) atribsGrid.getWidget(row, 1)).getSelectedIndex()];

			float value = 0;
			try {
				value = Float.parseFloat(((TextBox) atribsGrid.getWidget(row, 2)).getText());
			} catch (Exception ex) {
			}

			switch (ist) {
			case Multiplier:
				itemBeingEdited.setStatMultiplier(stat, value);
				break;
			case Modifier:
				itemBeingEdited.setStatModifier(stat, value);
				break;
			case Requirement:
				itemBeingEdited.setStatRequirement(stat, value);
				break;
			}
		}

		updateItemList();

		dataService.writeItem(itemBeingEdited, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				currentStatusLabel.setText("FAILURE: " + caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				currentStatusLabel.setText("SUCCESS?: " + result);
			}
		});
	}

	@UiHandler("createNewItemButton")
	void onCreateNewItemButtonClick(ClickEvent event) {
		Item newItem = new Item();
		newItem.setName(createNewItemNameBox.getText());

		itemList.add(newItem);

		updateItemList();
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {

		int poi = atribsGrid.getRowCount();
		atribsGrid.resizeRows(atribsGrid.getRowCount() + 1);

		ListBox c1 = createStatTypeComboBox();

		atribsGrid.setWidget(poi, 0, c1);

		ListBox c2 = createStatComboBox();

		atribsGrid.setWidget(poi, 1, c2);

		TextBox tb = new TextBox();
		tb.setText("0");

		atribsGrid.setWidget(poi, 2, tb);
	}
}
