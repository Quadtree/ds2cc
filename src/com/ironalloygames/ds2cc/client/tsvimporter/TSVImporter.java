package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class TSVImporter implements EntryPoint {

	TSVImporterServiceAsync tsvImporterService = GWT
			.create(TSVImporterService.class);

	@Override
	public void onModuleLoad() {

		RootPanel.get("mainContent").add(new TSVImporterLayout());

	}

}
