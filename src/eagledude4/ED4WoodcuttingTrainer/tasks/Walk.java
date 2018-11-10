package eagledude4.ED4WoodcuttingTrainer.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import eagledude4.ED4Utils.HandleStairs;
import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4WoodcuttingTrainer.Task;
import eagledude4.ED4WoodcuttingTrainer.Main;


public class Walk extends Task {
	Main main;
	private final HandleStairs handleStairs = new HandleStairs(ctx);
	
	public final Tile stairsBottomTile = new Tile(3206, 3208, 0);
	public final Tile stairsTopTile = new Tile(3205, 3209, 2);
	public final Tile bankTile = new Tile(3209, 3219, 2);
	public final Tile chopStartTile = new Tile(3176, 3222, 0);
    
    public Walk(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }
    
    public void walkToBank() {
    	main.updateStatus("Walking to Bank");
    	if (handleStairs.handleStairs("Up") && bankTile.distanceTo(ctx.players.local())>6) {
    		ctx.movement.step(bankTile.derive(1, 1)); //walk to bank
    	}
    }
    
    public void walkToTrees() {
    	main.updateStatus("Walking to Trees");
    	if (handleStairs.handleStairs("Down") && chopStartTile.distanceTo(ctx.players.local())>10) {
    		ctx.movement.step(chopStartTile.derive(1, 1)); //walk to trees
    	}
    }

    @Override
    public boolean activate() {
    	boolean needToBank = ctx.inventory.select().count() > 27 && bankTile.distanceTo(ctx.players.local()) > 6;
    	boolean needToReturn = ctx.players.local().tile().x() > 3200 && ctx.inventory.select().count() < 28;
    	boolean tooFar = ctx.players.local().animation() == -1 && ctx.players.local().tile().floor() == 0 && ctx.inventory.select().count() < 28 && chopStartTile.distanceTo(ctx.players.local())>50;
    	
    	return needToBank || needToReturn || tooFar;
    }
   
    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Executing walk task "+timeStamp);
    	
    	boolean needToBank = ctx.inventory.select().count() > 27 && bankTile.distanceTo(ctx.players.local()) > 6;
    	boolean needToReturn = ctx.players.local().tile().x() > 3200 && ctx.inventory.select().count() < 28;
    	boolean tooFar = ctx.players.local().animation() == -1 && ctx.players.local().tile().floor() == 0 && ctx.inventory.select().count() < 28 && chopStartTile.distanceTo(ctx.players.local())>50;
        
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
    	if (needToBank) {
        	walkToBank();
        } else if (needToReturn) {
        	walkToTrees();
        } else if (tooFar) {
        	ctx.movement.step(chopStartTile.derive(2, 2)); //walk to trees
        }
    }
}
