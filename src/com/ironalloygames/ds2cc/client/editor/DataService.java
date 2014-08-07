package com.ironalloygames.ds2cc.client.editor;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	List<BasicItem> getAllItemsBasicInfo();

	boolean writeItem(Item item);
}
