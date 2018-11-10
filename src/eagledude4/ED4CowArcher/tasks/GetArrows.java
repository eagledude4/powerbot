package eagledude4.ED4CowArcher.tasks;

import java.util.Comparator;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.BasicQuery;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import eagledude4.ED4CowArcher.Main;
import eagledude4.ED4CowArcher.Task;

public class GetArrows extends Task {
	Main main;
	
	int arrowID;
	
	public GetArrows(Main main, ClientContext ctx, int arrowID) {
        super(ctx);
        this.main = main;
        this.arrowID = arrowID;
    }
    
    @Override
    public boolean activate() {
    	GroundItem groundLoot = ctx.groundItems.select().id(arrowID).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().matrix(ctx).reachable() && groundLoot.tile().distanceTo(ctx.players.local().tile()) < 10;
            }
        }).nearest().poll();
    	
    	return !ctx.players.local().interacting().valid() && ctx.players.local().animation()==-1 && groundLoot.valid();
    }
    
    @Override
    public void execute() {
    	BasicQuery<GroundItem> groundLootArr = ctx.groundItems.select().id(arrowID);
    	groundLootArr.sort(new Comparator<GroundItem>() {
    	    @Override
    	    public int compare(GroundItem o1, GroundItem o2) {
    	    	return o1.stackSize() < o2.stackSize() ? 1 : -1;
    	    }
    	});
    	GroundItem groundLoot = groundLootArr.select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().matrix(ctx).reachable() && groundLoot.tile().distanceTo(ctx.players.local()) < 10;
            }
        }).first().poll();
    	
    	if (!groundLoot.inViewport()) {
    		ctx.camera.turnTo(groundLoot);
		} else {
			main.updateStatus("Getting "+groundLoot.stackSize()+" "+groundLoot.name());
			
			ctx.camera.turnTo(groundLoot);
			if (groundLoot.interact(false,"Take",groundLoot.name())) {
				Condition.sleep(Random.nextInt(500, 1000));
			}
		}
    }
}