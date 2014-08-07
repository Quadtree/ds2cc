package com.ironalloygames.ds2cc.server.editor;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.editor.DataService;
import com.ironalloygames.ds2cc.server.ItemDataService;
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

		return ItemDataService.getInstance().getAllBasicItems();
	}

	@Override
	public boolean writeItem(Item item)
	{
		if (!UserServiceFactory.getUserService().isUserLoggedIn() || !UserServiceFactory.getUserService().isUserAdmin()) {
			return false;
		}

		ItemDataService.getInstance().writeItem(item);

		return true;
	}

	@Override
	public Item readItem(BasicItem basicItem) {
		if (!UserServiceFactory.getUserService().isUserLoggedIn() || !UserServiceFactory.getUserService().isUserAdmin()) {
			return null;
		}

		return ItemDataService.getInstance().readItem(basicItem);
	}

}
