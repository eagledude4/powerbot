package eagledude4.ED4BarbarianSlayer.tasks;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import eagledude4.ED4BarbarianSlayer.Main;
import eagledude4.ED4BarbarianSlayer.Task;
import eagledude4.ED4Utils.MouseCamera;

public class LootItems extends Task {
	Main main;
	
	Pattern pattern = Pattern.compile("(.*rune)|(Coins)|(Grapes)");
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	public LootItems(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }
    
    @Override
    public boolean activate() {
    	GroundItem groundLoot = ctx.groundItems.select().name(pattern).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().matrix(ctx).reachable() && groundLoot.tile().distanceTo(ctx.players.local().tile()) < 10;
            }
        }).nearest().poll();
    	
    	return !ctx.players.local().interacting().valid() && 
    			!ctx.players.local().inCombat() &&
    			ctx.players.local().animation() == -1 && 
    			groundLoot.valid() && 
    			ctx.inventory.select().count() < 28;
    }
    
    @Override
    public void execute() {
    	//System.out.println("Executing Get Arrows Task "+timeStamp);
    	
    	GroundItem groundLoot = ctx.groundItems.select().name(pattern).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().matrix(ctx).reachable() && groundLoot.tile().distanceTo(ctx.players.local().tile()) < 10;
            }
        }).nearest().poll();
    	
    	if ((groundLoot.name().equals("Coins")) || (!groundLoot.name().equals("Coins")) && ctx.inventory.select().count() < 28) {
	    	main.updateStatus("Getting "+groundLoot.stackSize()+" "+groundLoot.name());
			
			mcam.turnTo(groundLoot);
			
			if (groundLoot.interact(false,"Take",groundLoot.name())) {
				Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		            	return !groundLoot.valid();
		            }
				 }, 200, 10);
			}
    	}
    }
}