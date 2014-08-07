package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.DataService;
import com.ironalloygames.ds2cc.shared.data.Item;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	private static final String ALL_ITEMS_BASIC_INFO_CACHE_KEY = "ALL_ITEMS_BASIC_INFO";

	/**
	 *
	 */
	private static final long serialVersionUID = -3362482680317315145L;

	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getAllItems() {
		ArrayList<Item> retItems = null;
		MemcacheService ms = MemcacheServiceFactory.getMemcacheService();

		try {
			retItems = (ArrayList<Item>) ms.get(ALL_ITEMS_BASIC_INFO_CACHE_KEY);
		} catch (Exception ex) {
			// log the error and continue
			Logger.getGlobal().warning(ex.toString());
		}

		if (retItems == null)
			retItems = doGetAllItemsBasicInfo();

		return retItems;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Item> doGetAllItemsBasicInfo() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		ArrayList<Item> retItems = new ArrayList<>();

		for (Item itm : (List<Item>) q.execute()) {
			// if (retItems.size() == 0)
			// Logger.getGlobal().info("ARMOR OF FIRST: " +
			// itm.getStatModifier(Stat.SLASH_RESISTANCE));

			// force hydration. find a better way to do this?
			itm.getStatModifiers();
			itm.getStatMultipliers();
			itm.getStatRequirements();

			// detach it... so it can be reattached later?
			Item detachedItem = pm.detachCopy(itm);

			retItems.add(detachedItem);
		}
		return retItems;
	}

}
