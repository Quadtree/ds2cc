package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TSVImporterServiceAsync {
	void upload(String tsvData, AsyncCallback<Void> callback);
}
