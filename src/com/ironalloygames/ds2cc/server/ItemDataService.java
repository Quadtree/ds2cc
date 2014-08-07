package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

public class ItemDataService {
	static ItemDataService singleton;

	public static synchronized ItemDataService getInstance() {
		if (singleton == null)
			singleton = new ItemDataService();

		return singleton;
	}

	private final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	public List<BasicItem> getAllBasicItems() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		ArrayList<BasicItem> retItems = new ArrayList<>();

		for (Item itm : (List<Item>) q.execute()) {
			retItems.add(itm.copyAsBasicItem());
		}

		return retItems;
	}

	public Item readItem(BasicItem key)
	{
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			return (Item) pm.getObjectById(KeyFactory.createKey(BasicItem.class.getSimpleName(), key.getName()));
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
