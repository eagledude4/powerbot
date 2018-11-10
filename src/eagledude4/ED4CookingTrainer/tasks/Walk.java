package eagledude4.ED4CookingTrainer.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import eagledude4.ED4CookingTrainer.Task;
import eagledude4.ED4Utils.HandleStairs;
import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4CookingTrainer.Main;

public class Walk extends Task {
	private final MouseCamera mcam = new MouseCamera(ctx);
	private final HandleStairs handleStairs = new HandleStairs(ctx);
	
	Main main;
	
	int fishIds[];
	
	final Tile stairsBottomTile = new Tile(3206, 3208, 0);
	final Tile stairsTopTile = new Tile(3205, 3209, 2);
	final Tile bankTile = new Tile(3209, 3219, 2);
	final Tile cookingTile = new Tile(3210, 3215, 0);
	
	public Walk(Main main, ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.main = main;
        this.fishIds = fishIds;
    }
    
	public void walkToBank() {
    	main.updateStatus("Walking to Bank");
    	if (handleStairs.handleStairs("Up") && bankTile.distanceTo(ctx.players.local())>6) {
    		ctx.movement.step(bankTile.derive(1, 1)); //walk to bank
    	}
    }
    
    public void walkToRange() {
    	main.updateStatus("Walking to Range");
    	if (handleStairs.handleStairs("Down") && cookingTile.distanceTo(ctx.players.local())>3) {
    		ctx.movement.step(cookingTile.derive(1, 1)); //walk to range
    	}
    }

    @Override
    public boolean activate() {
    	//AntibanTimer.cleanup();
    	
    	boolean needToBank = (ctx.inventory.select().id(fishIds).count() == 0) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToCook = (ctx.inventory.select().id(fishIds).count() > 0) && (cookingTile.distanceTo(ctx.players.local())>6);
    	
    	return needToBank || needToCook;
    }
   
    @Override
    public void execute() {
    	boolean needToBank = (ctx.inventory.select().id(fishIds).count() == 0) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToCook = (ctx.inventory.select().id(fishIds).count() > 0) && (cookingTile.distanceTo(ctx.players.local())>6);
		//System.out.println("Executing WalkTask "+timeStamp);
        
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToBank) {
        	walkToBank();
        } else if (needToCook) {
        	walkToRange();
        }
    }
}
