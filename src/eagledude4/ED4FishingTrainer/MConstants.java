package eagledude4.ED4FishingTrainer;

import org.powerbot.script.Tile;

public class MConstants {
	public static final int NET_SPOT[] = { 1530 };
	public static final int LURE_SPOT[] = { 1526 };
    public static final int CAGE_HARPOON_SPOT[] = { 1522 };
    public static final int SEAMAN_IDS[] =  { 3644, 3645, 3646 };
    public static final int PLANK_IDS[] = { 2082, 2084 };
    public static final int CUSTOMS_ID = 3648;
    
    public static final Tile BANK_TILE_EDGEVILLE = new Tile(3094, 3491, 0);
	public static final Tile WALK_TILE_EDGEVILLE = new Tile(3101, 3430, 0);
	
	public static final Tile BANK_TILE_LUMBRIDGE = new Tile(3209, 3219, 2);
	public static final Tile WALK_TILE_LUMBRIDGE = new Tile(3239, 3146, 0);
	
	public static final Tile BANK_TILE_DRAYNOR = new Tile(3092, 3243, 0);
	public static final Tile WALK_TILE_KARAMJA = new Tile(2924, 3180, 0);
	
	public static final Tile SEAMAN_TILE = new Tile(3027, 3217, 0);
	public static final Tile CUSTOMS_TILE = new Tile(2953, 3146, 0);
	
	public static final Tile[] TOBANK_EDGEVILLE = {
		WALK_TILE_EDGEVILLE,
		//new Tile(3101, 3430, 0), 
		new Tile(3098, 3435, 0),
		new Tile(3094, 3439, 0), 
		new Tile(3095, 3444, 0), 
		new Tile(3091, 3447, 0), 
		new Tile(3093, 3452, 0),
		new Tile(3095, 3457, 0), 
		new Tile(3090, 3460, 0), 
		new Tile(3091, 3465, 0), 
		new Tile(3096, 3465, 0), 
		new Tile(3098, 3470, 0), 
		new Tile(3099, 3475, 0), 
		new Tile(3099, 3480, 0), 
		new Tile(3095, 3483, 0),
		new Tile(3091, 3486, 0), 
		//new Tile(3093, 3491, 0),
		BANK_TILE_EDGEVILLE
	};
	public static final Tile[] TOBANK_LUMBRIDGE = 
	{
		WALK_TILE_LUMBRIDGE,
		new Tile(3240, 3161, 0),
		new Tile(3238, 3166, 0),
		new Tile(3241, 3179, 0),
		new Tile(3244, 3193, 0),
		new Tile(3236, 3201, 0),
		new Tile(3234, 3211, 0),
		new Tile(3226, 3218, 0),
		new Tile(3217, 3218, 0),
		BANK_TILE_LUMBRIDGE
	};
	
	//Karamja
	public static final Tile[] TOBANK_KARAMJA = {
		new Tile(3027, 3222, 0), 
		new Tile(3027, 3228, 0), 
		new Tile(3027, 3234, 0), 
		new Tile(3032, 3236, 0), 
		new Tile(3041, 3236, 0), 
		new Tile(3042, 3243, 0), 
		new Tile(3049, 3246, 0), 
		new Tile(3054, 3252, 0), 
		new Tile(3064, 3256, 0), 
		new Tile(3064, 3263, 0), 
		new Tile(3067, 3269, 0), 
		new Tile(3070, 3277, 0), 
		new Tile(3071, 3268, 0), 
		new Tile(3073, 3259, 0), 
		new Tile(3073, 3248, 0), 
		new Tile(3082, 3248, 0), 
		BANK_TILE_DRAYNOR
	};
	public static final Tile[] TOSEAMEN = {
		new Tile(3082, 3248, 0),
		new Tile(3073, 3248, 0),
		new Tile(3073, 3259, 0), 
		new Tile(3071, 3268, 0), 
		new Tile(3070, 3277, 0), 
		new Tile(3067, 3269, 0), 
		new Tile(3064, 3263, 0), 
		new Tile(3064, 3256, 0), 
		new Tile(3054, 3252, 0), 
		new Tile(3049, 3246, 0), 
		new Tile(3042, 3243, 0), 
		new Tile(3041, 3236, 0), 
		new Tile(3032, 3236, 0), 
		new Tile(3027, 3234, 0), 
		new Tile(3027, 3228, 0), 
		new Tile(3027, 3222, 0),
		SEAMAN_TILE
	};
	public static final Tile[] TOCUSTOMS = {
		new Tile(2924, 3176, 0),
		new Tile(2923, 3171, 0),
		new Tile(2920, 3167, 0),
		new Tile(2920, 3167, 0),
		new Tile(2917, 3160, 0),
		new Tile(2916, 3153, 0),
		new Tile(2922, 3151, 0),
		new Tile(2927, 3151, 0),
		new Tile(2936, 3147, 0),
		new Tile(2936, 3147, 0),
		new Tile(2943, 3145, 0),
		new Tile(2951, 3147, 0),
		CUSTOMS_TILE
	};
	public static final Tile[] TOFISH_KARAMJA = {
		new Tile(2951, 3147, 0),
		new Tile(2943, 3145, 0),
		new Tile(2936, 3147, 0),
		new Tile(2936, 3147, 0),
		new Tile(2927, 3151, 0),
		new Tile(2922, 3151, 0),
		new Tile(2916, 3153, 0),
		new Tile(2917, 3160, 0),
		new Tile(2920, 3167, 0),
		new Tile(2920, 3167, 0),
		new Tile(2923, 3171, 0),
		new Tile(2924, 3176, 0),
		WALK_TILE_KARAMJA
	};
}