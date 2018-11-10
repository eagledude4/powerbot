package eagledude4.ED4SmithingTrainer.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4SmithingTrainer.Task;

public class Walk extends Task {
	public Tile anvilTile;
	
	int barID;

	public Walk(ClientContext ctx, Tile anvilTile, int barID) {
        super(ctx);

        this.anvilTile = anvilTile.derive(1, 1);
        this.barID = barID;
    }
    
    @Override
    public boolean activate() {
    	boolean needToBank = ctx.inventory.select().id(barID).count() == 0 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
    	boolean needToReturn = ctx.inventory.select().id(barID).count() > 0 && (anvilTile.distanceTo(ctx.players.local()) > 6);
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	boolean needToBank = ctx.inventory.select().id(barID).count() == 0 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 6);
    	boolean needToReturn =  ctx.inventory.select().id(barID).count() > 0  && (anvilTile.distanceTo(ctx.players.local()) > 6);
        
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToBank) {
        	Tile bankTile = ctx.bank.nearest().tile().derive(1, 1);
        	ctx.movement.step(bankTile);
        } else if (needToReturn) {
        	ctx.movement.step(anvilTile);
        }
    }
}
