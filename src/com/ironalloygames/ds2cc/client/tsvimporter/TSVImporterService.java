package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ironalloygames.ds2cc.shared.tsvuploader.UploadType;

@RemoteServiceRelativePath("tsvimporter")
public interface TSVImporterService extends RemoteService {
	void upload(String tsvData, UploadType type);

	String downloadAll();
}
