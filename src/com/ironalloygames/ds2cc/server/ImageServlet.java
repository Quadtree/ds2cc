package com.ironalloygames.ds2cc.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.geronimo.mail.util.Base64;

import com.ironalloygames.ds2cc.shared.data.Item;

public class ImageServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 2651239132565787132L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// MemcacheService ms = MemcacheServiceFactory.getMemcacheService();

		Item item = ItemDataService.getInstance().readItem(req.getParameter("itemName"));

		resp.setContentType("image/png");
		resp.getOutputStream().write(Base64.decode(item.getEncodedImageData()));
	}

}
