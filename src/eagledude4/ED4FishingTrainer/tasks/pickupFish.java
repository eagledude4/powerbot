package eagledude4.ED4FishingTrainer.tasks;

import java.awt.Point;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4FishingTrainer.Main;
import eagledude4.ED4FishingTrainer.Task;

public class pickupFish extends Task {
	Main main;
	
	int fishIds[];
	
	public pickupFish(Main main, ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.main = main;
        this.fishIds = fishIds;
    }
    
    @Override
    public boolean activate() {
    	GroundItem groundLoot = ctx.groundItems.select().id(fishIds).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().distanceTo(ctx.players.local().tile()) < 10;
            }
        }).nearest().poll();
    	
    	return ctx.inventory.select().count() <= 27 && ctx.players.local().animation()==-1 && groundLoot.valid();
    }
    
    @Override
    public void execute() {
    	GroundItem groundLoot = ctx.groundItems.select().id(fishIds).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().distanceTo(ctx.players.local().tile()) < 10;
            }
        }).nearest().poll();
    	
    	if (!groundLoot.inViewport()) {
    		ctx.camera.turnTo(groundLoot);
		} else {
			main.updateStatus("Getting "+groundLoot.stackSize()+" "+groundLoot.name());

			int x = groundLoot.centerPoint().x - 11 + Random.nextInt(0, 25);
            int y = groundLoot.centerPoint().y - 12 + Random.nextInt(0, 25);
            
            ctx.input.click(new Point(x, y), false);
            if (!ctx.menu.click(Menu.filter("Pickup"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
            	groundLoot.interact("Pickup"); //if not then interact() as normal, just incase
            }
            Condition.sleep(Random.nextInt(300,600));
		}
    }
}