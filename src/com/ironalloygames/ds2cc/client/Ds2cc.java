package com.ironalloygames.ds2cc.client;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.ironalloygames.ds2cc.shared.data.Item;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ds2cc implements EntryPoint {

	DataServiceAsync dataService = GWT.create(DataService.class);

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		RootPanel.get("mainContent").add(new MainPage());

		Logger.getLogger("Client").info("Starting up");

		dataService.getAllItems(new AsyncCallback<List<Item>>() {

			@Override
			public void onFailure(Throwable caught) {
				Logger.getLogger("Client").warning("FAILURE: " + caught);
			}

			@Override
			public void onSuccess(List<Item> result) {
				Logger.getLogger("Client").info("SUCCESS: " + result.size());
			}
		});


	}

}
