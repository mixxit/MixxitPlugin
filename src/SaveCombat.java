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

  public void run()
  {
    this.parent.packParameters();

    this.parent.packPlayers();
    this.parent.packGuilds();
  }
}