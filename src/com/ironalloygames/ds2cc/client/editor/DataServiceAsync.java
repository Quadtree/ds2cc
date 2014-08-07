package com.ironalloygames.ds2cc.client.editor;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

public interface DataServiceAsync {
	void getAllItemsBasicInfo(AsyncCallback<List<BasicItem>> callback);
	void readItem(BasicItem basicItem, AsyncCallback<Item> callback);
	void writeItem(Item item, AsyncCallback<Boolean> callback);
}
