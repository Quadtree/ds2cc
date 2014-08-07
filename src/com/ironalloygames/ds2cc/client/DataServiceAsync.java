package com.ironalloygames.ds2cc.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

public interface DataServiceAsync {
	void getAllBasicItems(AsyncCallback<List<BasicItem>> callback);

	void readItem(BasicItem key, AsyncCallback<Item> callback);
}
