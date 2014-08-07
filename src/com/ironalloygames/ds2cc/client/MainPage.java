package com.ironalloygames.ds2cc.client;

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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

public class MainPage extends Composite {

	// NOTE: This is all test code
	// feel free to blow it away and replace it with
	// the real code

	DataServiceAsync dataService = GWT.create(DataService.class);

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	@UiField ListBox testItemList;
	@UiField Image itemImage;

	final List<BasicItem> itemList = new ArrayList<>();

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {
		initWidget(uiBinder.createAndBindUi(this));

		Logger.getLogger("Client").info("Starting up");

		dataService.getAllBasicItems(new AsyncCallback<List<BasicItem>>() {

			@Override
			public void onFailure(Throwable caught) {
				Logger.getLogger("Client").warning("FAILURE: " + caught);
			}

			@Override
			public void onSuccess(List<BasicItem> result) {
				Logger.getLogger("Client").info("SUCCESS: " + result.size());

				for (BasicItem itm : result) {
					testItemList.addItem(itm.getName() + " " + itm.getSlot());
					itemList.add(itm);
				}
			}
		});
	}

	@UiHandler("testItemList")
	void onTestItemListChange(ChangeEvent event) {
		if (testItemList.getSelectedIndex() != -1) {

			BasicItem curItem = itemList.get(testItemList.getSelectedIndex());

			Logger.getLogger("Client").info(curItem.getName());

			if (curItem instanceof Item) {
				itemImage.setUrl(((Item) curItem).getImageSrc());
			} else {
				dataService.readItem(curItem, new AsyncCallback<Item>() {

					@Override
					public void onSuccess(Item result) {
						for (int i = 0; i < itemList.size(); i++) {
							if (itemList.get(i).getName().equals(result.getName()))
							{
								itemList.set(i, result);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Logger.getLogger("Client").warning("FAILURE: " + caught);
					}
				});
			}
		}
	}
}
