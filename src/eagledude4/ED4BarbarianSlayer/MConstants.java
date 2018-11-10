package eagledude4.ED4BarbarianSlayer;

import org.powerbot.script.Tile;

public class MConstants {
	public static final Tile BARBARIAN_AREA_TILE = new Tile(3082, 3419, 0);
	public static final Tile BANK_TILE = new Tile(3094, 3491, 0);
	
    public static final Tile TO_BARBARIANS[] = 
	{
		BANK_TILE,
		new Tile(3086, 3486, 0),
		new Tile(3080, 3475, 0),
		new Tile(3087, 3462, 0),
		new Tile(3080, 3450, 0),
		new Tile(3079, 3433, 0),
		BARBARIAN_AREA_TILE
	};
    
    public static final int BARBARIAN_IDS[] = { 3058, 3059, 3060, 3061, 3064, 3065, 3067, 3071, 3072};
}