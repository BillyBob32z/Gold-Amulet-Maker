package goldamuletmaker.util;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public class Resources {

    public static int amount = 0;
    public static String status = "Starting Script";

    public static Area bankArea = new Area(new Tile(3272, 3173, 0), new Tile(3265, 3161, 0));
    public static Area furnaceArea = new Area(new Tile(3280, 3183, 0), new Tile(3272, 3189, 0));
    public static Area doorArea = new Area(new Tile(3278, 3186, 0), new Tile(3281, 3184, 0));

    public static final Tile[] thePath = new Tile[]{new Tile(3277, 3174), new Tile(3279, 3180, 0), new Tile(3274, 3186, 0)};
    public static final Tile[] bankPath = new Tile[]{new Tile(3279, 3185), new Tile(3276, 3173, 0), new Tile(3269, 3167, 0)};

}
