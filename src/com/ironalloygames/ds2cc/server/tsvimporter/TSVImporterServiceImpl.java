package com.ironalloygames.ds2cc.server.tsvimporter;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.tsvimporter.TSVImporterService;

public class TSVImporterServiceImpl extends RemoteServiceServlet implements
		TSVImporterService {

	/**
	 *
	 */
	private static final long serialVersionUID = 9068541989033451667L;

	@Override
	public void upload(String tsvData) {
		Logger.getLogger("Server").warning("TSV REC'D: " + tsvData);
	}

}
