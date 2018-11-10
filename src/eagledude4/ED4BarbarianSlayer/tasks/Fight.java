package eagledude4.ED4BarbarianSlayer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import eagledude4.ED4BarbarianSlayer.Main;
import eagledude4.ED4BarbarianSlayer.Task;
import eagledude4.ED4Utils.MouseCamera;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Fight extends Task {
	Main main;
	
	public Pattern pattern = Pattern.compile("(Lobster)|(Swordfish)|(Salmon)|(Trout)");
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Tile BarbarianAreaTile;
    Tile[] pathToBarbarians;
    int[] BarbarianIDs;
   
    public Fight(Main main, ClientContext ctx, Tile BarbarianAreaTile, Tile[] pathToBarbarians, int[] BarbarianIDs) {
    	 super(ctx);
         this.main = main;
         this.BarbarianAreaTile = BarbarianAreaTile;
         this.pathToBarbarians = pathToBarbarians;
         this.BarbarianIDs = BarbarianIDs;
    }

    @Override
    public boolean activate() {
    	return ctx.combat.healthPercent() > 10 && 
    			ctx.players.local().tile().distanceTo(BarbarianAreaTile) <= 20 && 
    			!ctx.players.local().interacting().valid() && 
    			!ctx.players.local().inCombat() && 
    			ctx.inventory.select().name(pattern).count() > 0;
    }

    @Override
    public void execute() {
    	Npc Barbarian = ctx.npcs.select().id(BarbarianIDs).select(new Filter<Npc>() {
			@Override
			public boolean accept(Npc npc) {
				return !npc.interacting().valid() && npc.tile().matrix(ctx).reachable();
			}
    	}).nearest().poll();
    	
    	if (!Barbarian.inViewport()) {
    		/*if (Barbarian.tile().distanceTo(ctx.players.local()) < 10) {
    			mcam.turnTo(Barbarian);
    		} else {
    			ctx.movement.step(Barbarian.tile().derive(3, 3));
    		}*/
    		mcam.turnTo(Barbarian);
		}
    	if (Barbarian.interact("Attack")) {
			 mcam.turnTo(Barbarian);
			 Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	            	return ctx.players.local().inCombat();
	            }
			 }, 200, 10);
			 
			 main.updateStatus("Fighting Barbarian");
		 }
    }
}