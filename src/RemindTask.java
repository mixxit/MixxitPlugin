import java.util.ConcurrentModificationException;
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
            p.sendMessage("§cYou were hit by " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + this.parent.getPlayerHP(p) + ")");
          }
          else if (this.parent.getCombatLog(p) == 2)
          {
            if (this.countcompress1 == 4)
            {
              p.sendMessage("Total damage recieved " + this.totalmobdmg + ". Current Health: " + this.parent.getPlayerHP(p) + ".");
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
          p.sendMessage("§cYou were hit by " + m.getName() + " HP(" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + this.parent.getPlayerHP(p) + ")");
        }
        else if (this.parent.getCombatLog(p) == 2)
        {
          if (this.countcompress1 == 4)
          {
            p.sendMessage("Total damage recieved " + this.totalmobdmg + ". Current Health: " + this.parent.getPlayerHP(p) + ".");
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
      for (Mob m : etc.getServer().getMobList()) {
        if (m == null) {
          continue;
        }
        if (m.getName() == "Spider")
        {
          for (Player p : etc.getServer().getPlayerList()) {
            DoMobCombat(m, p, 8);
          }
        }

        if (m.getName() == "Zombie")
        {
          for (Player p : etc.getServer().getPlayerList()) {
            DoMobCombat(m, p, 4);
          }
        }

        if (m.getName() == "Creeper")
        {
          for (Player p : etc.getServer().getPlayerList()) {
            DoMobCombat(m, p, 2);
          }
        }

        if (m.getName() != "Skeleton")
          continue;
        for (Player p : etc.getServer().getPlayerList())
          DoMobCombat(m, p, 5);
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