package eagledude4.ED4RunecraftingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4RunecraftingTrainer.Main;
import eagledude4.ED4RunecraftingTrainer.Task;

//import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.awt.Point;
//import java.text.SimpleDateFormat;

public class Craft extends Task {
	Main main;
	public Tile altarTile;
	
	public int runeID;
	public int runeEssenceID = 1436;

    public Craft(Main main, ClientContext ctx, Tile altarTile, int runeID) {
        super(ctx);
        this.runeID = runeID;
        this.altarTile = altarTile;
        this.main = main;
    }

    @Override
    public boolean activate() {
    	GameObject Altar = ctx.objects.select().name("Altar").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Altar) {
				return Altar.valid();
			}
    	}).nearest().poll();
    	
    	return ctx.inventory.select().id(runeEssenceID).count() > 0 &&
    			Altar.inViewport() &&
    			//!ctx.inventory.selectedItem().valid() && 
    			//ctx.players.local().animation() == -1 && 
    			//!ctx.players.local().inCombat() &&
    			//!ctx.players.local().inMotion() && 
    			//ctx.players.local().tile().distanceTo(altarTile) < 5;
    			ctx.players.local().tile().distanceTo(altarTile) < 10;
    }

    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Craft execute "+timeStamp);
    	
    	GameObject Altar = ctx.objects.select().name("Altar").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Altar) {
				return Altar.valid();
			}
    	}).nearest().poll();
    	
    	if (!Altar.inViewport()) {
			ctx.camera.turnTo(Altar);
		} else {
			final int inventCount = ctx.inventory.select().id(runeID).count(true);
			
			craft(Altar);
			
			Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(runeID).count(true) != inventCount;
                }
            }, 200, 10);
        	main.updateRunesCrafted(ctx.inventory.select().id(runeID).count(true));
		}
    }
    
    public void craft(GameObject Altar) {
    	main.updateStatus("Crafting runes");
    	
    	if (Altar.hover()) {
        	if (ctx.menu.items()[0].toString().contains("Craft-rune")) {
	        	if (Altar.click()) {
	        		Condition.wait(new Callable<Boolean>(){
	    	            @Override
	    	            public Boolean call() throws Exception {
	    	                return ctx.players.local().animation() == -1;
	    	            	//return !Altar.valid();
	    	            }
	    	        }, 200, 10);
	        		
	        		//newTimer();
	        	}
        	} else {
        		if (interact(Altar,"Craft-rune")) {
        			Condition.wait(new Callable<Boolean>(){
	    	            @Override
	    	            public Boolean call() throws Exception {
	    	                return ctx.players.local().animation() == -1;
	    	            	//return !Altar.valid();
	    	            }
	    	        }, 200, 10);
        			
        			//newTimer();
	        	}
        	}
    	}
    }
    
    public boolean interact(GameObject object, String choice) {
    	Point point = object.centerPoint();
        
    	ctx.input.click(new Point(point), false);
        if (!ctx.menu.click(Menu.filter(choice))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
        	if (object.interact(choice)) { //if not then interact() as normal, just incase
        		return true;
        	}
        }
    	
        return false;
    }
    
    public void newTimer() {
    	Timer timer = new Timer();
    	int time = (60 * Random.nextInt(1,2)) * 1000;
    	timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  if (Main.Status == "Crafting runes") {
				  new AntibanTimer(ctx);
			  }
		  }
		}, time);
    }
}
