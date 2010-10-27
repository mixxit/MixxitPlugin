import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MixxitPlugin extends Plugin
{
  private String name = "MixxitPlugin";
  private String version = "1.12";

  static final MixxitListener listener = new MixxitListener();

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

    System.out.println(getDateTime() + " [INFO] MixxitPlugin " + this.version + " enabled");    
  }

  public void disable()
  {
	etc.getInstance().removeCommand("/health");
    etc.getInstance().removeCommand("/pvpenable");
    etc.getInstance().removeCommand("/pvpdisable");
    etc.getInstance().removeCommand("/heal");
    System.out.println(getDateTime() + " [INFO] Solinia plugin disabled");
  }
  
  public void initialize() 
  {
      etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
      etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
      // Tom316's hook hp and pvp commands.
      etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
  }
  
  
  
}