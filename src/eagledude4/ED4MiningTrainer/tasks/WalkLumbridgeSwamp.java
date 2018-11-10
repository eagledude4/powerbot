package eagledude4.ED4MiningTrainer.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import eagledude4.ED4MiningTrainer.Task;
import eagledude4.ED4MiningTrainer.Main;

import Walker.Walker;

public class WalkLumbridgeSwamp extends Task {
	Main main;
	
	private final Walker walker = new Walker(ctx);
	//public String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	
	public final Tile stairsBottomTile = new Tile(3206, 3208, 0);
	public final Tile stairsTopTile = new Tile(3205, 3209, 2);
	public final Tile bankTile = new Tile(3209, 3219, 2);
	public final Tile mineStartTile = new Tile(3227, 3146, 0);
	final Tile toMine[] = 
	{
		new Tile(3217, 3218, 0),
		new Tile(3226, 3218, 0),
		new Tile(3234, 3211, 0),
		new Tile(3236, 3201, 0),
		new Tile(3244, 3193, 0),
		new Tile(3241, 3179, 0),
		new Tile(3238, 3166, 0),
		new Tile(3240, 3161, 0),
		new Tile(3234, 3153, 0),
		mineStartTile
	};
	//TilePath toMinePath = ctx.movement.newTilePath(toMine);
	final Tile toBank[] = 
	{
		new Tile(3234, 3153, 0),
		new Tile(3240, 3161, 0),
		new Tile(3238, 3166, 0),
		new Tile(3241, 3179, 0),
		new Tile(3244, 3193, 0),
		new Tile(3236, 3201, 0),
		new Tile(3234, 3211, 0),
		new Tile(3226, 3218, 0),
		new Tile(3217, 3218, 0),
		bankTile
	};
	//TilePath toBankPath = ctx.movement.newTilePath(toBank);
	
	public WalkLumbridgeSwamp(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }
    
    public void walkToBank() {
    	main.updateStatus("Walking to Bank");
        
    	GameObject stairsBottom = ctx.objects.select().id(16671).nearest().poll();
        GameObject stairsMiddle = ctx.objects.select().id(16672).nearest().poll();
    	
        if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())>3)) {
        	if (ctx.players.local().tile().distanceTo(stairsBottom.tile()) > 20) {
        		//toBankPath.traverse();
        		if (walker.walkPath(toBank)) {
        			Condition.wait(new Callable<Boolean>(){
        	            @Override
        	            public Boolean call() throws Exception {
        	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
        	            }
        	        }, 200, 10);
        		}
        	} else {
        		ctx.movement.step(stairsBottomTile.derive(1, 1)); //walk to stairs
        	}
        } else if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())<=3)) {
        	ctx.camera.turnTo(stairsBottom);
    		stairsBottom.interact("Climb-up");
    	} else if (ctx.players.local().tile().floor() == 1) {
    		stairsMiddle.interact("Climb-up");
    	} else if (ctx.players.local().tile().floor() == 2 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6)) {
    		ctx.camera.turnTo(bankTile);
    		ctx.movement.step(bankTile.derive(1, 1)); //walk to bank
    	}
    }
    
    public void walktoMine() {
    	main.updateStatus("Walking to Mine");
    	
        GameObject stairsMiddle = ctx.objects.select().id(16672).nearest().poll();
        GameObject stairsTop = ctx.objects.select().id(16673).nearest().poll();
    	
        if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())>3)) {
        	ctx.movement.step(stairsTopTile.derive(1, 1)); //walk to stairs
        } else if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())<=3)) {
        	ctx.camera.turnTo(stairsTop);
        	stairsTop.interact("Climb-down");
    	} else if (ctx.players.local().tile().floor() == 1) {
    		stairsMiddle.interact("Climb-down");
    	} else if (ctx.players.local().tile().floor() == 0) {
    		//toMinePath.traverse();
    		if (walker.walkPath(toMine)) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    	            }
    	        }, 200, 10);
    		}
    	}
    }

    @Override
    public boolean activate() {
    	//AntibanTimer.cleanup();
    	
    	boolean needToBank = (ctx.inventory.select().count() > 27) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToReturn = (ctx.inventory.select().count() == 0)  && (mineStartTile.distanceTo(ctx.players.local())>6);
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Executing WalkTask "+timeStamp);
    	
    	boolean needToBank = (ctx.inventory.select().count() > 27) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
    	boolean needToReturn = (ctx.inventory.select().count() == 0)  && (mineStartTile.distanceTo(ctx.players.local())>6);
		
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToBank) {
        	walkToBank();
        } else if (needToReturn) {
        	walktoMine();
        }
    }
}
