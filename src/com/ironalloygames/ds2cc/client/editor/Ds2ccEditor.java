package com.ironalloygames.ds2cc.client.editor;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Ds2ccEditor implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get("mainContent").add(new EditorPage());
	}

}
