import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

public class MixxitListener extends PluginListener
{
	public class p1 {
		public String name;
		public int hp;
		public int exp = 0;
		public int melee = 0;
		// Combat Log on by default
		public int combatlog = 1;


		public p1(String name, int hp)
		{
			this.name = name;
			this.hp = hp;

		}
	}

	 // Tom316 - Pull our variables over from MixxitPlugin.java where we load in all the information from the properties file.
    boolean pvp = MixxitPlugin.pvp;
    boolean dropinventory = MixxitPlugin.dropinventory;
    int Combattimer = MixxitPlugin.Combattimer;
    
    int woodensword = MixxitPlugin.woodensword;
    int stonesword = MixxitPlugin.stonesword;
    int ironsword = MixxitPlugin.ironsword;
    int goldsword = MixxitPlugin.goldsword;
    int diamondsword = MixxitPlugin.diamondsword;
    
    int woodenspade = MixxitPlugin.woodenspade;
    int stonespade = MixxitPlugin.stonespade;
    int ironspade = MixxitPlugin.ironspade;
    int goldspade = MixxitPlugin.goldspade;
    int diamondspade = MixxitPlugin.diamondspade;
    
    int woodenpickaxe = MixxitPlugin.woodenpickaxe;
    int stonepickaxe = MixxitPlugin.stonepickaxe;
    int ironpickaxe = MixxitPlugin.ironpickaxe;
    int goldpickaxe = MixxitPlugin.goldpickaxe;
    int diamondpickaxe = MixxitPlugin.diamondpickaxe;
    
    int woodenaxe = MixxitPlugin.woodenaxe;
    int stoneaxe = MixxitPlugin.stoneaxe;
    int ironaxe = MixxitPlugin.ironaxe;
    int goldaxe = MixxitPlugin.goldaxe;
    int diamondaxe = MixxitPlugin.diamondaxe;
    
    int basedamage = MixxitPlugin.basedamage;
    
	public Timer timer;
	public Timer saveTimer;
	
	public ArrayList<p1> playerList;
	public MixxitListener()
	{
		this.timer = new Timer();
		// Tom316 increase time to schedule for server overload
		// get from Combattimer property
		this.timer.schedule(new RemindTask(this), Combattimer);
		System.out.println(getDateTime() + " [INFO] Melee Combat Task Scheduled.");
		playerList = new ArrayList<p1>();
		
		loadPlayerList();
		
		// Set save
		this.saveTimer = new Timer();
		this.timer.schedule(new SaveCombat(this), 10000L);
		System.out.println(getDateTime() + " [INFO] Combat saving scheduled.");
        System.out.println(getDateTime() + " [DEBUG] MixxitPlugin - Listener PVP:" + pvp);

		
	}
	
