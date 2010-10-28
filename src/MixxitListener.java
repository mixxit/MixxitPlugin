import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

public class MixxitListener extends PluginListener
{
  public PropertiesFile properties = new PropertiesFile("MixxitPlugin.properties");

  boolean pvp = false;
  boolean dropinventory = true;

  public int totaldmg = 0;
  public int totalplydmg = 0;
  public int countcompress2 = 0;
  public int countcompress3 = 0;
  public int countcompress4 = 0;
  public int countcompress5 = 0;

  int Combattimer = 700;

  int woodensword = 6;
  int stonesword = 7;
  int ironsword = 8;
  int goldsword = 10;
  int diamondsword = 20;
  int woodenspade = 4;
  int stonespade = 5;
  int ironspade = 6;
  int goldspade = 8;
  int diamondspade = 10;
  int woodenpickaxe = 4;
  int stonepickaxe = 5;
  int ironpickaxe = 6;
  int goldpickaxe = 8;
  int diamondpickaxe = 10;
  int woodenaxe = 5;
  int stoneaxe = 6;
  int ironaxe = 7;
  int goldaxe = 10;
  int diamondaxe = 18;
  int basedamage = 3;

  int goldenapple = 100;
  int friedbacon = 20;
  int apple = 10;
  int bread = 15;
  public Timer timer;
  public Timer saveTimer;
  public ArrayList<MixxitListener.p1> playerList;

  public MixxitListener()
  {
    this.timer = new Timer();

    this.timer.scheduleAtFixedRate(new RemindTask(this), 0L, this.Combattimer);

    System.out.println(getDateTime() + " [INFO] Melee Combat Task Scheduled.");
    this.playerList = new ArrayList();

    loadPlayerList();
    loadProperties();

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new SaveCombat(this), 5000L, 20000L);

