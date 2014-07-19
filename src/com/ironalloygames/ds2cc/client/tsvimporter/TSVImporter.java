package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class TSVImporter implements EntryPoint {

	TSVImporterServiceAsync tsvImporterService = GWT
			.create(TSVImporterService.class);

	@Override
	public void onModuleLoad() {
		final TextArea ta = new TextArea();
		final Button b = new Button();

		ta.setWidth("1000px");
		ta.setHeight("800px");

		b.setText("Upload");
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				tsvImporterService.upload(ta.getText(),
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								ta.setText("Upload Successful");
							}

							@Override
							public void onFailure(Throwable caught) {
								ta.setText("Upload FAILED!");
							}
						});

			}
		});

		RootPanel.get("mainContent").add(ta);
		RootPanel.get("mainContent").add(b);

	}

}
