package com.ironalloygames.ds2cc.client.tsvimporter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.ironalloygames.ds2cc.shared.tsvuploader.UploadType;

public class TSVImporterLayout extends Composite {

	private static TSVImporterLayoutUiBinder uiBinder = GWT.create(TSVImporterLayoutUiBinder.class);

	@UiField Button beginUploadButton;
	@UiField Label statusLabel;
	@UiField ListBox uploadType;
	@UiField Button downloadAll;
	@UiField FileUpload fileUpload;

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

	}

	@UiHandler("downloadAll")
	void onDownloadAllAsJSONClick(ClickEvent event) {

	}
}
