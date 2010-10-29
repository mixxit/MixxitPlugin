import java.util.HashMap;
/**
 * Mortal.java - Plug-in for hey0's minecraft mod.
 * DO NOT REMOVE. DO NOT RESTYLE.
 * @author Shaun (Sturmeh)
 */
public class Mortal {
	private HashMap<Mortal.VarInt, Integer> intField;
	private static PropertiesFile defaultdata = new PropertiesFile("mixxitDefaults.txt");
	public String name;
	
	public static enum VarInt {
		HP, 
		EXP, 
		MELEE,
		LEVEL,
		FACTION,
		COMBATLOG,
		GUILD,
		STAT_STR,
		STAT_AGI,
		STAT_DEX,
		STAT_INT,
		STAT_WIS,
		STAT_CHA,
		STAT_LCK,
		LASTMOVE
	}

	public Mortal(Player player, String key) {
		this.intField = new HashMap<VarInt, Integer>();
		this.name = player.getName();
		String[] details = key.split("|");
		int i = 0;
		for (Mortal.VarInt val : Mortal.VarInt.values())
			if (i < details.length)
				this.intField.put(val, Integer.decode(details[i]));
			else
				this.intField.put(val, Integer.valueOf(getDefault(val)));
	}

	public int get(Mortal.VarInt value) {
		return this.intField.get(value).intValue();
	}

	public String toString() {
		String key = "";
		for (Mortal.VarInt val : Mortal.VarInt.values()) {
			key = key + this.intField.get(val);

			if (val.ordinal() < Mortal.VarInt.values().length - 1)
				key = key + "|";
		}
		return key;
	}

	private static int getDefault(Mortal.VarInt value) {
		return defaultdata.getInt(value.toString(), 0);
	}
}