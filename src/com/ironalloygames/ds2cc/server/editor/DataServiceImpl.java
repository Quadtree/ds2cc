package com.ironalloygames.ds2cc.server.editor;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.editor.DataService;
import com.ironalloygames.ds2cc.shared.data.BasicItem;
import com.ironalloygames.ds2cc.shared.data.Item;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {


	/**
	 *
	 */
	private static final long serialVersionUID = 4933987611806245157L;

	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicItem> getAllItemsBasicInfo() {

		if (!UserServiceFactory.getUserService().isUserLoggedIn() || !UserServiceFactory.getUserService().isUserAdmin()) {
			return null;
		}

		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		ArrayList<BasicItem> retItems = new ArrayList<>();

		for (BasicItem itm : (List<Item>) q.execute()) {

			Item returnItem = new Item();
			returnItem.setName(itm.getName());
			returnItem.setSlot(itm.getSlot());

			retItems.add(returnItem);
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
