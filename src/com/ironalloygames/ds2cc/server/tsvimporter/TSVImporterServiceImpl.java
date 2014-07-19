package com.ironalloygames.ds2cc.server.tsvimporter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.tsvimporter.TSVImporterService;
import com.ironalloygames.ds2cc.shared.data.Armor;
import com.ironalloygames.ds2cc.shared.data.Armor.ResistanceType;

public class TSVImporterServiceImpl extends RemoteServiceServlet implements
		TSVImporterService {

	/**
	 *
	 */
	private static final long serialVersionUID = 9068541989033451667L;

	@Override
	public void upload(String tsvData) {
		getLogger().info("TSV REC'D: " + tsvData);

		String[] lines = tsvData.split("\n");

		getLogger().info(lines.length + " lines extracted");

		if (lines.length > 0)
		{
			// assume the first row is the title row
			HashMap<String, Integer> columnNames = new HashMap<>();

			String[] titleColumns = lines[0].split("\t");

			for (int i = 0; i < titleColumns.length; i++) {
				columnNames.put(titleColumns[i], i);
			}

			getLogger().info("Column names: " + columnNames);


			for (String line : lines)
			{
				try {
					String[] columns = line.split("\t");

					Armor ar = new Armor();

					ar.setName(columns[columnNames.get("Name")]);

					ar.setResistance(ResistanceType.PHYSICAL, Float.parseFloat(columns[columnNames.get("Phys")]));
					ar.setResistance(ResistanceType.STRIKE, Float.parseFloat(columns[columnNames.get("Str")]));
					ar.setResistance(ResistanceType.SLASH, Float.parseFloat(columns[columnNames.get("Sls")]));
					ar.setResistance(ResistanceType.THRUST, Float.parseFloat(columns[columnNames.get("Thr")]));
					ar.setResistance(ResistanceType.MAGIC, Float.parseFloat(columns[columnNames.get("Mag")]));
					ar.setResistance(ResistanceType.FIRE, Float.parseFloat(columns[columnNames.get("Fire")]));
					ar.setResistance(ResistanceType.LIGHTNING, Float.parseFloat(columns[columnNames.get("Light")]));
					ar.setResistance(ResistanceType.DARK, Float.parseFloat(columns[columnNames.get("Dark")]));
					ar.setResistance(ResistanceType.POISE, Float.parseFloat(columns[columnNames.get("Poise")]));
					ar.setResistance(ResistanceType.POISON, Float.parseFloat(columns[columnNames.get("Poison")]));
					ar.setResistance(ResistanceType.BLEED, Float.parseFloat(columns[columnNames.get("Bleed")]));
					ar.setResistance(ResistanceType.PETRIFY, Float.parseFloat(columns[columnNames.get("Petrify")]));
					ar.setResistance(ResistanceType.CURSE, Float.parseFloat(columns[columnNames.get("Curse")]));

				} catch (Exception ex) {
					getLogger().info("Failed to parse line due to " + ex);
				}
			}
		} else {
			getLogger().warning("No rows found!");
		}
	}

	private Logger getLogger() {
		return Logger.getLogger("TSV_Server");
	}

}
