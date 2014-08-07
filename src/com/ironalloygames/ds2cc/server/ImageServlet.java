package com.ironalloygames.ds2cc.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.geronimo.mail.util.Base64;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.ironalloygames.ds2cc.shared.data.Item;

public class ImageServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 2651239132565787132L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		byte[] rawImageData = null;
		String memcacheKey = "IMAGE_" + req.getParameter("itemName").replaceAll("[A-Za-z '^]", "");
		MemcacheService ms = MemcacheServiceFactory.getMemcacheService();

		try {
			rawImageData = (byte[])ms.get(memcacheKey);
		} catch(Exception ex){
			Logger.getGlobal().warning(ex.toString());
		}

		if (rawImageData == null && !ms.contains(memcacheKey))
		{
			Item item = ItemDataService.getInstance().readItem(req.getParameter("itemName"));

			if (item != null) {
				rawImageData = Base64.decode(item.getEncodedImageData().getBytes());
			}
		}

		resp.setContentType("image/png");
		resp.getOutputStream().write(rawImageData);
	}

}
