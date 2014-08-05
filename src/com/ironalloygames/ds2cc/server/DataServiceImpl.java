package com.ironalloygames.ds2cc.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.DataService;
import com.ironalloygames.ds2cc.shared.data.Item;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	/**
	 *
	 */
	private static final long serialVersionUID = -3362482680317315145L;

	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getAllItems() {
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

	@Override
	public boolean writeItem(Item item)
	{
		if (!UserServiceFactory.getUserService().isUserLoggedIn() || !UserServiceFactory.getUserService().isUserAdmin()) {
			return false;
		}

		PersistenceManager pm = pmfInstance.getPersistenceManager();

		pm.makePersistent(item);

		pm.flush();

		return true;
	}

}
