import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MixxitPlugin extends Plugin
{
	private String name = "MixxitPlugin";
	private String version = "1.14";
	
	// Tom316 - Addition of Logger support so that our plugins info is tagged into the minecraft logfile.
	static final Logger log = Logger.getLogger("Minecraft");
	static MixxitListener listener;
	
	private String getDateTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void enable()
	{
		
		// Tom316's hp and pvp commands
		etc.getInstance().addCommand("/health", "- Shows your current health.");
		etc.getInstance().addCommand("/pvpenable", "- Enable PVP");
		etc.getInstance().addCommand("/pvpdisable", "- Disable PVP");
		etc.getInstance().addCommand("/heal", "- Sets you to full health");
		etc.getInstance().addCommand("/enablecombatlog", "- Enables your combat log");
		etc.getInstance().addCommand("/disablecombatlog", "- Disables your combat log");
		etc.getInstance().addCommand("/compressedcombatlog", "- Compresses your combat messages");
		etc.getInstance().addCommand("/MixxitDebug", "-Debug Information for MixxitPlugin"); // Tom316 - Used for getting debug information on variables and such.
		listener = new MixxitListener();
		System.out.println(getDateTime() + " [INFO] " + this.name + " " + this.version + " enabled");    
	}

	public void disable()
	{
		etc.getInstance().removeCommand("/health");
		etc.getInstance().removeCommand("/pvpenable");
		etc.getInstance().removeCommand("/pvpdisable");
		etc.getInstance().removeCommand("/heal");
		etc.getInstance().removeCommand("/enablecombatlog");
		etc.getInstance().removeCommand("/disablecombatlog");
		etc.getInstance().removeCommand("/compressedcombatlog");
		etc.getInstance().removeCommand("/MixxitDebug");
		
		System.out.println(getDateTime() + " [INFO] " + this.name + " " + this.version + " disabled");
	}

	public void initialize() 
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
		// Tom316's hook hp and pvp commands.
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);

	}



}