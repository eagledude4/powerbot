package eagledude4.ED4SmithingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4SmithingTrainer.Main;
//import eagledude4.ED4baringTrainer.AntibanTimer;
import eagledude4.ED4SmithingTrainer.Task;
import eagledude4.ED4Utils.AntibanTimer;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Smith extends Task {
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	
	Main main;
	
	public int barID;
    
    public Tile AnvilTile;
    
    public Smith(Main main, ClientContext ctx, Tile AnvilTile, int barID) {
        super(ctx);
        this.main = main;
        this.AnvilTile = AnvilTile;
        this.barID = barID;
    }

    @Override
    public boolean activate() {
    	return  ctx.inventory.select().id(barID).count() > 0 &&
    			ctx.players.local().animation() == -1 && 
    			!ctx.players.local().inCombat() && 
    			!ctx.players.local().inMotion() &&
    			ctx.players.local().tile().distanceTo(AnvilTile) < 10;
    }

    @Override
    public void execute() {
    	GameObject Anvil = ctx.objects.select().id(2097).nearest().poll();
    	
    	if (Anvil.inViewport()) {
    		Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return !ctx.players.local().inMotion();
                }
            }, 25, 20);
    		
	    	smith(Anvil);
    	} else {
    		ctx.camera.turnTo(Anvil);
    	}
    }
    
    public Component thingToSmith(int smithingLevel) {
    	if (barID == 2349) { //bronze
	    	if (smithingLevel >= 1 && smithingLevel < 5) {
	    		return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 5 && smithingLevel < 9) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 9 && smithingLevel < 15) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	} else if (barID == 2351) { //iron
    		if (smithingLevel >= 15 && smithingLevel < 20) {
    			return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 20 && smithingLevel < 24) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 24 && smithingLevel < 30) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	} else if (barID == 2351) { //steel
    		if (smithingLevel >= 30 && smithingLevel < 35) {
    			return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 35 && smithingLevel < 39) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 39 && smithingLevel < 50) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	} else if (barID == 2351) { //steel
    		if (smithingLevel >= 30 && smithingLevel < 35) {
    			return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 35 && smithingLevel < 39) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 39 && smithingLevel < 50) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	} else if (barID == 2359) { //mithril
    		if (smithingLevel >= 50 && smithingLevel < 55) {
    			return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 55 && smithingLevel < 59) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 59 && smithingLevel < 70) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	} else if (barID == 2361) { //mithril
    		if (smithingLevel >= 70 && smithingLevel < 75) {
    			return ctx.widgets.component(312, 2); //dagger - 12.5 exp- 1 bar
	    	} else if (smithingLevel >= 75 && smithingLevel < 79) {
	    		return ctx.widgets.component(312, 4); //scimitar - 25 exp - 2 bar
	    	} else if (smithingLevel >= 79 && smithingLevel < 80) {
	    		return ctx.widgets.component(312, 9); //warhammer - 37.5 exp - 3 bars
	    	}
    	}

    	return null;
    }
    
    public void smith(GameObject Anvil) {
    	Component smithingMenu = ctx.widgets.component(312, 1);
    	Component thingToSmith = thingToSmith(main.currentLevel);
    		    
    	if (!smithingMenu.visible()) {
    		if (Anvil.hover()) {
	        	if (ctx.menu.items()[0].toString().contains("Smith")) {
	        		Anvil.click();
	        	} else {
	        		interact(Anvil,"Smith");
	        	}
    		}
    		Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return smithingMenu.visible();
                }
            }, 50, 20);
    	} else {
    		thingToSmith.interact("Smith All");
    		
    		Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return !smithingMenu.visible();
                }
            }, 50, 20);
    
    		main.updateStatus("Smithing");
    		newAntibanTimer();
    		
    		Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(barID).count() == 0;
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
    
    public void newAntibanTimer() {
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
