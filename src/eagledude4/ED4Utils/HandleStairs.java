package eagledude4.ED4Utils;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class HandleStairs extends ClientAccessor {
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	private final Tile stairsBottomTile = new Tile(3206, 3208, 0);
	private final Tile stairsTopTile = new Tile(3205, 3209, 2);
	
	public HandleStairs(ClientContext ctx) {
        super(ctx);
    }
	
	public boolean handleStairs(String Direction) {
        GameObject stairsTop = ctx.objects.select().id(16673).nearest().poll();
        GameObject stairsMiddle = ctx.objects.select().id(16672).nearest().poll();
        GameObject stairsBottom = ctx.objects.select().id(16671).nearest().poll();
        
        Boolean returnValue = false;
    	
        if (Direction == "Up") {
	        if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())>3)) {
	        	ctx.movement.step(stairsBottomTile.derive(1, 1)); //walk to stairs
	        } else if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())<=3)) {
	        	mcam.turnTo(stairsBottom);
	        	stairsBottom.interact("Climb-up");
	        	Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().floor() == 1;
		            }
		        }, 500, 10);
	    	} else if (ctx.players.local().tile().floor() == 1) {
	    		stairsMiddle.interact("Climb-up");
	    		Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().floor() == 2;
		            }
		        }, 500, 10);
	    	} else if (ctx.players.local().tile().floor() == 2) {
	    		returnValue = true;
	    	}
        } else if (Direction == "Down") {
	        if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())>3)) {
	        	ctx.movement.step(stairsTopTile.derive(1, 1));
	        } else if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())<=3)) {
	        	mcam.turnTo(stairsTop);
	        	stairsTop.interact("Climb-down");
	        	Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().floor() == 1;
		            }
		        }, 500, 10);
	    	} else if (ctx.players.local().tile().floor() == 1) {
	    		stairsMiddle.interact("Climb-down");
	    		Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().floor() == 0;
		            }
		        }, 500, 10);
	    	} else if (ctx.players.local().tile().floor() == 0) {
	    		returnValue = true;
	    	}
    	}
        
        return returnValue;
    }
}
