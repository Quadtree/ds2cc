package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.DataService;
import com.ironalloygames.ds2cc.shared.data.Item;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	private static final String ALL_ITEMS_BASIC_INFO_CACHE_KEY = "ALL_ITEMS_INFO";

	/**
	 *
	 */
	private static final long serialVersionUID = -3362482680317315145L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getAllItems() {
		List<Item> retItems = null;
		MemcacheService ms = MemcacheServiceFactory.getMemcacheService();

		try {
			retItems = (ArrayList<Item>) ms.get(ALL_ITEMS_BASIC_INFO_CACHE_KEY);
		} catch (Exception ex) {
			// log the error and continue
			Logger.getGlobal().warning(ex.toString());
		}

		if (retItems == null && !ms.contains(ALL_ITEMS_BASIC_INFO_CACHE_KEY)) {
			Logger.getGlobal().info("Cache miss");
			retItems = ItemDataService.getInstance().getAllItems();
			ms.put(ALL_ITEMS_BASIC_INFO_CACHE_KEY, retItems);
		}

		Logger.getGlobal().info("RESP " + retItems.size());

		return retItems;
	}

}
