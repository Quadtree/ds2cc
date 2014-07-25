package com.ironalloygames.ds2cc.server.tsvimporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import javax.xml.bind.Unmarshaller;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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


	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int deleted = 0, created = 0;

		try {
			ServletFileUpload upload = new ServletFileUpload();

			FileItemIterator itr = upload.getItemIterator(req);

			while (itr.hasNext()) {
				FileItemStream item = itr.next();

				if (!item.isFormField()) {
					JAXBContext ctx = JAXBContext.newInstance(ItemList.class);

					Unmarshaller mrsh = ctx.createUnmarshaller();

					ItemList itemList = (ItemList) mrsh.unmarshal(item.openStream());

					Logger.getGlobal().info("ITEM LIST: " + itemList + " " + itemList.getItems().size());

					PersistenceManager pm = pmfInstance.getPersistenceManager();

					Query q = pm.newQuery(Item.class);


					for (Item i : (List<Item>) q.execute()) {
						pm.deletePersistent(i);
						Logger.getGlobal().info("Deleting " + i.getName());
						deleted++;
					}

					for (Item i : itemList.getItems()) {
						pm.makePersistent(i);
						Logger.getGlobal().info("Saving " + i.getName());
						created++;
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		Logger.getGlobal().info(deleted + " deleted, " + created + " created");

	}


}
