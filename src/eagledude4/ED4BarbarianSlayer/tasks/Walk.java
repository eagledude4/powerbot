package eagledude4.ED4BarbarianSlayer.tasks;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4BarbarianSlayer.Main;
import eagledude4.ED4BarbarianSlayer.Task;

import Walker.Walker;

public class Walk extends Task {
	Main main;
	
	private final Walker walker = new Walker(ctx);
	
	Pattern pattern = Pattern.compile("(Lobster)|(Sword)|(Salmon)|(Trout)");
	
    Tile BarbarianAreaTile;
    Tile bankTile;
    Tile[] pathToBarbarians;
    
    int[] BarbarianIDs;

    public Walk(Main main, ClientContext ctx, Tile BarbarianAreaTile, Tile bankTile, Tile[] pathToBarbarians, int[] BarbarianIDs) {
        super(ctx);
        this.main = main;
        this.BarbarianAreaTile = BarbarianAreaTile;
        this.bankTile = bankTile;
        this.pathToBarbarians = pathToBarbarians;
        this.BarbarianIDs = BarbarianIDs;
    }

    @Override
    public boolean activate() {
    	boolean needToBank = ctx.inventory.select().name(pattern).count() == 0 && ctx.players.local().tile().distanceTo(bankTile) > 10;
    	boolean needToReturn = ctx.players.local().tile().distanceTo(BarbarianAreaTile) > 20 &&  ctx.inventory.select().name(pattern).count() > 0;
    	
    	return needToBank || needToReturn;
    }

    @Override
    public void execute() {
    	boolean needToBank = ctx.inventory.select().name(pattern).count() == 0 && ctx.players.local().tile().distanceTo(bankTile) > 10;
    	boolean needToReturn = ctx.players.local().tile().distanceTo(BarbarianAreaTile) > 20 &&  ctx.inventory.select().name(pattern).count() > 0;
       
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }

        if (needToBank) {
        	main.updateStatus("Getting Food");
        	
    		if (walker.walkPathReverse(pathToBarbarians)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
        } else if (needToReturn) {
        	main.updateStatus("Returning to Fight");
        	
    		if (walker.walkPath(pathToBarbarians)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
        }
    }
}
