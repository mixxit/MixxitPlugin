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

			totalmobdmg = totalmobdmg + thisdmg;
			
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
						// Tom316 - Compress Combat Log...
						if (parent.getCombatLog(p) == 2)
						{
							if (countcompress1 == 4)
							{

								p.sendMessage("Total damage recieved " + totalmobdmg + ". Current Health: " + parent.getPlayerHP(p) + ".");
								countcompress1 = 0;
								totalmobdmg = 0;
							}else{
								countcompress1 = countcompress1 + 1;
								// Tom316 - Debug info
								//p.sendMessage("Spot 1 - CountCompress1 = " + countcompress1 );
							}
						}
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
						// Tom316 - Compress Combat Log...
						if (parent.getCombatLog(p) == 2)
						{
							if (countcompress1 == 4)
							{
								
								p.sendMessage("Total damage recieved " + totalmobdmg + ". Current Health: " + parent.getPlayerHP(p) + ".");
								countcompress1 = 0;
								totalmobdmg = 0;
							}else{
								countcompress1 = countcompress1 + 1;
								// Tom316 - Debug Info
								//p.sendMessage("Spot 2 -CountCompress1 = " + countcompress1 );
							}
						}
						
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