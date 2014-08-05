package com.ironalloygames.ds2cc.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ironalloygames.ds2cc.shared.data.Item;

public interface DataServiceAsync {
	void getAllItems(AsyncCallback<List<Item>> callback);

	void writeItem(Item item, AsyncCallback<Boolean> callback);
}
