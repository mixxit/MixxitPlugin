import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MixxitPlugin extends Plugin
{
  private String name = "MixxitPlugin";

  static final MixxitListener listener = new MixxitListener();

  private String getDateTime()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public void enable()
  {
    System.out.println(getDateTime() + " [INFO] Solinia plugin enabled");
    etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
    etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
    
  }

  public void disable()
  {
    System.out.println(getDateTime() + " [INFO] Solinia plugin disabled");
  }
}