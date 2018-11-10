package eagledude4.ED4GiantSlayer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import eagledude4.ED4GiantSlayer.Main;
import eagledude4.ED4GiantSlayer.Task;

import eagledude4.ED4Utils.MouseCamera;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Fight extends Task {
	Main main;
	public Pattern pattern = Pattern.compile("(Lobster)|(Swordfish)|(Salmon)|(Trout)");
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Tile GiantAreaTile;
    int[] GiantIDs;
   
    public Fight(Main main, ClientContext ctx, Tile GiantAreaTile,  int[] GiantIDs) {
    	 super(ctx);
         this.main = main;
         this.GiantAreaTile = GiantAreaTile;
         this.GiantIDs = GiantIDs;
    }

    @Override
    public boolean activate() {
    	return ctx.combat.healthPercent() > 10 && 
    			ctx.players.local().tile().distanceTo(GiantAreaTile) <= 20 && 
    			!ctx.players.local().interacting().valid() && 
    			!ctx.players.local().inCombat() && 
    			ctx.inventory.select().name(pattern).count() > 0;
    }

    @Override
    public void execute() {
    	Npc Giant = ctx.npcs.select().id(GiantIDs).select(new Filter<Npc>() {
			@Override
			public boolean accept(Npc npc) {
				return !npc.interacting().valid() && npc.tile().matrix(ctx).reachable();
			}
    	}).nearest().poll();
    	
    	if (!Giant.inViewport()) {
    		/*if (Giant.tile().distanceTo(ctx.players.local()) < 10) {
    			mcam.turnTo(Giant);
    		} else {
    			ctx.movement.step(Giant.tile().derive(3, 3));
    		}*/
    		mcam.turnTo(Giant);
		}
    	if (Giant.interact("Attack")) {
			 mcam.turnTo(Giant);
			 Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	            	return ctx.players.local().inCombat();
	            }
			 }, 200, 10);
			 
			 main.updateStatus("Fighting Giant");
		 }
    }
}