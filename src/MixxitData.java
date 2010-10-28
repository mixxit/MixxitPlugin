import java.util.ArrayList;

public class MixxitData
{
  private static ArrayList<Mortal> users;
  private static PropertiesFile userdata = new PropertiesFile("mixxitUsers.txt");

  private static void populate() {
    users = new ArrayList();
    userdata.load();
    for (Player p : etc.getServer().getPlayerList())
      addMortal(p);
  }

  public static void saveToDisk()
  {
    if (users == null) {
      populate();
    }

    for (Mortal m : users) {
      userdata.setString(m.name, m.toString());
      if (etc.getServer().getPlayer(m.name) == null)
        users.remove(m);
    }
  }

  private static Mortal getMortal(Player player)
  {
    if (users == null) {
      populate();
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
}