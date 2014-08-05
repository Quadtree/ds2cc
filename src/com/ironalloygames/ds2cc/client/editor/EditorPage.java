package com.ironalloygames.ds2cc.client.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.client.DataService;
import com.ironalloygames.ds2cc.client.DataServiceAsync;
import com.ironalloygames.ds2cc.shared.data.Item;
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

					// if (itemList.size() == 0)
					// Logger.getLogger("Client").info("ARMOR OF: " +
					// itm.getStatModifier(Stat.SLASH_RESISTANCE));

					testItemList.addItem(itm.getName() + " " + itm.getSlot());
					itemList.add(itm);
				}
			}
		});
	}

	@UiHandler("testItemList")
	void onTestItemListChange(ChangeEvent event) {
		if (testItemList.getSelectedIndex() != -1) {

			Item item = itemList.get(testItemList.getSelectedIndex());

			itemBeingEdited = item;

			// Logger.getLogger("Client").info(item.getName());
			itemImage.setUrl(item.getImageSrc());
			itemNameLabel.setText(item.getName());

			int atribsCount = item.getStatModifiers().size() + item.getStatMultipliers().size() + item.getStatRequirements().size();

			atribsGrid.resize(atribsCount, 3);

			int poi = 0;

			// Logger.getLogger("Client").info(item.getStatModifiers().toString());
			// Logger.getLogger("Client").info(item.getStatMultipliers().toString());
			// Logger.getLogger("Client").info(item.getStatRequirements().toString());

			// Logger.getLogger("Client").info("ARMOR: " +
			// item.getStatModifier(Stat.SLASH_RESISTANCE));

			for (Entry<Stat, Float> kv : item.getStatModifiers().entrySet()) {

				ListBox c1 = createStatTypeComboBox();
				c1.setSelectedIndex(1);

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
				tb.addChangeHandler(new ItemUpdatedChangeEvent());
				tb.setText(kv.getValue().toString());

				atribsGrid.setWidget(poi, 2, tb);

				poi++;

				// Logger.getLogger("Client").info(kv.toString());
			}
		}
	}

	Item itemBeingEdited = null;

	class ItemUpdatedChangeEvent implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
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

	}

	private ListBox createStatTypeComboBox()
	{
		ListBox c = new ListBox();
		for (ItemStatType ist : ItemStatType.values()) {
			c.addItem(ist.toString());
		}
		c.addChangeHandler(new ItemUpdatedChangeEvent());
		return c;
	}

	private ListBox createStatComboBox()
	{
		ListBox c = new ListBox();
		for (Stat ist : Stat.values()) {
			c.addItem(ist.toString());
		}
		c.addChangeHandler(new ItemUpdatedChangeEvent());
		return c;
	}
}
