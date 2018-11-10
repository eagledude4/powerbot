package eagledude4.ED4GiantSlayer.tasks;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4GiantSlayer.Main;
import eagledude4.ED4GiantSlayer.Task;
import eagledude4.ED4Utils.Utils;
import Walker.Walker;

public class Walk extends Task {
	Main main;
	
	private final Utils utils = new Utils(ctx);
	private final Walker walker = new Walker(ctx);
	
	Pattern pattern = Pattern.compile("(Lobster)|(Sword)|(Salmon)|(Trout)");
	
    Tile GiantAreaTile;
    Tile bankTile;
    Tile[] pathToGiants;
    
    int[] GiantIDs;

    public Walk(Main main, ClientContext ctx, Tile GiantAreaTile, Tile bankTile, Tile[] pathToGiants, int[] GiantIDs) {
        super(ctx);
        this.main = main;
        this.GiantAreaTile = GiantAreaTile;
        this.bankTile = bankTile;
        this.pathToGiants = pathToGiants;
        this.GiantIDs = GiantIDs;
    }

    @Override
    public boolean activate() {
    	boolean needToBank = ctx.inventory.select().name(pattern).count() == 0 && ctx.players.local().tile().distanceTo(bankTile) > 10;
    	boolean needToReturn = ctx.players.local().tile().distanceTo(GiantAreaTile) > 20 &&  ctx.inventory.select().name(pattern).count() > 1;
    	
    	return needToBank || needToReturn;
    }

    @Override
    public void execute() {
    	boolean needToBank = ctx.inventory.select().name(pattern).count() == 0 && ctx.players.local().tile().distanceTo(bankTile) > 10;
    	boolean needToReturn = ctx.players.local().tile().distanceTo(GiantAreaTile) > 20 &&  ctx.inventory.select().name(pattern).count() > 1;
       
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }

        if (needToBank) {
        	utils.updateStatus(main.Status,"Getting Food");
        	
    		if (walker.walkPathReverse(pathToGiants)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
        } else if (needToReturn) {
        	utils.updateStatus(main.Status,"Returning to Giants");
        	
    		if (walker.walkPath(pathToGiants)) {
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
