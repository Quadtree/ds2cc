package com.ironalloygames.ds2cc.server.tsvimporter;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ironalloygames.ds2cc.client.tsvimporter.TSVImporterService;
import com.ironalloygames.ds2cc.shared.data.Item;
import com.ironalloygames.ds2cc.shared.data.ResistanceType;
import com.ironalloygames.ds2cc.shared.data.Slot;
import com.ironalloygames.ds2cc.shared.data.Stat;
import com.ironalloygames.ds2cc.shared.tsvuploader.UploadType;

public class TSVImporterServiceImpl extends RemoteServiceServlet implements
		TSVImporterService {

	/**
	 *
	 */
	private static final long serialVersionUID = 9068541989033451667L;

	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@SuppressWarnings("incomplete-switch")
	@Override
	public void upload(String tsvData, UploadType type) {
		getLogger().info("TSV REC'D: " + tsvData);

		getLogger().info("Upload type is " + type);

		String[] lines = tsvData.split("\n");

		getLogger().info(lines.length + " lines extracted");

		int numSaved = 0;

		if (lines.length > 0)
		{
			// assume the first row is the title row
			HashMap<String, Integer> columnNames = new HashMap<>();

			String[] titleColumns = lines[0].split("\t");

			for (int i = 0; i < titleColumns.length; i++) {
				columnNames.put(titleColumns[i], i);
			}

			getLogger().info("Column names: " + columnNames);

			HashMap<String, Stat> statNames = new HashMap<>();
			for (Stat stat : Stat.values()) {
				statNames.put(stat.getAbbrev(), stat);
			}

			RegExp prereqFinder = RegExp.compile("(\\d+) ([A-Z]{3})", "g");

			RegExp nameParser = RegExp.compile("(.+)\\+(\\d+)");

			RegExp bonusFinder = RegExp.compile("([A-Z]{3})\\s*\\+\\s*(\\d+)", "g");
			RegExp compoundBonusFinder = RegExp.compile("([A-Z]{3})\\s*&\\s*([A-Z]{3})\\s*\\+\\s*(\\d+)", "g");
			RegExp fullNameBonusFinder = RegExp.compile("([A-Za-z]{4,})\\s*\\+\\s*(\\d+)", "g");

			PersistenceManager pm = pmfInstance.getPersistenceManager();

			for (String line : lines)
			{
				boolean saved = false;

				try {
					String[] columns = line.split("\t");

					MatchResult nameParseMatchResult = nameParser.exec(columns[columnNames.get("Name")]);

					Item ar = new Item();

					if (nameParseMatchResult != null && type != UploadType.COMPLETE_DATA_SET)
					{
						if (columns[columnNames.get("Reinforcement")].equals("Titanite") && !nameParseMatchResult.getGroup(2).equals("10"))
							continue;
						if (columns[columnNames.get("Reinforcement")].equals("Twinkling Titanite") && !nameParseMatchResult.getGroup(2).equals("5"))
							continue;

						ar.setName(nameParseMatchResult.getGroup(1));
					} else {
						if (type != UploadType.COMPLETE_DATA_SET)
							continue;

						ar.setName(columns[columnNames.get("Name")]);
					}

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

					ar.setDurability(Integer.parseInt(columns[columnNames.get("Dur")]));
					ar.setWeight(Float.parseFloat(columns[columnNames.get("Weight")]));

					switch (type)
					{
					case TSV_HEAD:
						ar.setSlot(Slot.HEAD);
						break;
					case TSV_LEGS:
						ar.setSlot(Slot.LEGS);
						break;
					case TSV_CHEST:
						ar.setSlot(Slot.CHEST);
						break;
					case TSV_HANDS:
						ar.setSlot(Slot.HANDS);
						break;
					}

					if (columnNames.containsKey("Slot"))
					{
						ar.setSlot(Slot.valueOf(columns[columnNames.get("Slot")]));
					}

					MatchResult mr = null;

					if (columns.length > columnNames.get("Prerequisite")) {
						while ((mr = prereqFinder.exec(columns[columnNames.get("Prerequisite")])) != null) {
							ar.setStatRequirement(statNames.get(mr.getGroup(2)), Integer.parseInt(mr.getGroup(1)));
						}
					}

					if (columns.length > columnNames.get("Effect")) {
						while ((mr = compoundBonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(statNames.get(mr.getGroup(1)), Integer.parseInt(mr.getGroup(3)));
							ar.setStatModifier(statNames.get(mr.getGroup(2)), Integer.parseInt(mr.getGroup(3)));
							// getLogger().info("COMPOUND BONUS FOUND " +
							// mr.getGroup(1) + " " + mr.getGroup(2) + " " +
							// mr.getGroup(3));
						}

						while ((mr = fullNameBonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(Stat.valueOf(mr.getGroup(1).toUpperCase()), Integer.parseInt(mr.getGroup(2)));
							// getLogger().info("FULL BONUS FOUND " +
							// mr.getGroup(1) + " " + mr.getGroup(2));
						}

						while ((mr = bonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(statNames.get(mr.getGroup(1)), Integer.parseInt(mr.getGroup(2)));
							// getLogger().info("BONUS FOUND " + mr.getGroup(1)
							// + " " + mr.getGroup(2));
						}
					}

					pm.makePersistent(ar);
					saved = true;

				} catch (Exception ex) {
					getLogger().info("Failed to parse line due to " + ex);
				}

				if (!saved)
					getLogger().warning("Failed to save " + line);

				if (saved)
					numSaved++;
			}

			pm.close();
		} else {
			getLogger().warning("No rows found!");
		}

		getLogger().info(numSaved + " saved");
	}

	private Logger getLogger() {
		return Logger.getLogger("TSV_Server");
	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadAll() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		StringBuilder ret = new StringBuilder();

		ret.append("Name	Phys	Str	Sls	Thr	Mag	Fire	Light	Dark	Poise	Poison	Bleed	Petrify	Curse	Dur	Weight	Prerequisite	Effect\n");

		for (Item itm : (List<Item>) q.execute()) {

			StringBuilder prereq = new StringBuilder();

			for(Stat stat : Stat.values()){
				if (itm.getStatRequirement(stat) > 0){
					prereq.append(" " + itm.getStatRequirement(stat) + " " + stat.getAbbrev());
				}
			}

			StringBuilder effect = new StringBuilder();

			for (Stat stat : Stat.values())
			{
				if (itm.getStatModifier(stat) > 0) {
					effect.append(" " + stat.getAbbrev() + "+" + itm.getStatModifier(stat));
				}
			}

			String[] elements = {
					itm.getName(),
					"" + itm.getResistance(ResistanceType.PHYSICAL),
					"" + itm.getResistance(ResistanceType.STRIKE),
					"" + itm.getResistance(ResistanceType.SLASH),
					"" + itm.getResistance(ResistanceType.THRUST),
					"" + itm.getResistance(ResistanceType.MAGIC),
					"" + itm.getResistance(ResistanceType.FIRE),
					"" + itm.getResistance(ResistanceType.LIGHTNING),
					"" + itm.getResistance(ResistanceType.DARK),
					"" + itm.getResistance(ResistanceType.POISE),
					"" + itm.getResistance(ResistanceType.POISON),
					"" + itm.getResistance(ResistanceType.BLEED),
					"" + itm.getResistance(ResistanceType.PETRIFY),
					"" + itm.getResistance(ResistanceType.CURSE),
					"" + itm.getDurability(),
					"" + itm.getWeight(),
					"" + prereq.toString(),
					"" + effect.toString()
			};

			for (String s : elements) {
				ret.append(s + "\t");
			}
			ret.append("\n");
		}

		return ret.toString();
	}

}
