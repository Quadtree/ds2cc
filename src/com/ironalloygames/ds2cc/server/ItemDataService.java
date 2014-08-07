package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
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
	public List<Item> getAllItems() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		pm.getFetchPlan().setGroup(FetchPlan.ALL);

		Query q = pm.newQuery(Item.class);

		ArrayList<Item> retItems = new ArrayList<>();

		for (Item itm : (List<Item>) q.execute()) {
			Item detachedCopy = pm.detachCopy(itm);
			detachedCopy.getStatModifiers();
			detachedCopy.getStatMultipliers();
			detachedCopy.getStatRequirements();
			detachedCopy.setEncodedImageData("");
			retItems.add(detachedCopy);
		}

		return retItems;
	}

	public Item readItem(String itemName)
	{
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			return pm.getObjectById(Item.class, KeyFactory.createKey(Item.class.getSimpleName(), itemName));
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
