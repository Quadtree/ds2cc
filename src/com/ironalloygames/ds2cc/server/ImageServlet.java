package com.ironalloygames.ds2cc.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 2651239132565787132L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);

		// MemcacheService ms = MemcacheServiceFactory.getMemcacheService();


	}

}
