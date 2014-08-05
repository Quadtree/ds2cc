package com.ironalloygames.ds2cc.client.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.client.DataService;
import com.ironalloygames.ds2cc.client.DataServiceAsync;
import com.ironalloygames.ds2cc.shared.data.Item;

public class EditorPage extends Composite {

	DataServiceAsync dataService = GWT.create(DataService.class);

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	@UiField ListBox testItemList;
	@UiField Image itemImage;
	@UiField Label itemNameLabel;
	@UiField Label currentStatusLabel;

	final List<Item> itemList = new ArrayList<>();

	interface MainPageUiBinder extends UiBinder<Widget, EditorPage> {
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
					testItemList.addItem(itm.getName() + " " + itm.getSlot());
					itemList.add(itm);
				}
			}
		});
	}

	@UiHandler("testItemList")
	void onTestItemListChange(ChangeEvent event) {
		if (testItemList.getSelectedIndex() != -1) {
			Logger.getLogger("Client").info(itemList.get(testItemList.getSelectedIndex()).getName());
			itemImage.setUrl(itemList.get(testItemList.getSelectedIndex()).getImageSrc());
			itemNameLabel.setText(itemList.get(testItemList.getSelectedIndex()).getName());

		}
	}
}
