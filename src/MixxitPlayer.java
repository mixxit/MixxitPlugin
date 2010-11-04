
public class MixxitPlayer {
	public String name;
    public int hp = 0;
    public int exp = 0;
    public int melee = 0;
    public int level = 0;
    public int faction = 0;
    public int combatlog = 1;
    public int guild = 0;
    public int stat_str = 0;
    public int stat_sta = 0;
    public int stat_agi = 0;
    public int stat_dex = 0;
    public int stat_int = 0;
    public int stat_wis = 0;
    public int stat_cha = 0;
    public int stat_lck = 0;
    public long lastmove = 0;
    public int breath = 25;
    public MixxitPlayer(String name, int hp)
    {
      this.name = name;
      this.hp = hp;
    }
}
