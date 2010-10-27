// THIS IS NOT IN USE YET
// PLEASE WAIT TILL CONVERTED
import java.util.ArrayList;
/**
 * MixxitData.java - Plug-in class for hey0's minecraft mod.
 * @author Shaun (Sturmeh)
 */
public class MixxitData {
	private static ArrayList<Mortal> users;
	private static PropertiesFile userdata;
	
	public static void populate() {
		users = new ArrayList<Mortal>();
		userdata = new PropertiesFile("mixxitUsers.txt");
		userdata.load();
		for (Player p : etc.getServer().getPlayerList()) {
			addMortal(p);
		}
	}

	public static void saveToDisk() {
		if (users == null) {
			populate();
		}

		for (Mortal m : users) {
			userdata.setString(m.name, m.toString());
			if (etc.getServer().getPlayer(m.name) == null) {
				users.remove(m);
			}
		}
	}

	@SuppressWarnings("unused")
	private static Mortal getMortal(Player player) {
		if (users == null) {
			populate();
		}

		for (Mortal m : users) {
			if (m.name.endsWith(player.getName())) {
				return m;
			}
		}

		return addMortal(player);
	}

	private static Mortal addMortal(Player player) {
		String key = userdata.getString(player.getName(), "100|0|0");
		Mortal meer = new Mortal(player, key);
		users.add(meer);
		return meer;
	}

}