	public void loadPlayerList()
	{
		// populate the playerlist with previous data
		
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("MixxitPlugin.txt"))));
			while ((line = br.readLine()) != null) {
				// loop through each player
				
				// # ? skip it...
				if (line.substring(0,1).matches("[#]"))
				{
					System.out.println(getDateTime() + " [DEBUG] Comment Skipped");
				} else {
				
					// first remove the \'s
					String slashedstring;
					slashedstring = line.replace("\\:",":");
					
					String[] tokens = slashedstring.split("=");
					
					String[] params = tokens[1].split(":");
					
					// hp
					int curhp = Integer.parseInt(params[0]);
					// exp
					int curexp = Integer.parseInt(params[1]);
					// melee					
					int curmelee = Integer.parseInt(params[2]);

					p1 curplayer = new p1(tokens[0], curhp);
					curplayer.exp = curexp;
					curplayer.melee = curmelee;
					
					this.playerList.add(curplayer);
					System.out.println(getDateTime() + " [DEBUG] new player: " + curplayer.name + " added with: " + curplayer.hp + ":" + curplayer.exp + ":" + curplayer.melee);
					
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void packParameters()
	{
		PropertiesFile configProperties = new PropertiesFile("MixxitPlugin.properties");
		configProperties.setBoolean("pvp", pvp);
		configProperties.setBoolean("drop-inventory", dropinventory);
		configProperties.setInt("combat-timer", Combattimer);
		configProperties.setInt("wooden-sword", woodensword);
		configProperties.setInt("stone-sword", stonesword);
		configProperties.setInt("iron-sword", ironsword);
		configProperties.setInt("gold-sword", goldsword);
		configProperties.setInt("diamond-sword", diamondsword);
		configProperties.setInt("wooden-spade", woodenspade);
		configProperties.setInt("stone-spade", stonespade);
		configProperties.setInt("iron-spade", ironspade);
		configProperties.setInt("gold-spade", goldspade);
		configProperties.setInt("diamond-spade", diamondspade);
		configProperties.setInt("wooden-pickaxe", woodenpickaxe);
		configProperties.setInt("stone-pickaxe", stonepickaxe);
		configProperties.setInt("iron-pickaxe", ironpickaxe);
		configProperties.setInt("gold-pickaxe", goldpickaxe);
		configProperties.setInt("diamond-pickaxe", diamondpickaxe);
		configProperties.setInt("wooden-axe", woodenaxe);
		configProperties.setInt("stone-axe", stoneaxe);
		configProperties.setInt("iron-axe", ironaxe);
		configProperties.setInt("gold-axe", goldaxe);
		configProperties.setInt("diamond-axe", diamondaxe);
		// too spammy
		//System.out.println(getDateTime() + " [INFO] MixxitPlugin properties saved.");
	}
	
	public void packPlayers()
	{
		// Packs all players stored in ArrayList playerList
		// into the configPlayers file
		PropertiesFile configPlayers = new PropertiesFile("MixxitPlugin.txt");
		for (int i = 0; i < this.playerList.size(); i++) {
			String playerData = this.playerList.get(i).hp + ":" + this.playerList.get(i).exp + ":" + this.playerList.get(i).melee;
			configPlayers.setString(this.playerList.get(i).name, playerData);
		}
		// too spammy
		//System.out.println(getDateTime() + " [INFO] MixxitPlugin player data saved.");
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
			if (this.playerList.get(i).name == player.getName())
			{
				return this.playerList.get(i).hp;
			}
		}
		return 0;
	}

	public void setPlayerHP(Player player, Integer newhp)
	{
		for (int i = 0; i < this.playerList.size(); i++) {
			if (this.playerList.get(i).name == player.getName())
			{
				this.playerList.get(i).hp = newhp;

			}
		}
	}

	public int getPlayerMelee(Player player)
	{
		for (int i = 0; i < this.playerList.size(); i++) {
			if (this.playerList.get(i).name == player.getName())
			{
				return this.playerList.get(i).melee;
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
			} else {
				// Suppress the combat message, they've disabled it
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
			if (this.playerList.get(i).name == player.getName())
			{
				this.playerList.get(i).combatlog = value;
			}
		}
	}
	
	public int getCombatLog(Player player)
	{
		for (int i = 0; i < this.playerList.size(); i++) {
			if (this.playerList.get(i).name == player.getName())
			{
				return this.playerList.get(i).combatlog;
			}
		}
		return -1;
	}
	
	// stops/starts combat spam
	public void enableCombatLog(Player player)
	{
		player.sendMessage("Combat Log Enabled - to disable /disablecombatlog");
		setCombatLog(player,1);
		
	}
	
	public void disableCombatLog(Player player)
	{
		player.sendMessage("Combat Log Disabled - to enable /enablecombatlog");			
		setCombatLog(player,0);
	}

	// Tom316's pvp toggle and misc commands

	public boolean onCommand(Player player, String[] split)
	{
		if(split[0].equalsIgnoreCase("/health") && player.canUseCommand("/health"))
		{
			// Tom316 - Send a message to the player with his HP Value
			player.sendMessage("HP:" + getPlayerHP(player));
			return true;
		}
		
		if(split[0].equalsIgnoreCase("/enablecombatlog") && player.canUseCommand("/enablecombatlog"))
		{
			// Enable combat log for the player
			enableCombatLog(player);
			return true;
		}
		if(split[0].equalsIgnoreCase("/disablecombatlog") && player.canUseCommand("/disablecombatlog"))
		{
			// Enable combat log for the player
			disableCombatLog(player);
			return true;
		}


		if(split[0].equalsIgnoreCase("/pvpenable") && player.canUseCommand("/pvpenable"))
        {
          // Tom316 - Enable PVP by setting its value to 1
          pvp = true; // Tom316 - false = Disabled, true = Enabled
          player.sendMessage("PVP Enabled");
          return true;
       }

		if(split[0].equalsIgnoreCase("/pvpdisable") && player.canUseCommand("/pvpdisable"))
        {
          // Tom316 - Disable PVP by setting its value to 0
          pvp = false; // Tom316 - false = Disabled, true = Enabled
          player.sendMessage("PVP Disabled");
          return true;
      }

		if(split[0].equalsIgnoreCase("/heal") && player.canUseCommand("/heal"))
		{
			// Tom316 - Set players health to 100
			setPlayerHP(player,100);
			player.sendMessage("You have been fully healed. HP:" + getPlayerHP(player));
			return true;
		}

		return false;
	}

	public void onLogin(Player player)
	{
		// check if the player exists
		int exists = 0;
		System.out.println(getDateTime() + " [DEBUG] MixxitPlugin - PlayerListSize: " + this.playerList.size());    

		for (int i = 0; i < this.playerList.size(); i++) {
			System.out.println(getDateTime() + " [DEBUG] MixxitPlugin " + this.playerList.get(i).name + " against " + player.getName());    
			if (this.playerList.get(i).name == player.getName())
			{
				exists = 1;
				player.sendMessage("Welcome back! HP:" + getPlayerHP(player));
			}
		}

		if (exists == 0)
		{
			p1 play = new p1(player.getName(),100);

			this.playerList.add(play);
			player.sendMessage("Welcome, you have been registered by the hp system! HP:" + getPlayerHP(player));
		}
		// register the player
		//  MixxitListener.p1 thisplayer = new MixxitListener.p1(player.getName(),"100");
		//	  this.playerList[this.playerList.length] = thisplayer;
	}

	public void GiveExperience(Player player, int amount)
	{
		player.sendMessage("Pending experience...");
		int playerfound = 0;
		for (int i = 0; i < this.playerList.size(); i++) {
			if (this.playerList.get(i).name == player.getName())
			{
				playerfound = 1;
				this.playerList.get(i).exp = this.playerList.get(i).exp + amount;
				player.sendMessage("§eYou gain experience (" + this.playerList.get(i).exp + ")!");
				Random generator = new Random();
				int index = generator.nextInt(100);
				// 1 in a hundred chance of skillup
				if (index == 1)
				{
					this.playerList.get(i).melee = this.playerList.get(i).melee + 1;
					player.sendMessage("§9You get better at melee! (" + this.playerList.get(i).melee + ")!");

				}

			}
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
		// drop items
		// Tom316 - Loop through the inventory slots removing each item.
		for(int slot=0;slot<36;slot++)
		{
			// what's the item id?
			try {
				Item item = player.getInventory().getItemFromSlot(slot);
				int itemid = item.getItemId();
				int amount = item.getAmount();


				// dupe the item to the location of the player
				player.giveItemDrop(itemid, amount);
			} 
			catch (NullPointerException e)
			{
				// no item

			}

			// Tom316 - Remove the item from the slot.
			player.getInventory().removeItem(slot);


		}
		// Tom316 - Make sure we send a inventory update to the player so there client gets the changes.
		player.getInventory().updateInventory();
	}
	public void DoPlayerDeath(Player player)
	{
	      // slain
	      player.sendMessage("You have been slain");
	      
	      if (dropinventory == true )
	      {
	          DropPlayerItems(player);
	      }
	      
	      // warp to spawn
	      player.teleportTo(etc.getServer().getSpawnLocation());
	      setPlayerHP(player,100);
	}

	public String getItemName(int itemId)
	{
		// <for future use>

		// incase there is no item or something we don't have in the list
		String itemname = "fashioned weapon";

		if (itemId == 268)
		{
			// Wooden Sword
			itemname = "Wooden Sword";
		}

		if (itemId == 272)
		{
			// Stone Sword
			itemname = "Stone Sword";
		}

		if (itemId == 267)
		{
			// Iron Sword
			itemname = "Iron Sword";
		}

		if (itemId == 283)
		{
			// Gold Sword
			itemname = "Gold Sword";
		}

		if (itemId == 276)
		{
			// Diamond Sword
			itemname = "Diamond Sword";
		}

		return itemname;
	}

	public int getItemDamage(int itemId)
	  {
	      // in case there is no item found, use the base damage for a 'fashioned weapon' (ie brick etc) (3)
	      int itembasedamage = basedamage;
	           // WOODEN ITEMS
	         if (itemId == 268)
	         {
	             // Wooden Sword
	             itembasedamage = woodensword;
	         }
	         if (itemId == 269)
	         {
	             // Wooden Spade
	             itembasedamage = woodenspade;
	         }
	         if (itemId == 270)
	         {
	             // Wooden Pickaxe
	             itembasedamage = woodenpickaxe;
	         }
	         if (itemId == 271)
	         {
	             // Wooden Axe
	             itembasedamage = woodenaxe;
	         }
	         // STONE ITEMS
	         if (itemId == 272)
	         {
	             // Stone Sword
	             itembasedamage = stonesword;
	         }
	         if (itemId == 273)
	         {
	             // Stone Spade
	             itembasedamage = stonespade;
	         }
	         if (itemId == 274)
	         {
	             // Stone Pickaxe
	             itembasedamage = stonepickaxe;
	         }
	         if (itemId == 275)
	         {
	             // Stone Axe
	             itembasedamage = stoneaxe;
	         }
	         // DIAMOND ITEMS
	         if (itemId == 276)
	         {
	             // Diamond Sword
	             itembasedamage = diamondsword;
	         }
	         if (itemId == 277)
	         {
	             // Diamond Spade
	             itembasedamage = diamondspade;
	         }
	         if (itemId == 278)
	         {
	             // Diamond Pickaxe
	             itembasedamage = diamondpickaxe;
	         }
	         if (itemId == 279)
	         {
	             // Diamond Axe
	             itembasedamage = diamondaxe;
	         }
	         // IRON ITEMS
	         if (itemId == 267)
	         {
	             // Iron Sword
	             itembasedamage = ironsword;
	         }
	         if (itemId == 256)
	         {
	             // Iron Spade
	             itembasedamage = ironspade;
	         }
	         if (itemId == 257)
	         {
	             // Iron Pickaxe
	             itembasedamage = ironpickaxe;
	         }
	         if (itemId == 258)
	         {
	             // Iron Axe
	             itembasedamage = ironaxe;
	         }
	         // GOLD ITEMS
	         if (itemId == 283)
	         {
	             // Gold Sword
	             itembasedamage = goldsword;
	         }
	         if (itemId == 284)
	         {
	             // Gold Spade
	             itembasedamage = goldspade;
	         }
	         if (itemId == 285)
	         {
	             // Gold Pickaxe
	             itembasedamage = goldpickaxe;
	         }
	         if (itemId == 286)
	         {
	             // Gold Axe
	             itembasedamage = goldaxe;
	         }
	         return itembasedamage;
	  }

	public int getPlayerDamage(Player player)
	{
		// what are they holding? (if anything)
		int itemId = player.getItemInHand();
		// default base damage is 3 if no item is found
		int damage = getItemDamage(itemId);

		// add melee skill bonus modifier
		damage = damage + getPlayerMelee(player);

		// randomise damage from 1 to max so far
		Random generator = new Random();
		int index = generator.nextInt(damage);

		// add one just incase it was 0 (maybe do a miss here instead)
		index = index + 1;

		return index;
	}

	public void onArmSwing(Player player)
	{

		// Player trying to hit player player
		for (Player p : etc.getServer().getPlayerList())
		{
			if (p != null) {
				if (p.getName() == player.getName())
				{
				} else {
					if (pvp == true)
					{
						double dist = getPlayerDistance(player, p);
						if (dist <= 2.0D)
						{
							if (PlayerHasHit(player) == 0)
							{ 
								// missed
								if (getPlayerHP(p) < 1)
								{
									// do nothing they are already dead
								} else {
									if (getCombatLog(player) == 1)
									{
										player.sendMessage("§7You try to strike a " + p.getName() + " HP: (" + getPlayerHP(p) + ") but miss! Your HP: " + getPlayerHP(player));
									} else {
										// suppress the combat log
									}
								}
							} else {
								// hit
								// Get player damage
								int thisdmg = getPlayerDamage(player);

								if (getCombatLog(player) == 1)
								{
									player.sendMessage("You strike " + p.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(player) + " Their HP: " + getPlayerHP(p));
								} else {
									// Supress the combat log
								}
								if (getPlayerHP(p) < thisdmg)
								{
									player.sendMessage("You have slain " + p.getName() + "!");
									p.sendMessage("§cYou have been slain by " + player.getName() + "!");
									// reset hp and warp to spawn
									DoPlayerDeath(p);
								} else {
									setPlayerHP(p,getPlayerHP(p) - thisdmg);
									if (getCombatLog(p) == 1)
									{
										p.sendMessage("§cYou have been hit by " + player.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(p) + " Their HP: " + getPlayerHP(player));
									} else {
										// Supress the combat log
									}
								}
							}
						} else {
							// too far away
						}
					}
				}
			}
		}

		// against npc  
		for (Mob m : etc.getServer().getMobList())
		{
			if (m != null) {
				double dist = getDistance(player, m);

				if (dist < 2.0D)
				{
					if (PlayerHasHit(player) == 0)
					{
						// Missed
						if (m.getHealth() < 1)
						{
							// do nothing they are already dead...
						} else {

							// tell them they missed
							if (getCombatLog(player) == 1)
							{
								player.sendMessage("§7You try to strike a " + m.getName() + " HP: (" + m.getHealth() + ") but miss! Your HP: " + getPlayerHP(player));
							} else {
								// supress the combat log
							}
						}
					} else {
						// Hit

						if (m.getHealth() < 1)
						{
							// do nothing they are already dead
						} else {

							// Get player damage
							int thisdmg = getPlayerDamage(player);
							
							if (getCombatLog(player) == 1)
							{
								player.sendMessage("You strike " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage. Your HP: " + getPlayerHP(player));
							} else {
								
							}
							if (m.getHealth() <= thisdmg)
							{
								player.sendMessage("You have slain a " + m.getName() + "!");
								m.setHealth(0);
								GiveExperience(player,1);
							} else {
								m.setHealth(m.getHealth() - thisdmg);
								//DoPanic(m, player, 5);
							}
						}
					}
				}
			}
		}
	}

}