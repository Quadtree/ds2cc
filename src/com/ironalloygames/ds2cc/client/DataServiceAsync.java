package com.ironalloygames.ds2cc.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.ItemKey;

public interface DataServiceAsync {
	void getAllItemKeys(AsyncCallback<List<ItemKey>> callback);

	void readItem(ItemKey key, AsyncCallback<Item> callback);
}
