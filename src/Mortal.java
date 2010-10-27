import java.util.HashMap;
/**
 * Mortal.java - Plug-in class for hey0's minecraft mod.
 * @author Shaun (Sturmeh)
 */
public class Mortal {
	private HashMap<VarInt, Integer> intField;
	private static PropertiesFile defaultdata = new PropertiesFile("mixxitDefaults.txt");
	public String name;
	
	public enum VarInt {
		HP,
		EXP,
		MELEE
	}

	public Mortal(Player player, String key) {
		intField = new HashMap<VarInt, Integer>();
		name = player.getName();
		String[] details = key.split("|");
		int i = 0;
		for (VarInt val : VarInt.values()) {
			if (i < details.length)
				intField.put(val, Integer.decode(details[i]));
			else
				intField.put(val, getDefault(val));
		}
	}
	
	public int get(VarInt value) {
		return intField.get(value);
	}

	public String toString() {
		String key = "";
		for (VarInt val : VarInt.values()) {
			key += intField.get(val);
			
			if (val.ordinal() < VarInt.values().length - 1)
				key += "|";
		}
		return key;
	}
	
	private int getDefault(VarInt value) {
		return defaultdata.getInt(value.toString(), 0);
	}
}
