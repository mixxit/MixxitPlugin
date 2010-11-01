import java.util.ArrayList;
/**
 * MixxitData.java - Plug-in for hey0's minecraft mod.
 * DO NOT REMOVE. DO NOT RESTYLE.
 * @author Shaun (Sturmeh)
 */
public class MixxitData {
	private static long lastaccess = etc.getServer().getTime();
	private static ArrayList<Mortal> users;
	private static PropertiesFile userdata = new PropertiesFile("MixxitPlugin.users");

	public static Mortal getMortal(Player player) {
		if (users == null) {
			populate();
		} else {
			long diff = etc.getServer().getTime() - lastaccess;
			if (diff > 1000 || diff < 0) {
				lastaccess = etc.getServer().getTime();
				saveToDisk();
			}
		}

		for (Mortal m : users) {
			if (m.name.equals(player.getName())) {
				return m;
			}
		}

		return addMortal(player);
	}

	private static Mortal addMortal(Player player) {
		String key = userdata.getString(player.getName(), new Mortal(player, "").toString());
		Mortal meer = new Mortal(player, key);
		users.add(meer);
		return meer;
	}

	private static void populate() {
		users = new ArrayList<Mortal>();
		userdata.load();
		ArrayList<Player> players = new ArrayList<Player>(etc.getServer().getPlayerList());
		for (Player p : players)
			addMortal(p);
	}

	private static void saveToDisk() {
		for (Mortal m : users) {
			userdata.setString(m.name, m.toString());
			if (etc.getServer().getPlayer(m.name) == null)
				users.remove(m);
		}
	}
}