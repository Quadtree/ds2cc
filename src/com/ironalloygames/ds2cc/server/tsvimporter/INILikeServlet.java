package com.ironalloygames.ds2cc.server.tsvimporter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
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

import com.ironalloygames.ds2cc.shared.data.Item;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024, maxRequestSize = 1024 * 1024)
public class INILikeServlet extends HttpServlet {

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

		INILikeSerializer ser = new INILikeSerializer();

		String serialized = ser.serializeList((List<Item>) q.execute());

		resp.setContentType("text/plain");

		Writer w = new OutputStreamWriter(resp.getOutputStream());
		w.write(serialized);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Reader rdr = new InputStreamReader(req.getInputStream());

		CharBuffer cb = CharBuffer.allocate(1024 * 1024);

		while (req.getInputStream().) {
		}

		MultipartSplitter sp = new MultipartSplitter();
		sp.getParts(cb.toString(), req.getHeader("Content-Type"));
	}


}
