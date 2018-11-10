package eagledude4.ED4MiningTrainer.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import eagledude4.ED4MiningTrainer.Main;
import eagledude4.ED4MiningTrainer.Task;
import eagledude4.ED4Utils.MouseCamera;

public class pickupOre extends Task {
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Main main;
	
	int oreID;
	
	public pickupOre(Main main, ClientContext ctx, int oreID) {
        super(ctx);
        this.main = main;
        this.oreID = oreID;
    }
    
    @Override
    public boolean activate() {
    	return  ctx.players.local().tile().y() > 9000 &&
    			ctx.players.local().tile().x() >= 3026 &&
    			ctx.inventory.select().count() <= 27 &&
    			ctx.players.local().animation() == -1 && 
    			ctx.groundItems.select(20).id(main.oreID).size() > 0;
    }
    
    @Override
    public void execute() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("PICKUP ORE "+timeStamp);
    	
    	GroundItem groundLoot = ctx.groundItems.select().id(oreID).select(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundLoot) {
				return groundLoot.tile().y() > 9730 && groundLoot.tile().y() < 9756;
			}
    	}).within(20).nearest().poll();
    	
    	//System.out.println(ctx.groundItems.select().id(oreID).poll().valid());
    	main.updateStatus("Getting "+groundLoot.stackSize()+" "+groundLoot.name());
    	
    	if (!groundLoot.inViewport()) {
    		ctx.camera.pitch(100);
	    	if (ctx.players.local().tile().distanceTo(groundLoot) < 10) {
	    		//ctx.camera.turnTo(Rock);
	    		mcam.turnTo(groundLoot);
	    	} else {
	    		ctx.movement.step(groundLoot.tile().derive(2, 2));
	    		
	    		if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
	                ctx.movement.running(true);
	            }
	    	}
    	}
    	
    	while (groundLoot.valid()) {
    		groundLoot.click();
    		Condition.sleep(Random.nextInt(100,200));
    	}
    }
}