    System.out.println(getDateTime() + " [INFO] Combat saving scheduled.");
  }

  public void loadProperties()
  {
    this.properties = new PropertiesFile("MixxitPlugin.properties");
    this.properties.load();
    try
    {
      this.pvp = this.properties.getBoolean("pvp", false);
      this.dropinventory = this.properties.getBoolean("drop-inventory", true);
      this.Combattimer = this.properties.getInt("combat-timer", 700);
      this.woodensword = this.properties.getInt("wooden-sword", 6);
      this.stonesword = this.properties.getInt("stone-sword", 7);
      this.ironsword = this.properties.getInt("iron-sword", 8);
      this.goldsword = this.properties.getInt("gold-sword", 10);
      this.diamondsword = this.properties.getInt("diamond-sword", 20);
      this.woodenspade = this.properties.getInt("wooden-spade", 4);
      this.stonespade = this.properties.getInt("stone-spade", 5);
      this.ironspade = this.properties.getInt("iron-spade", 6);
      this.goldspade = this.properties.getInt("gold-spade", 8);
      this.diamondspade = this.properties.getInt("diamond-spade", 10);
      this.woodenpickaxe = this.properties.getInt("wooden-pickaxe", 4);
      this.stonepickaxe = this.properties.getInt("stone-pickaxe", 5);
      this.ironpickaxe = this.properties.getInt("iron-pickaxe", 6);
      this.goldpickaxe = this.properties.getInt("gold-pickaxe", 8);
      this.diamondpickaxe = this.properties.getInt("diamond-pickaxe", 10);
      this.woodenaxe = this.properties.getInt("wooden-axe", 5);
      this.stoneaxe = this.properties.getInt("stone-axe", 6);
      this.ironaxe = this.properties.getInt("iron-axe", 7);
      this.goldaxe = this.properties.getInt("gold-axe", 10);
      this.diamondaxe = this.properties.getInt("diamond-axe", 18);
      this.goldenapple = this.properties.getInt("goldenapple", 100);
      this.friedbacon = this.properties.getInt("friedbacon", 20);
      this.apple = this.properties.getInt("apple", 10);
      this.bread = this.properties.getInt("bread", 15);
    }
    catch (Exception localException)
    {
    }
  }

  public void loadPlayerList()
  {
    try
    {
      DataInputStream in = new DataInputStream(new FileInputStream("MixxitPlugin.txt"));
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
      {
        String line;
        if (line.substring(0, 1).matches("[#]"))
        {
          System.out.println(getDateTime() + " [DEBUG] Comment Skipped");
        }
        else
        {
          String slashedstring = line.replace("\\:", ":");

          String[] tokens = slashedstring.split("=");

          String[] params = tokens[1].split(":");

          int curhp = Integer.parseInt(params[0]);

          int curexp = Integer.parseInt(params[1]);

          int curmelee = Integer.parseInt(params[2]);

          MixxitListener.p1 curplayer = new MixxitListener.p1(tokens[0], curhp);
          curplayer.exp = curexp;
          curplayer.melee = curmelee;

          this.playerList.add(curplayer);
          System.out.println(getDateTime() + " [DEBUG] new player: " + curplayer.name + " added with: " + curplayer.hp + ":" + curplayer.exp + ":" + curplayer.melee);
        }

      }

      in.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void packParameters()
  {
  }

  public void packPlayers()
  {
    PropertiesFile configPlayers = new PropertiesFile("MixxitPlugin.txt");
    for (int i = 0; i < this.playerList.size(); i++) {
      String playerData = ((MixxitListener.p1)this.playerList.get(i)).hp + ":" + ((MixxitListener.p1)this.playerList.get(i)).exp + ":" + ((MixxitListener.p1)this.playerList.get(i)).melee;
      configPlayers.setString(((MixxitListener.p1)this.playerList.get(i)).name, playerData);
    }
  }

  private String getDateTime()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  private double getDistance(Player a, Mob b)
  {
    double xPart = Math.pow(a.getX() - b.getX(), 2.0D);
    double yPart = Math.pow(a.getY() - b.getY(), 2.0D);
    double zPart = Math.pow(a.getZ() - b.getZ(), 2.0D);
    return Math.sqrt(xPart + yPart + zPart);
  }

  private double getPlayerDistance(Player a, Player b)
  {
    double xPart = Math.pow(a.getX() - b.getX(), 2.0D);
    double yPart = Math.pow(a.getY() - b.getY(), 2.0D);
    double zPart = Math.pow(a.getZ() - b.getZ(), 2.0D);
    return Math.sqrt(xPart + yPart + zPart);
  }

  public int getPlayerHP(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
      {
        return ((MixxitListener.p1)this.playerList.get(i)).hp;
      }
    }
    return 0;
  }

  public void setPlayerHP(Player player, Integer newhp)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
        continue;
      ((MixxitListener.p1)this.playerList.get(i)).hp = newhp.intValue();
    }
  }

  public int getPlayerMelee(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
      {
        return ((MixxitListener.p1)this.playerList.get(i)).melee;
      }
    }
    return 0;
  }

  public void DoPanic(Mob m, Player p, int basedamage)
  {
    double dist = getDistance(p, m);
    if (dist <= 2.0D)
    {
      p.sendMessage("Distance check:" + dist);
      Random generator = new Random();
      int index = generator.nextInt(basedamage);
      int thisdmg = index;

      if (getCombatLog(p) == 1)
      {
        p.sendMessage("The " + m.getName() + " hit you back! For " + thisdmg + " damage! (CurrHP: " + p.getHealth() + ")");
      }

      if (p.getHealth() < thisdmg)
      {
        p.sendMessage("You have been slain!");

        p.teleportTo(etc.getServer().getSpawnLocation());
      }
      else {
        p.setHealth(p.getHealth() - thisdmg);
      }
    }
  }

  public void setCombatLog(Player player, int value)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
        continue;
      ((MixxitListener.p1)this.playerList.get(i)).combatlog = value;
    }
  }

  public int getCombatLog(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
      {
        return ((MixxitListener.p1)this.playerList.get(i)).combatlog;
      }
    }
    return -1;
  }

  public void enableCombatLog(Player player)
  {
    player.sendMessage("Combat Log Enabled - to disable /disablecombatlog");
    setCombatLog(player, 1);
  }

  public void disableCombatLog(Player player)
  {
    player.sendMessage("Combat Log Disabled - to enable /enablecombatlog");
    setCombatLog(player, 0);
  }

  public void compressedCombatLog(Player player)
  {
    player.sendMessage("Combat Log Compressed");
    setCombatLog(player, 2);
  }

  public boolean onCommand(Player player, String[] split)
  {
    if ((split[0].equalsIgnoreCase("/health")) && (player.canUseCommand("/health")))
    {
      player.sendMessage("HP: " + getPlayerHP(player));
      return true;
    }

    if ((split[0].equalsIgnoreCase("/enablecombatlog")) && (player.canUseCommand("/enablecombatlog")))
    {
      enableCombatLog(player);
      return true;
    }
    if ((split[0].equalsIgnoreCase("/disablecombatlog")) && (player.canUseCommand("/disablecombatlog")))
    {
      disableCombatLog(player);
      return true;
    }
    if ((split[0].equalsIgnoreCase("/compressedcombatlog")) && (player.canUseCommand("/compressedcombatlog")))
    {
      compressedCombatLog(player);
      return true;
    }

    if ((split[0].equalsIgnoreCase("/pvpenable")) && (player.canUseCommand("/pvpenable")))
    {
      this.pvp = true;
      player.sendMessage("PVP Enabled");
      return true;
    }

    if ((split[0].equalsIgnoreCase("/pvpdisable")) && (player.canUseCommand("/pvpdisable")))
    {
      this.pvp = false;
      player.sendMessage("PVP Disabled");
      return true;
    }

    if ((split[0].equalsIgnoreCase("/heal")) && (player.canUseCommand("/heal")))
    {
      setPlayerHP(player, Integer.valueOf(100));
      player.sendMessage("You have been fully healed. HP:" + getPlayerHP(player));
      return true;
    }

    if ((split[0].equalsIgnoreCase("/MixxitDebug")) && (player.canUseCommand("/MixxitDebug")))
    {
      player.sendMessage("You have been fully healed. HP: " + getPlayerHP(player));

      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: pvp = " + this.pvp);
      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: drop inventory = " + this.dropinventory);
      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: combat timer = " + this.Combattimer);

      return true;
    }

    return false;
  }

  public void onLogin(Player player)
  {
    int exists = 0;

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName()))
        continue;
      exists = 1;
      player.sendMessage("Welcome back! HP: " + getPlayerHP(player));
    }

    if (exists == 0)
    {
      MixxitListener.p1 play = new MixxitListener.p1(player.getName(), 100);

      this.playerList.add(play);
      player.sendMessage("Welcome, you have been registered by the hp system! HP: " + getPlayerHP(player));
    }
  }

  public void GiveExperience(Player player, int amount)
  {
    player.sendMessage("Pending experience...");

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!((MixxitListener.p1)this.playerList.get(i)).name.equals(player.getName())) {
        continue;
      }
      ((MixxitListener.p1)this.playerList.get(i)).exp += amount;
      player.sendMessage("�eYou gain experience (" + ((MixxitListener.p1)this.playerList.get(i)).exp + ")!");
      Random generator = new Random();
      int index = generator.nextInt(100);

      if (index != 1)
        continue;
      ((MixxitListener.p1)this.playerList.get(i)).melee += 1;
      player.sendMessage("�9You get better at melee! (" + ((MixxitListener.p1)this.playerList.get(i)).melee + ")!");
    }
  }

  public int PlayerHasHit(Player player)
  {
    int melee = getPlayerMelee(player);
    Random generator = new Random();
    int index = generator.nextInt(10);
    if (index + melee > 5)
    {
      return 1;
    }
    return 0;
  }

  public void DropPlayerItems(Player player)
  {
    for (int slot = 0; slot < 36; slot++)
    {
      try
      {
        Item item = player.getInventory().getItemFromSlot(slot);
        int itemid = item.getItemId();
        int amount = item.getAmount();

        player.giveItemDrop(itemid, amount);
      }
      catch (NullPointerException localNullPointerException)
      {
      }

      player.getInventory().removeItem(slot);
    }

    player.getInventory().updateInventory();
  }

  public void DoPlayerDeath(Player player)
  {
    player.sendMessage("You have been slain");

    if (this.dropinventory)
    {
      DropPlayerItems(player);
    }

    player.teleportTo(etc.getServer().getSpawnLocation());
    setPlayerHP(player, Integer.valueOf(100));
  }

  public String getItemName(int itemId)
  {
    String itemname = "fashioned weapon";

    if (itemId == 268)
    {
      itemname = "Wooden Sword";
    }

    if (itemId == 272)
    {
      itemname = "Stone Sword";
    }

    if (itemId == 267)
    {
      itemname = "Iron Sword";
    }

    if (itemId == 283)
    {
      itemname = "Gold Sword";
    }

    if (itemId == 276)
    {
      itemname = "Diamond Sword";
    }

    return itemname;
  }

  public int getItemDamage(int itemId)
  {
    int itembasedamage = this.basedamage;

    if (itemId == 268)
    {
      itembasedamage = this.woodensword;
    }
    if (itemId == 269)
    {
      itembasedamage = this.woodenspade;
    }
    if (itemId == 270)
    {
      itembasedamage = this.woodenpickaxe;
    }
    if (itemId == 271)
    {
      itembasedamage = this.woodenaxe;
    }

    if (itemId == 272)
    {
      itembasedamage = this.stonesword;
    }
    if (itemId == 273)
    {
      itembasedamage = this.stonespade;
    }
    if (itemId == 274)
    {
      itembasedamage = this.stonepickaxe;
    }
    if (itemId == 275)
    {
      itembasedamage = this.stoneaxe;
    }

    if (itemId == 276)
    {
      itembasedamage = this.diamondsword;
    }
    if (itemId == 277)
    {
      itembasedamage = this.diamondspade;
    }
    if (itemId == 278)
    {
      itembasedamage = this.diamondpickaxe;
    }
    if (itemId == 279)
    {
      itembasedamage = this.diamondaxe;
    }

    if (itemId == 267)
    {
      itembasedamage = this.ironsword;
    }
    if (itemId == 256)
    {
      itembasedamage = this.ironspade;
    }
    if (itemId == 257)
    {
      itembasedamage = this.ironpickaxe;
    }
    if (itemId == 258)
    {
      itembasedamage = this.ironaxe;
    }

    if (itemId == 283)
    {
      itembasedamage = this.goldsword;
    }
    if (itemId == 284)
    {
      itembasedamage = this.goldspade;
    }
    if (itemId == 285)
    {
      itembasedamage = this.goldpickaxe;
    }
    if (itemId == 286)
    {
      itembasedamage = this.goldaxe;
    }
    return itembasedamage;
  }

  public int getPlayerDamage(Player player)
  {
    int itemId = player.getItemInHand();

    int damage = getItemDamage(itemId);

    damage += getPlayerMelee(player);

    Random generator = new Random();
    int index = generator.nextInt(damage);

    index++;

    return index;
  }

  public void onArmSwing(Player player)
  {
    int iteminhand = player.getItemInHand();
    Inventory inv;
    if ((iteminhand == 297) || (iteminhand == 260) || (iteminhand == 320) || (iteminhand == 322))
    {
      String item = "";

      if (iteminhand == 322)
      {
        setPlayerHP(player, Integer.valueOf(100));
        player.sendMessage("The golden apple heals you to full health.");
      }

      if (iteminhand == 320)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + 20));
        item = "fried bacon";
      }

      if (iteminhand == 260)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + 10));
        item = "apple";
      }

      if (iteminhand == 297)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + 15));
        item = "bread";
      }

      if (getPlayerHP(player) < 100)
      {
        player.sendMessage("The " + item + " heals you to " + player.getHealth() + ".");
      } else {
        player.sendMessage("The " + item + " heals you to full health.");
        setPlayerHP(player, Integer.valueOf(100));
      }

      inv = player.getInventory();
      inv.removeItem(new Item(iteminhand, 1));
      inv.updateInventory();
    }

    for (Player p : etc.getServer().getPlayerList())
    {
      if ((p == null) || 
        (p.getName() == player.getName())) {
        continue;
      }
      if (!this.pvp)
        continue;
      double dist = getPlayerDistance(player, p);
      if (dist > 2.0D)
        continue;
      if (PlayerHasHit(player) == 0)
      {
        if (getPlayerHP(p) < 1)
        {
          continue;
        }
        if (getCombatLog(player) != 1)
          continue;
        player.sendMessage("�7You try to strike a " + p.getName() + " HP: (" + getPlayerHP(p) + ") but miss! Your HP: " + getPlayerHP(player));
      }
      else
      {
        int thisdmg = getPlayerDamage(player);
        this.totaldmg += thisdmg;

        if (getCombatLog(player) == 1)
        {
          player.sendMessage("You strike " + p.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(player) + " Their HP: " + getPlayerHP(p));
        }
        else if (getCombatLog(player) == 2)
        {
          if (this.countcompress4 == 4)
          {
            player.sendMessage("Total damage done " + this.totaldmg + ". Current Health: " + getPlayerHP(player) + ".");
            this.countcompress4 = 0;
            this.totaldmg = 0;
          } else {
            this.countcompress4 += 1;
          }

        }

        if (getPlayerHP(p) < thisdmg)
        {
          player.sendMessage("You have slain " + p.getName() + "!");
          p.sendMessage("�cYou have been slain by " + player.getName() + "!");

          DoPlayerDeath(p);
        } else {
          setPlayerHP(p, Integer.valueOf(getPlayerHP(p) - thisdmg));
          this.totalplydmg += thisdmg;
          if (getCombatLog(p) == 1)
          {
            p.sendMessage("�cYou have been hit by " + player.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(p) + " Their HP: " + getPlayerHP(player));
          }
          else {
            if (getCombatLog(p) != 2)
              continue;
            if (this.countcompress5 == 4)
            {
              p.sendMessage("Total damage recieved " + this.totalplydmg + " from " + player.getName() + ". Current Health: " + getPlayerHP(p) + ".");
              this.countcompress5 = 0;
              this.totalplydmg = 0;
            } else {
              this.countcompress5 += 1;
            }

          }

        }

      }

    }

    for (Mob m : etc.getServer().getMobList())
    {
      if (m != null) {
        double dist = getDistance(player, m);

        if (dist >= 2.0D)
          continue;
        if (PlayerHasHit(player) == 0)
        {
          if (m.getHealth() < 1)
          {
            continue;
          }

          if (getCombatLog(player) == 1)
          {
            player.sendMessage("�7You try to strike a " + m.getName() + " HP: (" + m.getHealth() + ") but miss! Your HP: " + getPlayerHP(player));
          }
          else
          {
            if (getCombatLog(player) != 2)
              continue;
            if (this.countcompress3 == 6)
            {
              player.sendMessage("You have missed " + this.countcompress3 + " times. Current Health: " + getPlayerHP(player) + ".");
              this.countcompress3 = 0;
            }
            else {
              this.countcompress3 += 1;
            }

          }

        }
        else
        {
          if (m.getHealth() < 1)
          {
            continue;
          }

          int thisdmg = getPlayerDamage(player);

          this.totaldmg += thisdmg;

          if (getCombatLog(player) == 1)
          {
            player.sendMessage("You strike " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage. Your HP: " + getPlayerHP(player));
          }
          else if (getCombatLog(player) == 2)
          {
            if (this.countcompress2 == 4)
            {
              player.sendMessage("Total damage done " + this.totaldmg + ". Current Health: " + getPlayerHP(player) + ".");
              this.countcompress2 = 0;
              this.totaldmg = 0;
            } else {
              this.countcompress2 += 1;
            }

          }

          if (m.getHealth() <= thisdmg)
          {
            player.sendMessage("You have slain a " + m.getName() + "!");
            m.setHealth(0);
            GiveExperience(player, 1);
          } else {
            m.setHealth(m.getHealth() - thisdmg);
          }
        }
      }
    }
  }

  public class p1
  {
    public String name;
    public int hp;
    public int exp = 0;
    public int melee = 0;

    public int combatlog = 1;

    public p1(String name, int hp)
    {
      this.name = name;
      this.hp = hp;
    }
  }
}