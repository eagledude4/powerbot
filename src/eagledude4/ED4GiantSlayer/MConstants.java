package eagledude4.ED4GiantSlayer;

import org.powerbot.script.Tile;

public class MConstants {
	public static final Tile HILLGIANT_AREA_TILE = new Tile(3115, 9836, 0);
	public static final Tile MOSSGIANT_AREA_TILE = new Tile(3159, 9902, 0);
	public static final Tile EDGEVILLE_BANK_TILE = new Tile(3094, 3491, 0);
	public static final Tile VARROCK_BANK_TILE = new Tile(3253, 3420, 0);
	
	public static final Tile[] TO_HILLGIANTS = 
	{
		EDGEVILLE_BANK_TILE,
		new Tile(3094, 3491, 0), 
		new Tile(3100, 3500, 0), 
		new Tile(3110, 3502, 0), 
		new Tile(3120, 3507, 0), 
		new Tile(3128, 3515, 0), 
		new Tile(3131, 3505, 0), 
		new Tile(3125, 3496, 0), 
		new Tile(3117, 3490, 0), 
		new Tile(3114, 3480, 0), 
		new Tile(3122, 3473, 0), 
		new Tile(3132, 3470, 0), 
		new Tile(3126, 3461, 0), 
		new Tile(3117, 3456, 0),
		new Tile(3115, 3448, 0),
		new Tile(3116, 3451, 0),
		new Tile(3116, 9851, 0),
		HILLGIANT_AREA_TILE
	};
	
	public static final Tile[] TO_MOSSGIANTS = 
		{
			VARROCK_BANK_TILE,
			new Tile(3253, 3420, 0), 
			new Tile(3248, 3430, 0), 
			new Tile(3246, 3440, 0), 
			new Tile(3246, 3450, 0), 
			new Tile(3238, 3456, 0), 
			new Tile(3237, 9858, 0), 
			new Tile(3245, 9865, 0), 
			new Tile(3245, 9870, 0), 
			new Tile(3243, 9880, 0), 
			new Tile(3243, 9890, 0), 
			new Tile(3253, 9892, 0), 
			new Tile(3263, 9892, 0), 
			new Tile(3273, 9892, 0), 
			new Tile(3281, 9898, 0), 
			new Tile(3278, 9908, 0), 
			new Tile(3270, 9914, 0), 
			new Tile(3260, 9915, 0), 
			new Tile(3250, 9915, 0), 
			new Tile(3241, 9910, 0), 
			new Tile(3231, 9907, 0), 
			new Tile(3221, 9907, 0), 
			new Tile(3211, 9903, 0), 
			new Tile(3210, 9893, 0), 
			new Tile(3200, 9890, 0), 
			new Tile(3190, 9890, 0), 
			new Tile(3181, 9895, 0), 
			new Tile(3171, 9892, 0), 
			new Tile(3161, 9894, 0),
			HILLGIANT_AREA_TILE
		};
    
    public static final int HILLGIANT_IDS[] = { 2098, 2099, 2100, 2101, 2102 };
    public static final int MOSSGIANT_IDS[] = { 2090, 2091, 2092, 2093 };
}