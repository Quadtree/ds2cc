package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.ItemKey;

public class ItemDataService {
	static ItemDataService singleton;

	public static synchronized ItemDataService getInstance() {
		if (singleton == null)
			singleton = new ItemDataService();

		return singleton;
	}

	private final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	public List<ItemKey> getAllItemKeys() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		ArrayList<ItemKey> retItems = new ArrayList<>();

		for (Item itm : (List<Item>) q.execute()) {
			retItems.add(itm.copyAsBasicItem());
		}

		return retItems;
	}

	public Item readItem(ItemKey key)
	{
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			return pm.getObjectById(Item.class, KeyFactory.createKey(Item.class.getSimpleName(), key.getName()));
		} catch (Exception ex) {
			Logger.getGlobal().warning(ex.toString());
			return null;
		}
	}

	public void writeItem(Item item) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		pm.makePersistent(item);
	}
}
