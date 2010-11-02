import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class RemindTask extends TimerTask
{
  Timer timer;
  MixxitListener parent;
  public int countcompress1 = 0;
  public int totalmobdmg = 0;

  public RemindTask(MixxitListener parent)
  {
    this.parent = parent;
  }

  public void DoMobCombat(Mob m, Player p, int basedamage)
  {
	
	  
    double dist = getDistance(p, m);

    if (dist <= 2.0D)
    {
      Random generator = new Random();
      int index = generator.nextInt(basedamage);
      int thisdmg = index;

      this.totalmobdmg += thisdmg;

      if (this.parent.getPlayerHP(p) - thisdmg < 1)
      {
        if (m.getHealth() != 0)
        {
          if (this.parent.getCombatLog(p) == 1)
          {
            p.sendMessage("§cYou were hit by " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + this.parent.getPlayerHP(p) + "/" + this.parent.getMaxBaseHealth(p) + ")");
          }
          else if (this.parent.getCombatLog(p) == 2)
          {
            if (this.countcompress1 == 4)
            {
              p.sendMessage("Total damage recieved " + this.totalmobdmg + ". Current Health: " + this.parent.getPlayerHP(p) + "/" + this.parent.getMaxBaseHealth(p) + ".");
              this.countcompress1 = 0;
              this.totalmobdmg = 0;
            } else {
              this.countcompress1 += 1;
            }

          }

          this.parent.DoPlayerDeath(p);
        }
      }
      else if (m.getHealth() != 0)
      {
        this.parent.setPlayerHP(p, Integer.valueOf(this.parent.getPlayerHP(p) - thisdmg));

        if (this.parent.getCombatLog(p) == 1)
        {
          p.sendMessage("§cYou were hit by " + m.getName() + " HP(" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + this.parent.getPlayerHP(p) + "/" + this.parent.getMaxBaseHealth(p) + ")");
        }
        else if (this.parent.getCombatLog(p) == 2)
        {
          if (this.countcompress1 == 4)
          {
            p.sendMessage("Total damage recieved " + this.totalmobdmg + ". Current Health: " + this.parent.getPlayerHP(p) + "/" + this.parent.getMaxBaseHealth(p) + ".");
            this.countcompress1 = 0;
            this.totalmobdmg = 0;
          } else {
            this.countcompress1 += 1;
          }
        }
      }
    }
  }

  public void run()
  {
    try
    {
      List<Mob> mobs = new ArrayList<Mob>(etc.getServer().getMobList());
      List<Player> players = new ArrayList<Player>(etc.getServer().getPlayerList());
      
      for (Player p : players) {
    	  Location l = p.getLocation();
    	  int id = etc.getServer().getBlockIdAt((int)l.x, (int)(l.y), (int)(l.z)-1);
    	  
    	  if(id == 10 || id == 11) { //lava
    		  if(this.parent.getPlayerHP(p) > 2) {
    	        this.parent.setPlayerHP(p, Integer.valueOf(this.parent.getPlayerHP(p) - 2));

    	        if (this.parent.getCombatLog(p) == 1)
    	        {
    	          p.sendMessage("§cYou are standing in lava! (CurrHP: " + this.parent.getPlayerHP(p) + "/" + this.parent.getMaxBaseHealth(p) + ")");
    	        }
    		  } else {
    			  this.parent.DoPlayerDeath(p);
    		  }
    	  }
      }
      
      for (Mob m : mobs) {
        if (m == null) {
          continue;
        }
        if (m.getName().equals("Creeper")  == true)
        {
          if (this.parent.boomers == false)
          {
        	  m.setHealth(0);
          }
        	
          for (Player p : players) {
            DoMobCombat(m, p, 3);
          }
        }
        
        if (m.getName().equals("Spider")  == true)
        {
          for (Player p : players) {
            DoMobCombat(m, p, 8);
          }
        }

        if (m.getName().equals("Zombie")  == true)
        {
          for (Player p : players) {
            DoMobCombat(m, p, 4);
          }
        }
        if (m.getName().equals("Skeleton") == true)
        {
	        for (Player p : players)
	        {
	          DoMobCombat(m, p, 5);
	        }
        }
      }
    }
    catch (ConcurrentModificationException localConcurrentModificationException)
    {
    }
  }

  private double getDistance(Player a, Mob b)
  {
    double xPart = Math.pow(a.getX() - b.getX(), 2.0D);
    double yPart = Math.pow(a.getY() - b.getY(), 2.0D);
    double zPart = Math.pow(a.getZ() - b.getZ(), 2.0D);
    return Math.sqrt(xPart + yPart + zPart);
  }
}