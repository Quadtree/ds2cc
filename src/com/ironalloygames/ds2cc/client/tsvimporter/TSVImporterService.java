package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tsvimporter")
public interface TSVImporterService extends RemoteService {
	void upload(String tsvData);
}
