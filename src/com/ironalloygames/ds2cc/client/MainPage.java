package com.ironalloygames.ds2cc.client;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.shared.data.Item;

public class MainPage extends Composite {

	DataServiceAsync dataService = GWT.create(DataService.class);

	private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);
	@UiField ListBox testItemList;

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {
		initWidget(uiBinder.createAndBindUi(this));

		Logger.getLogger("Client").info("Starting up");

		dataService.getAllItems(new AsyncCallback<List<Item>>() {

			@Override
			public void onFailure(Throwable caught) {
				Logger.getLogger("Client").warning("FAILURE: " + caught);
			}

			@Override
			public void onSuccess(List<Item> result) {
				Logger.getLogger("Client").info("SUCCESS: " + result.size());

				for (Item itm : result) {
					testItemList.addItem(itm.getName() + " " + itm.getSlot());
				}
			}
		});
	}

}
