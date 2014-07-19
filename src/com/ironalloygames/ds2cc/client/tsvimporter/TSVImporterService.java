package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tsvimporter")
public interface TSVImporterService {
	void upload(String tsvData);
}
