import java.util.ArrayList;

public class MixxitData {
	private static ArrayList<Mortal> users;
	private static PropertiesFile userdata;

	public static void populate() {
		users = new ArrayList<Mortal>();
		userdata = new PropertiesFile("MixxitPlugin.txt");
		userdata.load();
		for (Player p : etc.getServer().getPlayerList()) {
			addMortal(p);
		}
	}

	public static int getHp(Player player) {
		return getMortal(player).hp;
	}

	public static void setHp(Player player, int hp) {
		getMortal(player).hp = hp;
	}

	public static int getExp(Player player) {
		return getMortal(player).exp;
	}

	public static void setExp(Player player, int exp) {
		getMortal(player).exp = exp;
	}

	public static int getMelee(Player player) {
		return getMortal(player).melee;
	}

	public static void setMelee(Player player, int melee) {
		getMortal(player).melee = melee;
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

	private static Mortal getMortal (Player player) {
		if (users == null) {
			populate();
		}

		for (Mortal m : users) {
			if (m.player == player) {
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

	private static class Mortal {
		public Player player;
		public String name;
		public int hp;
		public int exp;
		public int melee;

		public Mortal(Player player, String key) {
			this.player = player;
			name = player.getName();
			String[] details = key.split("|");
			hp = Integer.decode(details[0]);
			exp = Integer.decode(details[1]);
			melee = Integer.decode(details[2]);
		}

		public String toString() {
			return hp+"|"+exp+"|"+melee;
		}
	}
}
