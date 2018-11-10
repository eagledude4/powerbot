package eagledude4.ED4AlKharidSmelter.tasks;

import java.util.concurrent.Callable;
//import java.util.regex.Pattern;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4AlKharidSmelter.Task;
import eagledude4.ED4AlKharidSmelter.Main;

//import eagledude4.ED4Utils.Walker;

public class Walk extends Task {
	Main main;
	//private final Walker walker = new Walker(ctx);
	
	public Tile bankTile;
	public Tile furnaceTile;
	
	int barID;
	int[] oreIds;

	public Walk(Main main, ClientContext ctx, Tile furnaceTile, int barID, int[] oreIds) {
        super(ctx);
        this.furnaceTile = furnaceTile.derive(1, 1);
        this.barID = barID;
        this.oreIds = oreIds;
    }
    
    @Override
    public boolean activate() {
    	boolean needToBank = false;
    	boolean needToReturn = false;
    	
		if (oreIds.length == 1) {
			needToBank = ctx.inventory.select().id(oreIds[0]).count() == 0 && 
						 ctx.inventory.select().id(barID).count() > 0 && 
						 (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
						
			needToReturn = ctx.inventory.select().id(oreIds[0]).count() > 0  && 
						   ctx.inventory.select().id(barID).count() == 0 && 
				    	   (furnaceTile.distanceTo(ctx.players.local()) > 6);
		} else {
			needToBank = ctx.inventory.select().id(oreIds[0]).count() == 0 && 
						 ctx.inventory.select().id(oreIds[1]).count() == 0 && 
						 ctx.inventory.select().id(barID).count() > 0 && 
						 (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
					
			needToReturn = ctx.inventory.select().id(oreIds[0]).count() > 0  && 
						   ctx.inventory.select().id(oreIds[1]).count() > 0  && 
						   ctx.inventory.select().id(barID).count() == 0 && 
						   (furnaceTile.distanceTo(ctx.players.local()) > 6);
		}
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	boolean needToBank = false;
    	boolean needToReturn = false;
    	
		if (oreIds.length == 1) {
			needToBank = ctx.inventory.select().id(oreIds[0]).count() == 0 && 
						 ctx.inventory.select().id(barID).count() > 0 && 
						 (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
						
			needToReturn = ctx.inventory.select().id(oreIds[0]).count() > 0  && 
						   ctx.inventory.select().id(barID).count() == 0 && 
				    	   (furnaceTile.distanceTo(ctx.players.local()) > 6);
		} else {
			needToBank = ctx.inventory.select().id(oreIds[0]).count() == 0 && 
						 ctx.inventory.select().id(oreIds[1]).count() == 0 && 
						 ctx.inventory.select().id(barID).count() > 0 && 
						 (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
					
			needToReturn = ctx.inventory.select().id(oreIds[0]).count() > 0  && 
						   ctx.inventory.select().id(oreIds[1]).count() > 0  && 
						   ctx.inventory.select().id(barID).count() == 0 && 
						   (furnaceTile.distanceTo(ctx.players.local()) > 6);
		}
        
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToBank) {
        	walkToBank();
        } else if (needToReturn) {
        	walkToFurnace();
        }
    }
    
    public void walkToBank() {
    	//main.updateStatus("Walking to Bank");
        
        bankTile = ctx.bank.nearest().tile().derive(1, 1);
    	
        if (bankTile.distanceTo(ctx.players.local())>6) {
    		if (ctx.movement.step(bankTile)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
    	}
    }
    
    public void walkToFurnace() {
    	//main.updateStatus("Walking to Smelt");
    	
    	if (ctx.players.local().tile().distanceTo(furnaceTile) > 10) {
    		if (ctx.movement.step(furnaceTile)) {
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
