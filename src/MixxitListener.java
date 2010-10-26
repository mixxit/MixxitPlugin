import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.ArrayList;
import java.util.Iterator;
public class MixxitListener extends PluginListener
{
	public class p1 {
		  public String name;
		  public int hp;
		  public int exp = 0;
		  public int melee = 0;
		  
		  
		  public p1(String name, int hp)
		  {
			  	this.name = name;
			  	this.hp = hp;
			  	
		  }
	}

  public int pvp = 0;
	
  public Timer timer;
  public ArrayList<p1> playerList;
  public MixxitListener()
  {
    this.timer = new Timer();
    // Tom316 increase time to schedule for server overload
    this.timer.schedule(new RemindTask(this), 700L);
    System.out.println(getDateTime() + " [INFO] Task Scheduled.");
    playerList = new ArrayList<p1>();
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
  
  public void getPlayerMelee(Player player, Integer newmelee)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
		    if (this.playerList.get(i).name == player.getName())
		    {
		    	this.playerList.get(i).melee = newmelee;
		    	
		    }
	  }
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

      p.sendMessage("The " + m.getName() + " hit you back! For " + thisdmg + " damage! (CurrHP: " + p.getHealth() + ")");
      if (p.getHealth() < thisdmg)
      {
        p.sendMessage("You have been slain!");

        Warp home = etc.getDataSource().getHome(p.getName());
        p.teleportTo(home.Location);
      }
      else {
        p.setHealth(p.getHealth() - thisdmg);
      }
    }
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
          
          if(split[0].equalsIgnoreCase("/pvpenable") && player.canUseCommand("/pvpenable"))
          {
            // Tom316 - Enable PVP by setting its value to 1
              pvp = 1; // Tom316 - 0 = Disabled, 1 = Enabled
            player.sendMessage("PVP Enabled");
            return true;
        }
          
          if(split[0].equalsIgnoreCase("/pvpdisable") && player.canUseCommand("/pvpdisable"))
          {
            // Tom316 - Disable PVP by setting its value to 0
            pvp = 0; // Tom316 - 0 = Disabled, 1 = Enabled
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
	  for (int i = 0; i < this.playerList.size(); i++) {
		    if (this.playerList.get(i).name == player.getName())
		    {
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
		        	player.sendMessage("§eYou get better at melee! (" + this.playerList.get(i).melee + ")!");
		        	
		        }
		    
		    }
	  }
	  
		    
		    
	  
  }
  
  public int PlayerHasHit(Player player, Mob m)
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
  
  public void DoPlayerDeath(Player player)
  {
	  // slain
	  player.sendMessage("You have slain " + player.getName());
	  
	  // delete items
	  
	  // Tom316 - Loop through the inventory slots removing each item.
      for(int slot=9;slot<36;slot++)
      {
          // Tom316 - Remove the item from the slot.
          player.getInventory().removeItem(slot);
          
          // maybe spawn the item on the ground here?
          
      }
      // Tom316 - Make sure we send a inventory update to the player so there client gets the changes.
      player.getInventory().updateInventory();
	  
      
	  // warp home
      Warp home = etc.getDataSource().getHome(player.getName());
      player.teleportTo(home.Location);
      setPlayerHP(player,100);

  }
  
  public void onArmSwing(Player player)
  {
	  
	// against player
	  for (Player p : etc.getServer().getPlayerList())
	      if (p != null) {
	    	  
	    	if (p.getName() == player.getName())
	    	{
	    		
	    	} else {
	    		if (this.pvp == 1)
	    		{
			        double dist = getPlayerDistance(player, p);
			        Random generator = new Random();
			        int index = generator.nextInt(3);
			        if (dist <= 2.0D)
			        {
			        	
			        	
			        	int thisdmg = index;
				        player.sendMessage("You strike " + p.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(player) + " Their HP: " + getPlayerHP(p));
				        
				        if (getPlayerHP(p) < thisdmg)
				        {
				        	
				        	p.sendMessage("You have been slain by " + player.getName() + "!");
				            // reset hp and warp home
				            
				        	DoPlayerDeath(p);
				        }
				        else {
				        	
				        	setPlayerHP(p,getPlayerHP(p) - thisdmg);
				        	p.sendMessage("You have been hit by " + player.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(p) + " Their HP: " + getPlayerHP(player));
					        
				          //DoPanic(m, player, 5);
				        }
			        }
			        
	    		}
	    	}
	}
	  
	// against npc  
    for (Mob m : etc.getServer().getMobList())
      if (m != null) {
        double dist = getDistance(player, m);
        Random generator = new Random();
        int index = generator.nextInt(3 + getPlayerMelee(player));
        if (dist < 2.0D)
        {
        	if (PlayerHasHit(player,m) == 0)
        	{
        		if (m.getHealth() < 1)
        		{
        			// do nothing
        		} else {
        			player.sendMessage("You try to strike a " + m.getName() + " HP: (" + m.getHealth() + ") but miss! Your HP: " + getPlayerHP(player));
        		}
       		} else {
        		if (m.getHealth() < 1)
        		{
        			// do nothing
        		} else {
        			
		        	int thisdmg = index;
		            player.sendMessage("You strike " + m.getName() + "HP(" + m.getHealth() + ") for " + thisdmg + " damage. Your HP: " + getPlayerHP(player));
		
		            if (m.getHealth() <= thisdmg)
		            {
			          player.sendMessage("You have slain a " + m.getName() + "!");
		              m.setHealth(0);
		              GiveExperience(player,1);

		            }
		            else {
		              m.setHealth(m.getHealth() - thisdmg);
		              //DoPanic(m, player, 5);
		            }
        		}
        	}
        }
        
      }
  }
}