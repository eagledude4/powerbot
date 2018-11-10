package eagledude4.ED4MiningTrainer.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
//import org.powerbot.script.rt4.TilePath;
import org.powerbot.script.rt4.TilePath;

import eagledude4.ED4MiningTrainer.Task;
import eagledude4.ED4MiningTrainer.Main;

import eagledude4.ED4Utils.MouseCamera;
import Walker.Walker;

public class WalkMiningGuild extends Task {
	private final MouseCamera mcam = new MouseCamera(ctx);
	Main main;
	
	private final Walker walker = new Walker(ctx);
	
	public final Tile bankTile = new Tile(3013, 3355, 0);
	public final Tile ladderTopTile = new Tile(3019, 3336, 0);
	public final Tile ladderBottomTile = new Tile(3021, 9739, 0);
	public Tile mineStartTile;
	public Tile toMine[];
	public TilePath pathToMine;
	public TilePath pathToLadderBottom;
	
	public WalkMiningGuild(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
        
        if (main.oreID == 440) { //Iron
        	this.mineStartTile = new Tile(3033, 9738, 0);
        	this.toMine = new Tile[] {
        		ladderBottomTile,
        		new Tile(3026, 9739, 0),
    			mineStartTile
        	};
		} else if (main.oreID == 453) { //coal
        	this.mineStartTile = new Tile(3026, 9740, 0);
        	this.toMine = new Tile[] {
        		ladderBottomTile,
    			mineStartTile
        	};
        } else if (main.oreID == 447) { //mithril
        	this.mineStartTile = new Tile(3052, 9738, 0);
        	this.toMine = new Tile[] {
        		ladderBottomTile,
        		new Tile(3026, 9739, 0),
        		new Tile(3034, 9737, 0),
    			mineStartTile
        	};
        } else  if (main.oreID == 449) { //adamant
        	this.mineStartTile = new Tile(3039, 9736, 0);
        	this.toMine = new Tile[] {
        		ladderBottomTile,
        		new Tile(3026, 9739, 0),
        		new Tile(3034, 9737, 0),
    			mineStartTile
        	};
        }
        
        //pathToMine = ctx.movement.newTilePath(toMine);
        //pathToLadderBottom = pathToMine.reverse();
    }
	
	public boolean needToWalkToBank() {
		if (ctx.players.local().tile().y() < 9000 && 
				ctx.players.local().tile().distanceTo(bankTile) > 6 && 
				ctx.inventory.select().count() > 27) {
			return true;
		}
		return false;
	}
	
	public boolean needToWalkToLadderTop() {
		if (ctx.players.local().tile().y() < 9000 && 
				ctx.inventory.select().count() < 28) {
			return true;
		}
		return false;
	}
	
	public boolean needToWalkToMine() {
		int tileX = 0;
		
		if (main.oreID == 453) { //coal
			tileX = 3026;
		} else if (main.oreID == 447) { //mithril
			tileX = 3046;
		} else if (main.oreID == 449) { //adamant
			tileX = 3034;
		}
				
		if (ctx.players.local().tile().y() > 9733 && 
				ctx.players.local().tile().x() < tileX && 
				ctx.inventory.select().count() == 0) {
			return true;
		}
		return false;
	}
	
	public boolean needToWalkToLadderBottom() {
		if (ctx.players.local().tile().y() > 9000 && 
				ctx.inventory.select().count() > 27) {
			return true;
		}
		return false;
	}

    @Override
    public boolean activate() {
    	return needToWalkToBank() || needToWalkToLadderTop() || needToWalkToMine() || needToWalkToLadderBottom();
    }
   
    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("WALK MINING GUILD "+timeStamp);
    	
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        if (needToWalkToBank()) {
        	walkToBank();
        } else if (needToWalkToLadderTop()) {
        	walkToLadderTop();
        } else if (needToWalkToMine()) {
        	walkToMine();
        } else if (needToWalkToLadderBottom()) {
        	walkToLadderBottom();
        }
    }
    
    public void walkToBank() {
    	main.updateStatus("Walking to Bank");
    	
    	if (!ctx.players.local().inMotion()) {
	    	ctx.movement.step(bankTile.derive(3, 1));
	    	
	    	final int randomNum = Random.nextInt(1, 6);
	    	Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
	            }
	        }, 200, 10);
    	}
    }
    
    public void walkToLadderTop() {
    	GameObject ladderTop = ctx.objects.select().id(30367).nearest().poll();
    	GameObject ladderBottom = ctx.objects.select().id(17385).nearest().poll();
    	
    	if (ladderTopTile.distanceTo(ctx.players.local())>5) {
        	//System.out.println("LETS WALK 2 "+timeStamp);
        	
    		if (!ctx.players.local().inMotion()) {
	    		ctx.movement.step(ladderTopTile.derive(3, 3));
	    		
	    		final int randomNum = Random.nextInt(1, 6);
	        	Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
		            }
		        }, 200, 10);
    		} else {
    			main.updateStatus("Walking to Ladder");
    		}
    	} else {
    		if (!ladderTop.inViewport()) {
    			ctx.camera.turnTo(ladderTop);
    			mcam.turnTo(ladderTop);
    		} 
    		
    		if (ladderTop.interact("Climb-down")) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().y() > 9000 || ladderBottom.inViewport();
    	            }
    	        }, 200, 10);
    		}
    	}
    }
    
    public void walkToMine() {
    	main.updateStatus("Walking to Mine");
        
    	if (walker.walkPath(toMine)) {
    		final int randomNum = Random.nextInt(1, 6);
			Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
	            }
	        }, 200, 10);
		}
    	//pathToMine.traverse();
    }

    public void walkToLadderBottom() {
    	GameObject ladderTop = ctx.objects.select().id(30367).nearest().poll();
    	GameObject ladderBottom = ctx.objects.select().id(17385).nearest().poll();
    	
    	if (ladderBottomTile.distanceTo(ctx.players.local())>5) {
    		if (!ctx.players.local().inMotion()) {
	    		if (walker.walkPathReverse(toMine)) {
	        		final int randomNum = Random.nextInt(1, 6);
	    			Condition.wait(new Callable<Boolean>(){
	    	            @Override
	    	            public Boolean call() throws Exception {
	    	            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomNum;
	    	            }
	    	        }, 200, 10);
	    		}
	    		//pathToLadderBottom.traverse();
    		} else {
    			main.updateStatus("Walking to Ladder");
    		}
    	} else {
    		if (!ladderBottom.inViewport()) {
    			//ctx.camera.turnTo(ladderBottom);
    			mcam.turnTo(ladderBottom);
    		}
    		
    		if (ladderBottom.interact("Climb-up")) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().y() < 9000 || ladderTop.inViewport();
    	            }
    	        }, 200, 10);
    		}
    	}
    }
}
