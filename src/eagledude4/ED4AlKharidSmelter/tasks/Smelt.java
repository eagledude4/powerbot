package eagledude4.ED4AlKharidSmelter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4AlKharidSmelter.Main;
import eagledude4.ED4AlKharidSmelter.Task;
import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4Utils.MouseCamera;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Smelt extends Task {
	public Timer timer;
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	Main main;
	
	public int[] oreIds;
	public int barID;
    
    public Tile furnaceTile;
    
    public Smelt(Main main, ClientContext ctx, Tile furnaceTile, int[] oreIds, int barID) {
        super(ctx);
        this.furnaceTile = furnaceTile;
        this.oreIds = oreIds;
        this.barID = barID;
        this.main = main;
    }

    @Override
    public boolean activate() {
    	return  ctx.inventory.select().id(oreIds).count() > 0 &&
    			ctx.players.local().animation() == -1 && 
    			!ctx.players.local().inCombat() && 
    			!ctx.players.local().inMotion() &&
    			ctx.players.local().tile().distanceTo(furnaceTile) < 10;
    }

    @Override
    public void execute() {
    	GameObject Furnace = ctx.objects.select().id(24009).nearest().poll();
        
    	if (!Furnace.inViewport() && (ctx.players.local().tile().distanceTo(Furnace) < 10)) {
    		//ctx.camera.turnTo(Furnace);
        	mcam.turnTo(Furnace);
    	}
    	
    	smelt(Furnace);
    }
    
    public void smelt(GameObject Furnace) {
    	Component smeltingMenu = ctx.widgets.component(270, 0);
    	Component allButton = ctx.widgets.component(270, 12).component(0);
    	Component smeltButton = ctx.widgets.component(270, 14);
    	if (barID == 2349) {
    		smeltButton = ctx.widgets.component(270, 14);
    	} else if (barID == 2351) {
    		smeltButton = ctx.widgets.component(270, 15);
    	} else if (barID == 2353) {
    		smeltButton = ctx.widgets.component(270, 17);
    	} else if (barID == 2357) {
    		smeltButton = ctx.widgets.component(270, 18);
    	} else if (barID == 2359) {
    		smeltButton = ctx.widgets.component(270, 19);
    	} else if (barID == 2361) {
    		smeltButton = ctx.widgets.component(270, 20);
    	}
    	

    	if (!smeltingMenu.visible()) {
    		if (Furnace.hover()) {
	        	if (ctx.menu.items()[0].toString().contains("Smelt")) {
	        		Furnace.click();
	        	} else {
	        		interact(Furnace,"Smelt");
	        	}
    		}
    	} else {
    		if (allButton.textureId() == 1545) {
        		allButton.click();
        	}
    	
        	Condition.sleep(Random.nextInt(500, 1000));
    		
        	smeltButton.click();
        	
        	/*Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return !smeltingMenu.visible();
                }
            }, 25, 20);*/
        	
    		main.updateStatus("Smelting");
    		//newAntibanTimer();
    		
    		Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(oreIds).count() == 0;
                }
            }, 1000, 30);
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
    
    public void killAntibanTimer() {
    	if (timer != null) {
    		timer.cancel();
    		timer.purge();
    	}
    }
    
    public void startAntibanTimer() {
    	Timer timer = new Timer();
    	int time = 60*1000;
    	timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			  System.out.println("ANTIBAN TIMER ACTIVATE "+timeStamp);
			  
			  antiban.execute(Constants.SKILLS_SMITHING);
		  }
		}, time);
    }
}
