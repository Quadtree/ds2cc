package com.ironalloygames.ds2cc.client.editor;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.ItemKey;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	List<ItemKey> getAllItemKeys();

	Item readItem(ItemKey ItemKey);
	boolean writeItem(Item item);
}
