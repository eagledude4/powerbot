package eagledude4.ED4WoodcuttingTrainer.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
//import org.powerbot.script.rt4.TilePath;

import eagledude4.ED4WoodcuttingTrainer.Task;
import eagledude4.ED4WoodcuttingTrainer.Main;

import eagledude4.ED4Utils.HandleStairs;
import eagledude4.ED4Utils.MouseCamera;

import Walker.Walker;

public class WalkWillows extends Task {
	Main main;
	private final HandleStairs handleStairs = new HandleStairs(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	private final Walker walker = new Walker(ctx);
	
	public final Tile stairsBottomTile = new Tile(3206, 3208, 0);
	public final Tile stairsTopTile = new Tile(3205, 3209, 2);
	public final Tile bankTile = new Tile(3209, 3219, 2);
	public final Tile chopStartTile = new Tile(3172, 3266, 0);
	public final Tile toWillows[] = 
	{
		new Tile(3198, 3218, 0),
		new Tile(3193, 3222, 0),
		new Tile(3189, 3227, 0),
		new Tile(3185, 3233, 0),
		new Tile(3180, 3238, 0),
		new Tile(3179, 3243, 0),
		new Tile(3179, 3248, 0),
		new Tile(3178, 3253, 0),
		new Tile(3177, 3259, 0),
		chopStartTile
	};
	//TilePath toWillowsPath = ctx.movement.newTilePath(toWillows);
	public final Tile pathStartTile = toWillows[0];
	public final Tile toBank[] = 
	{
		chopStartTile,
		new Tile(3177, 3259, 0),
		new Tile(3178, 3253, 0),
		new Tile(3179, 3248, 0),
		new Tile(3180, 3238, 0),
		new Tile(3185, 3233, 0),
		new Tile(3189, 3227, 0),
		new Tile(3185, 3233, 0),
		new Tile(3193, 3222, 0),
		new Tile(3198, 3218, 0),
		bankTile
	};
	//TilePath toBankPath = ctx.movement.newTilePath(toBank);
    
	public WalkWillows(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }
    
	public void walkToBank() {
    	main.updateStatus("walking to Bank");
    	
    	if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())>3)) {
        	if (ctx.players.local().tile().x() < pathStartTile.x()) {
        		if (walker.walkPath(toBank)) {
        			Condition.wait(new Callable<Boolean>(){
        	            @Override
        	            public Boolean call() throws Exception {
        	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
        	            }
        	        }, 200, 10);
        		}
        	} else {
        		ctx.movement.step(stairsBottomTile.derive(1, 1)); //walk to stairs
        	}    
        } else {
        	if (handleStairs.handleStairs("Up") && bankTile.distanceTo(ctx.players.local())>6) {
        		ctx.movement.step(bankTile.derive(1, 1)); //walk to bank
        	}
    	}
    }
    
    public void walkToTrees() {
    	main.updateStatus("walking to Trees");
    	if (handleStairs.handleStairs("Down")) {
    		if (ctx.players.local().tile().x() <= pathStartTile.x()) {
    			if (walker.walkPath(toWillows)) {
    				Condition.wait(new Callable<Boolean>(){
    		            @Override
    		            public Boolean call() throws Exception {
    		                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    		            }
    		        }, 200, 10);
    			}
    		} else {
    			ctx.movement.step(pathStartTile.derive(0, 1));
    		}
    	}
    }
    
    @Override
    public boolean activate() {
    	boolean needToBank = ctx.inventory.select().count() > 27 && bankTile.distanceTo(ctx.players.local()) > 6;
    	boolean needToReturn = chopStartTile.distanceTo(ctx.players.local())>20 && ctx.inventory.select().count() < 28;
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Executing walk task "+timeStamp);
    	
    	boolean needToBank = ctx.inventory.select().count() > 27 && bankTile.distanceTo(ctx.players.local()) > 6;
    	boolean needToReturn = chopStartTile.distanceTo(ctx.players.local())>20 && ctx.inventory.select().count() < 28;
    	
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
    	if (needToBank) {
        	walkToBank();
        } else if (needToReturn) {
        	walkToTrees();
        }
    }
}
