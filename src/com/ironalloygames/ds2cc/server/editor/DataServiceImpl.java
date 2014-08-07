package com.ironalloygames.ds2cc.server.editor;

import java.util.List;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.editor.DataService;
import com.ironalloygames.ds2cc.server.ItemDataService;
import com.ironalloygames.ds2cc.shared.data.Item;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {


	/**
	 *
	 */
	private static final long serialVersionUID = 4933987611806245157L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getAllItems() {

		if (!UserServiceFactory.getUserService().isUserLoggedIn() || !UserServiceFactory.getUserService().isUserAdmin()) {
			return null;
		}

		return ItemDataService.getInstance().getAllItems();
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

}
