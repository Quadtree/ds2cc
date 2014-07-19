package com.ironalloygames.ds2cc.server.tsvimporter;

import java.util.ArrayList;
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
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
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

	@Override
	public void upload(String tsvData, UploadType type) {
		getLogger().info("TSV REC'D: " + tsvData);

		getLogger().info("Upload type is " + type);

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

			HashMap<String, Stat> statNames = new HashMap<>();
			statNames.put("STR", Stat.STRENGTH);
			statNames.put("DEX", Stat.DEXTERITY);
			statNames.put("INT", Stat.INTELLIGENCE);
			statNames.put("FTH", Stat.FAITH);

			RegExp prereqFinder = RegExp.compile("(\\d+) ([A-Z]{3})", "g");

			RegExp nameParser = RegExp.compile("(.+)\\+(\\d+)");

			RegExp bonusFinder = RegExp.compile("([A-Z]{3})\\s*\\+\\s*(\\d+)");
			RegExp compoundBonusFinder = RegExp.compile("([A-Z]{3})\\s*&\\s*([A-Z]{3})\\s*\\+\\s*(\\d+)");
			RegExp fullNameBonusFinder = RegExp.compile("([A-Za-z]+)\\s*\\+\\s*(\\d+)");

			PersistenceManager pm = pmfInstance.getPersistenceManager();

			for (String line : lines)
			{
				try {
					String[] columns = line.split("\t");

					MatchResult nameParseMatchResult = nameParser.exec(columns[columnNames.get("Name")]);

					if (nameParseMatchResult != null)
					{
						if (columns[columnNames.get("Reinforcement")].equals("Titanite") && !nameParseMatchResult.getGroup(2).equals("10"))
							continue;
						if (columns[columnNames.get("Reinforcement")].equals("Twinkling Titanite") && !nameParseMatchResult.getGroup(2).equals("5"))
							continue;

						Item ar = new Item();

						ar.setName(nameParseMatchResult.getGroup(1));

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

						MatchResult mr = null;

						while ((mr = prereqFinder.exec(columns[columnNames.get("Prerequisite")])) != null) {
							ar.setStatRequirement(statNames.get(mr.getGroup(2)), Integer.parseInt(mr.getGroup(1)));
						}

						while ((mr = compoundBonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(statNames.get(mr.getGroup(1)), Integer.parseInt(mr.getGroup(3)));
							ar.setStatModifier(statNames.get(mr.getGroup(2)), Integer.parseInt(mr.getGroup(3)));
						}

						while ((mr = fullNameBonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(Stat.valueOf(mr.getGroup(1).toUpperCase()), Integer.parseInt(mr.getGroup(2)));
						}

						while ((mr = bonusFinder.exec(columns[columnNames.get("Effect")])) != null) {
							ar.setStatModifier(statNames.get(mr.getGroup(1)), Integer.parseInt(mr.getGroup(2)));
						}

						pm.makePersistent(ar);
					}

				} catch (Exception ex) {
					getLogger().info("Failed to parse line due to " + ex);
				}
			}

			pm.close();
		} else {
			getLogger().warning("No rows found!");
		}
	}

	private Logger getLogger() {
		return Logger.getLogger("TSV_Server");
	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadAllAsJSON() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		Query q = pm.newQuery(Item.class);

		ArrayList<Item> retItems = new ArrayList<>();

		for (Item itm : (List<Item>) q.execute()) {
			retItems.add(itm);
		}

		// return JSON.toString(retItems);

		AutoBean<ArrayList<Item>> bean = AutoBeanUtils.getAutoBean(retItems);

		return AutoBeanCodex.encode(bean).getPayload();
	}

}
