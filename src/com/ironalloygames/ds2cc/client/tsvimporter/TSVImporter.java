package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class TSVImporter implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final TextArea ta = new TextArea();

		RootPanel.get("mainContent").add(ta);
	}

}
