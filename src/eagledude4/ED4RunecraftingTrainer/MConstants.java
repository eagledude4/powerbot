package eagledude4.ED4RunecraftingTrainer;

import org.powerbot.script.Tile;

public class MConstants {
	public static final Tile STAIRS_BOTTOM_TILE = new Tile(3206, 3208, 0);
	public static final Tile STAIRS_TOP_TILE = new Tile(3205, 3209, 2);

    public static final Tile FALADOR_BANK = new Tile(3013, 3355, 0);
    public static final Tile AIR_RUIN_TILE = new Tile(2986, 3294, 0);
    public static final Tile AIR_ALTAR_TILE = new Tile(2843, 4832, 0);
    public static final Tile[] AIR_RUIN_PATH = { 
			FALADOR_BANK,
			new Tile(3006, 3343, 0),
			new Tile(3005, 3328, 0),
			new Tile(3006, 3315, 0),
			new Tile(3005, 3302, 0),
			new Tile(3004, 3288, 0),
			new Tile(2991, 3292, 0),
			AIR_RUIN_TILE
	};
    
	public static final Tile EDGEVILLE_BANK = new Tile(3094, 3491, 0);
	public static final Tile MIND_RUIN_TILE = new Tile(2980, 3513, 0);
    public static final Tile MIND_ALTAR_TILE = new Tile(2786, 4839, 0);
	public static final Tile[] MIND_RUIN_PATH = { 
			EDGEVILLE_BANK,
			new Tile(3086, 3493, 0),
			new Tile(3078, 3503, 0),
			new Tile(3073, 3493, 0),
			new Tile(3072, 3478, 0),
			new Tile(3072, 3465, 0),
			new Tile(3076, 3455, 0),
			new Tile(3068, 3444, 0),
			new Tile(3066, 3430, 0),
			new Tile(3059, 3417, 0),
			new Tile(3044, 3418, 0),
			new Tile(3033, 3427, 0),
			new Tile(3018, 3427, 0),
			new Tile(3005, 3432, 0),
			new Tile(2991, 3430, 0),
			new Tile(2981, 3441, 0),
			new Tile(2974, 3453, 0),
			new Tile(2973, 3465, 0),
			new Tile(2969, 3480, 0),
			new Tile(2975, 3491, 0),
			new Tile(2979, 3502, 0),
			MIND_RUIN_TILE
	};
	
	public static final Tile WATER_RUIN_TILE = new Tile(3185, 3163, 0);
    public static final Tile WATER_ALTAR_TILE = new Tile(2718, 4835, 0);
	public static final Tile[] WATER_RUIN_PATH = { 
			STAIRS_BOTTOM_TILE,
			new Tile(3217, 3218, 0),
			new Tile(3226, 3218, 0),
			new Tile(3234, 3211, 0),
			new Tile(3236, 3201, 0),
			new Tile(3244, 3193, 0),
			new Tile(3241, 3179, 0),
			new Tile(3238, 3166, 0),
			new Tile(3240, 3161, 0),
			new Tile(3225, 3160, 0),
			new Tile(3211, 3158, 0),
			new Tile(3198, 3159, 0),
			new Tile(3190, 3161, 0),
			WATER_RUIN_TILE
	};
	
	public static final Tile VARROCK_BANK = new Tile(3253, 3420, 0);
	public static final Tile EARTH_RUIN_TILE = new Tile(3305, 3472, 0);
    public static final Tile EARTH_ALTAR_TILE = new Tile(2658, 4839, 0);
	public static final Tile[] EARTH_RUIN_PATH = { 
			VARROCK_BANK,
			new Tile(3265, 3428, 0),
			new Tile(3279, 3429, 0),
			new Tile(3285, 3442, 0),
			new Tile(3286, 3456, 0),
			new Tile(3288, 3468, 0),
			new Tile(3068, 3444, 0),
			new Tile(3066, 3430, 0),
			EARTH_RUIN_TILE
	};
	
	public static final Tile FIGHT_ARENA_BANK = new Tile(3382, 3269, 0);
	public static final Tile FIRE_RUIN_TILE = new Tile(3312, 3253, 0);
    public static final Tile FIRE_ALTAR_TILE = new Tile(2583, 4840, 0);
	public static final Tile[] FIRE_RUIN_PATH = { 
			FIGHT_ARENA_BANK,
			new Tile(3366, 3266, 0),
			new Tile(3351, 3265, 0),
			new Tile(3336, 3266, 0),
			new Tile(3325, 3258, 0),
			new Tile(3322, 3243, 0),
			new Tile(3308, 3244, 0),
			FIRE_RUIN_TILE
	};
	
	public static final Tile BODY_RUIN_TILE = new Tile(3055, 3444, 0);
    public static final Tile BODY_ALTAR_TILE = new Tile(2521, 4841, 0);
	public static final Tile[] BODY_RUIN_PATH = { 
			EDGEVILLE_BANK,
			new Tile(3086, 3493, 0),
			new Tile(3078, 3503, 0),
			new Tile(3073, 3493, 0),
			new Tile(3073, 3480, 0),
			new Tile(3072, 3478, 0),
			new Tile(3072, 3465, 0),
			new Tile(3076, 3455, 0),
			new Tile(3068, 3444, 0),
			BODY_RUIN_TILE
	};
}