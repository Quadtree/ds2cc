package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ironalloygames.ds2cc.shared.tsvuploader.UploadType;

public interface TSVImporterServiceAsync {
	void upload(String tsvData, UploadType type, AsyncCallback<Void> callback);

	void downloadAll(AsyncCallback<String> callback);
}
