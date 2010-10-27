import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
class SaveCombat extends TimerTask
{
	Timer timer;
	MixxitListener parent;

	public SaveCombat(MixxitListener parent)
	{
		this.parent = parent;
	}
	
	private String getDateTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void run()
	{
		this.parent.packParameters();
		
		this.parent.packPlayers();
		// Start this again
		this.timer = new Timer();
		this.timer.schedule(new SaveCombat(parent), 10000L);

		
	}
	
}