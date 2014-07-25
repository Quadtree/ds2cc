package com.ironalloygames.ds2cc.server.tsvimporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.ItemList;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024, maxRequestSize = 1024 * 1024)
public class XMLImporterServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 2558632788879987602L;

	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		List<Item> items = (List<Item>) q.execute();

		for (Item i : items)
			i.filterInternalData();

		ItemList itemList = new ItemList();
		itemList.setItems(new ArrayList<Item>(items));

		resp.setContentType("text/xml");

		try {
			JAXBContext ctx = JAXBContext.newInstance(ItemList.class);

			Marshaller mrsh = ctx.createMarshaller();
			mrsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			mrsh.marshal(itemList, resp.getOutputStream());

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


	}


}
