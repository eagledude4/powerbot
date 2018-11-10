package eagledude4.ED4FishingTrainer.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Npc;

//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;

import eagledude4.ED4FishingTrainer.Main;
import eagledude4.ED4FishingTrainer.Task;
import eagledude4.ED4FishingTrainer.MConstants;

//import eagledude4.ED4Utils.Walker;
import STRepo.ST.api.STWalker.STWalker;

public class WalkKaramja extends Task {
	Main main;
	
	//private final Walker walker = new Walker(ctx);
	private final STWalker walker = new STWalker(ctx);

	private Tile walkTile;
	
	public WalkKaramja(Main main, ClientContext ctx, Tile walkTile) {
		super(ctx);
        this.main = main;
        this.walkTile = walkTile;
    }
	
	private void walkToSeamen() {
		//System.out.println("walkToSeamen");
		Npc Seaman = ctx.npcs.select().id(MConstants.SEAMAN_IDS).nearest().poll();
		
    	if ((MConstants.SEAMAN_TILE.distanceTo(ctx.players.local())>6)/* && !Seaman.inViewport()*/) {  //walk to docks
        	
    		if (walker.walkPath(MConstants.TOSEAMEN)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
    	} else {
        	if (!Seaman.inViewport()) {
        		ctx.camera.turnTo(Seaman);
        	} else {
	        	//payFare(Seaman, "To");
        		Seaman.interact("Pay-fare");
        	}
        }
	}
	
	/*private void payFare(Npc Customs, String direction) {
		//System.out.println("payFare");
		if (!questComplete()) {
			if (direction.equals("From")) {
				doConvoFrom();
			} else {
				doConvoTo();
			}
        } else {
        	Customs.interact("Pay-fare");
        }
	}*/

    @Override
    public boolean activate() {
        boolean needToBank = ctx.inventory.select().count()>27 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToReturn = ctx.inventory.select().count()==2  && (walkTile.distanceTo(ctx.players.local())>6);
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	//System.out.println("Executing WalkTask "+timeStamp);
    	
    	boolean needToBank = ctx.inventory.select().count()>27 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToReturn = ctx.inventory.select().count()==2  && (walkTile.distanceTo(ctx.players.local())>6);
    	
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
    	if (needToBank) {
        	main.updateStatus("Walking to Bank");
        	//walker.walkToBank();
        	walker.walkToBank();
        } else if (needToReturn) {
        	main.updateStatus("Walking to Fish");
        	walker.walkPath(walkTile);
        }
    }
}
