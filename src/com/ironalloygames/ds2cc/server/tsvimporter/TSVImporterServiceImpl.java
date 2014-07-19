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

					// if it has one compatibility column, assume that it has
					// them all
					if (columnNames.containsKey("Phys")) {
						ar.setStatModifier(Stat.PHYSICAL_RESISTANCE, Float.parseFloat(columns[columnNames.get("Phys")]));
						ar.setStatModifier(Stat.STRIKE_RESISTANCE, Float.parseFloat(columns[columnNames.get("Str")]));
						ar.setStatModifier(Stat.SLASH_RESISTANCE, Float.parseFloat(columns[columnNames.get("Sls")]));
						ar.setStatModifier(Stat.THRUST_RESISTANCE, Float.parseFloat(columns[columnNames.get("Thr")]));
						ar.setStatModifier(Stat.MAGIC_RESISTANCE, Float.parseFloat(columns[columnNames.get("Mag")]));
						ar.setStatModifier(Stat.FIRE_RESISTANCE, Float.parseFloat(columns[columnNames.get("Fire")]));
						ar.setStatModifier(Stat.LIGHTNING_RESISTANCE, Float.parseFloat(columns[columnNames.get("Light")]));
						ar.setStatModifier(Stat.DARK_RESISTANCE, Float.parseFloat(columns[columnNames.get("Dark")]));
						ar.setStatModifier(Stat.POISE_RESISTANCE, Float.parseFloat(columns[columnNames.get("Poise")]));
						ar.setStatModifier(Stat.POISON_RESISTANCE, Float.parseFloat(columns[columnNames.get("Poison")]));
						ar.setStatModifier(Stat.BLEED_RESISTANCE, Float.parseFloat(columns[columnNames.get("Bleed")]));
						ar.setStatModifier(Stat.PETRIFY_RESISTANCE, Float.parseFloat(columns[columnNames.get("Petrify")]));
						ar.setStatModifier(Stat.CURSE_RESISTANCE, Float.parseFloat(columns[columnNames.get("Curse")]));
					}

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

					if (columnNames.containsKey("Effect") && columns.length > columnNames.get("Prerequisite")) {
						while ((mr = prereqFinder.exec(columns[columnNames.get("Prerequisite")])) != null) {
							ar.setStatRequirement(statNames.get(mr.getGroup(2)), Integer.parseInt(mr.getGroup(1)));
						}
					}

					if (columnNames.containsKey("Effect") && columns.length > columnNames.get("Effect")) {
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

					// check for our own column names...
					for(Stat s : Stat.values())
					{
						if (columnNames.containsKey("+" + s))
							ar.setStatModifier(s, Float.parseFloat(columns[columnNames.get("+" + s)]));

						if (columnNames.containsKey("*" + s))
							ar.setStatMultiplier(s, Float.parseFloat(columns[columnNames.get("*" + s)]));

						if (columnNames.containsKey(">" + s))
							ar.setStatRequirement(s, Float.parseFloat(columns[columnNames.get(">" + s)]));
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

		// ret.append("Name	Phys	Str	Sls	Thr	Mag	Fire	Light	Dark	Poise	Poison	Bleed	Petrify	Curse	Dur	Weight	Prerequisite	Effect\n");

		ret.append("Name\tDur\tWeight\tSlot");

		for (Stat s : Stat.values()) {
			ret.append("\t+" + s);
			ret.append("\t*" + s);
			ret.append("\t>" + s);
		}

		ret.append("\n");

		for (Item itm : (List<Item>) q.execute()) {

			ret.append(itm.getName());
			ret.append("\t");
			ret.append(itm.getDurability());
			ret.append("\t");
			ret.append(itm.getWeight());
			ret.append("\t");
			ret.append(itm.getSlot());

			for (Stat s : Stat.values()) {
				ret.append("\t");
				ret.append(itm.getStatModifier(s));
				ret.append("\t");
				ret.append(itm.getStatMultiplier(s));
				ret.append("\t");
				ret.append(itm.getStatRequirement(s));
			}

			ret.append("\n");
		}

		return ret.toString();
	}

}
