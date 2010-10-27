import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
class RemindTask extends TimerTask
{
	Timer timer;
	MixxitListener parent;
	int Combattimer = MixxitPlugin.Combattimer;
	
	public RemindTask(MixxitListener parent)
	{
		this.parent = parent;
	}

	public void DoMobCombat(Mob m, Player p, int basedamage)
	{
		double dist = getDistance(p, m);
		int playerentry = 0;
		if (dist <= 2.0D)
		{
			Random generator = new Random();
			int index = generator.nextInt(basedamage);
			int thisdmg = index;

			if ((parent.getPlayerHP(p) - thisdmg) < 1)
			{
				if (m.getHealth() == 0)
				{
					// do nothing
				} else {
					if (parent.getCombatLog(p) == 1)
					{
						p.sendMessage("§cYou were hit by " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + parent.getPlayerHP(p) + ")");
					} else {
						// supress the combat log
					}
	
					// reset hp and warp to spawn
					parent.DoPlayerDeath(p);
				}
			} else {
				if (m.getHealth() == 0)
				{
					// do nothing
				} else {
					parent.setPlayerHP(p, parent.getPlayerHP(p) - thisdmg);
					
					if (parent.getCombatLog(p) == 1)
					{
						p.sendMessage("§cYou were hit by " + m.getName() + " HP(" + m.getHealth() + ") for " + thisdmg + " damage! (CurrHP: " + parent.getPlayerHP(p) + ")");
					} else {
						// supress the combat log
					}
				}
			}



		}
	}


	public void run()
	{

		try {
			for (Mob m : etc.getServer().getMobList()) {
				if (m == null)
					continue;
				
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
				
				if (m.getName() == "Skeleton")
				{
					for (Player p : etc.getServer().getPlayerList()) {
						DoMobCombat(m, p, 5);
					}
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			// array modified mid use, skip for next turn
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