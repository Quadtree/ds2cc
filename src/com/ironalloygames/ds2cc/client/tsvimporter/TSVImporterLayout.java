package com.ironalloygames.ds2cc.client.tsvimporter;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.shared.tsvuploader.UploadType;

public class TSVImporterLayout extends Composite {

	private static TSVImporterLayoutUiBinder uiBinder = GWT.create(TSVImporterLayoutUiBinder.class);

	@UiField Button beginUploadButton;
	@UiField TextArea tsvUploadBody;
	@UiField Label statusLabel;
	@UiField ListBox uploadType;

	TSVImporterServiceAsync tsvImporterService = GWT
			.create(TSVImporterService.class);

	interface TSVImporterLayoutUiBinder extends UiBinder<Widget, TSVImporterLayout> {
	}

	public TSVImporterLayout() {
		initWidget(uiBinder.createAndBindUi(this));

		for (UploadType type : UploadType.values()) {
			uploadType.addItem(type.toString());
		}
	}

	@UiHandler("beginUploadButton")
	void onBeginUploadButtonClick(ClickEvent event) {
		tsvImporterService.upload(tsvUploadBody.getText(), UploadType.valueOf(uploadType.getItemText(uploadType.getSelectedIndex())), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				statusLabel.setText("Failure!: " + caught);
			}

			@Override
			public void onSuccess(Void result) {
				statusLabel.setText("Success at " + new Date());
			}
		});
	}
}
