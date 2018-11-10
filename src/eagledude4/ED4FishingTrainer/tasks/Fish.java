package eagledude4.ED4FishingTrainer.tasks;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Npc;

//import eagledude4.ED4FishingTrainer.AntibanTimer;
import eagledude4.ED4FishingTrainer.Main;
import eagledude4.ED4FishingTrainer.Task;

import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4Utils.MouseCamera;

public class Fish extends Task {
	public Timer timer;
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Main main;
	
    int fishIds[];
    
    public Tile fishingTile;
	public String fishMethod;
	public String fishLocation;

	public Fish(Main main, ClientContext ctx, int[] fishIds, String fishMethod, String fishLocation) {
        super(ctx);
        this.main = main;
        this.fishIds = fishIds;
        this.fishMethod = fishMethod;
        this.fishLocation = fishLocation;
        
        if (this.fishLocation.equals("Lumbridge")) {
        	this.fishingTile = new Tile(3239, 3146, 0);
        } else if (fishLocation.equals("Barb Village")) {
        	this.fishingTile = new Tile(3101, 3430, 0);
        } else if (fishLocation.equals("Karamja")) {
        	this.fishingTile = new Tile(2924, 3180, 0);
        }
        
        //System.out.println(this.fishMethod);
        //System.out.println(this.fishLocation);
        //System.out.println(this.fishingTile);
    }

    @Override
    public boolean activate() {
        return  ctx.inventory.select().count() <= 27 && 
        		ctx.players.local().animation() ==-1 && 
        		!ctx.players.local().interacting().valid() && 
        		!ctx.players.local().inMotion() && 
        		ctx.players.local().tile().distanceTo(fishingTile) <= 10;
    }

    @Override
    public void execute() {
    	//System.out.println("FISH EXECUTE");
    	killAntibanTimer();
    	
    	Npc Fish = ctx.npcs.select().id(fishIds).nearest().poll();
    	Tile fishTile = Fish.tile();
    	
    	if (!Fish.inViewport()) {
    		if (fishTile.distanceTo(ctx.players.local()) > 3) {
        		Tile tile = reachableTile(fishTile);
        		ctx.movement.step(tile.matrix(ctx)); //move to where the fishes be
        	} else {
        		ctx.camera.pitch(Random.nextInt(90, 99));
    			mcam.turnTo(Fish);
        	}
    	} else {
	    	if (Fish.interact(fishMethod,"Fishing spot")) {
				Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		            	return ctx.players.local().animation()!=-1;
		            }
		    	}, 25, 20);
				startAntibanTimer();
				main.updateStatus("Fishing");
				//System.out.println("DIST: "+ctx.players.local().tile().distanceTo(fishingTile));
	    	}
    	}
    }
    
    private Tile reachableTile(Tile tile) {
        Tile t1 = new Tile(tile.x() + 1, tile.y(), tile.floor());
        Tile t2 = new Tile(tile.x() - 1, tile.y(), tile.floor());
        Tile t3 = new Tile(tile.x(), tile.y() + 1, tile.floor());
        Tile t4 = new Tile(tile.x(), tile.y() - 1, tile.floor());

        if (t1.matrix(ctx).reachable()) {
        	return t1;
        } else if (t2.matrix(ctx).reachable()) {
        	return t2;
        } else if (t3.matrix(ctx).reachable()) {
            return t3;
        } else if (t4.matrix(ctx).reachable()) {
            return t4;
        }
        
        return null;
    }
    
    public void killAntibanTimer() {
    	if (timer != null) {
    		timer.cancel();
    		timer.purge();
    	}
    }
    
    public void startAntibanTimer() {
    	if (timer == null) {
	    	timer = new Timer();
	    	int seconds = 20;
	    	int time = seconds*1000;
	    	timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  //String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
				  //System.out.println("ANTIBAN TIMER ACTIVATE "+timeStamp);
				  
				  antiban.execute(Constants.SKILLS_FISHING);
			  }
			}, time);
    	}
    }
}
