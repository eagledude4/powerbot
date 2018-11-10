package eagledude4.ED4MiningTrainer.tasks;

//import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
//import org.powerbot.script.rt4.TilePath;

import eagledude4.ED4MiningTrainer.Task;
import eagledude4.ED4MiningTrainer.Main;

import eagledude4.ED4Utils.MouseCamera;
import Walker.Walker;

public class WalkDwarvenMine extends Task {
	private final MouseCamera mcam = new MouseCamera(ctx);
	private final Walker walker = new Walker(ctx);
	Main main;

	
	public final Tile bankTile = new Tile(3013, 3355, 0);
	public final Tile stairsTopTile = new Tile(3061, 3376, 0);
	public final Tile stairsBottomTile = new Tile(3058, 9776, 0);
	public Tile mineStartTile;
	public Tile toMine[];
	
	final Tile toStairsTop[] = 
	{
		bankTile,
		new Tile(3013, 3363, 0),
		new Tile(3022, 3361, 0),
		new Tile(3036, 3357, 0),
		new Tile(3043, 3364, 0),
		new Tile(3049, 3369, 0),
		new Tile(3059, 3370, 0),
		stairsTopTile
	};
	
	public WalkDwarvenMine(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
        
        if (main.oreID == 440) { //iron
        	this.mineStartTile = new Tile(3052, 9822, 0);
        	this.toMine = new Tile[] {
        		stairsBottomTile,
    			new Tile(3048, 9783, 0),
    			new Tile(3043, 9793, 0),
    			new Tile(3043, 9806, 0),
    			new Tile(3050, 9816, 0),
    			mineStartTile
    		};
        } else if (main.oreID == 453) { //coal
        	this.mineStartTile = new Tile(3038, 9799, 0);
        	this.toMine = new Tile[] {
        		stairsBottomTile,
        		new Tile(3048, 9783, 0),
    			new Tile(3043, 9793, 0),
    			new Tile(3043, 9806, 0),
    			mineStartTile
    		};
        } else if (main.oreID == 444) { //gold
        	this.mineStartTile = new Tile(3049, 9761, 0);
        	this.toMine = new Tile[] {
        		stairsBottomTile,
        		new Tile(3050, 9768, 0),
    			mineStartTile
        	};
        } else if (main.oreID == 447) { //mithril
        	this.mineStartTile = new Tile(3036, 9772, 0);
        	this.toMine = new Tile[] {
        		stairsBottomTile,
        		new Tile(3047, 9769, 0),
    			mineStartTile
        	};
        } else if (main.oreID == 449) { //adamant
        	this.mineStartTile = new Tile(3042, 9773, 0);
        	this.toMine = new Tile[] {
        		stairsBottomTile,
        		new Tile(3047, 9769, 0),
    			mineStartTile
        	};
        }
    }
    
    public void walkToBank() {
    	main.updateStatus("Walking to Bank");
    	
    	GameObject stairsTop = ctx.objects.select().id(16664).nearest().poll();
        GameObject stairsBottom = ctx.objects.select().id(23969).nearest().poll();
    	
        if (ctx.players.local().tile().y() > 9000) {
        	if (stairsBottomTile.distanceTo(ctx.players.local())>5) {
            	if (walker.walkPathReverse(toMine)) {
            		final int randomNum = Random.nextInt(1, 6);
        			Condition.wait(new Callable<Boolean>(){
        	            @Override
        	            public Boolean call() throws Exception {
        	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
        	            }
        	        }, 200, 10);
        		}
        	} else {
        		if (stairsBottom.inViewport()) {
            		if (stairsBottom.interact("Climb-up")) {
            			Condition.wait(new Callable<Boolean>(){
    	    	            @Override
    	    	            public Boolean call() throws Exception {
    	    	                return ctx.players.local().tile().y() < 9000 || stairsTop.inViewport();
    	    	            }
    	    	        }, 200, 10);
            		}
        		} else {
        			//ctx.camera.turnTo(stairsTop);
        			mcam.turnTo(stairsTop);
        		}
        	}
        } else if (ctx.players.local().tile().y() < 9000) {
        	if (walker.walkPathReverse(toStairsTop)) {
        		final int randomNum = Random.nextInt(1, 6);
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
    	            }
    	        }, 200, 10);
    		}
        }
    }
    
    public void walkToMine() {
    	main.updateStatus("Walking to Mine");
        
    	GameObject stairsTop = ctx.objects.select().id(16664).nearest().poll();
        GameObject stairsBottom = ctx.objects.select().id(23969).nearest().poll();
    	
        if (ctx.players.local().tile().y() < 9000) {
        	if (stairsTopTile.distanceTo(ctx.players.local())>5) {
            	if (walker.walkPath(toStairsTop)) {
            		final int randomNum = Random.nextInt(1, 6);
        			Condition.wait(new Callable<Boolean>(){
        	            @Override
        	            public Boolean call() throws Exception {
        	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
        	            }
        	        }, 200, 10);
        		}
        	} else {
        		if (stairsTop.inViewport()) {
            		if (stairsTop.interact("Climb-down")) {
            			Condition.wait(new Callable<Boolean>(){
    	    	            @Override
    	    	            public Boolean call() throws Exception {
    	    	                return ctx.players.local().tile().y() > 9000 || stairsBottom.inViewport();
    	    	            }
    	    	        }, 200, 10);
            		}
        		} else {
        			ctx.camera.turnTo(stairsTop);
        		}
        	}
        } else if (ctx.players.local().tile().y() > 9000) {
        	if (walker.walkPath(toMine)) {
        		final int randomNum = Random.nextInt(1, 6);
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
    	            }
    	        }, 200, 10);
    		}
        }
    }

    public boolean needToReturn() {
    	if (ctx.inventory.select().count() < 28) {
    		if (ctx.players.local().tile().y() < 9000) {
    			return true;
    		} else  if ((mineStartTile.distanceTo(ctx.players.local()) > 6) && (ctx.players.local().tile().y() > 9000)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    public boolean needToBank() {
    	if (ctx.inventory.select().count() > 27 && bankTile.distanceTo(ctx.players.local()) > 6) {
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public boolean activate() {
    	return needToBank() || needToReturn();
    }
   
    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("WALK DWARVEN MINE "+timeStamp);
    	
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToBank()) {
        	walkToBank();
        } else if (needToReturn()) {
        	walkToMine();
        }
    }
}
