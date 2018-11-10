package eagledude4.ED4CookingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4CookingTrainer.Main;
import eagledude4.ED4CookingTrainer.Task;

import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4Utils.AntibanTimer;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Cook extends Task {
	public Timer timer;
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Main main;
	Pattern pattern = Pattern.compile("(Raw.*)");
	
	int fishIds[];

    public Cook(Main main, ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.main = main;
        this.fishIds = fishIds;
    }

    @Override
    public boolean activate() {
    	Tile cookingTile = new Tile(3210, 3215, 0);
    	
        return  (ctx.inventory.select().id(fishIds).count() > 0) && ctx.players.local().animation()==-1 && !ctx.players.local().interacting().valid() && (cookingTile.distanceTo(ctx.players.local())<=5);
    }

    @Override
    public void execute() {
    	killAntibanTimer();
    	String fishString = ctx.inventory.itemAt(0).name().toLowerCase();
    			
    	main.updateStatus("Cooking "+fishString);
    	
    	GameObject cookingRange = ctx.objects.select().id(114).nearest().poll();
    	
    	if (cookingRange.inViewport()) {
	    	Component cookingMenu = ctx.widgets.component(270, 0);
	    	Component allButton = ctx.widgets.component(270, 12).component(0);
	    	Component cookButton = ctx.widgets.component(270, 14);
	    		    	
	    	Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return !ctx.players.local().inMotion();
                }
            }, 25, 20);
	    	
	    	if (!cookingMenu.visible()) {
		    	for(Item fish: ctx.inventory.select().name(pattern)){
		            if(ctx.controller.isStopping()){
		                break;
		            }
		            
		            int x = fish.centerPoint().x - 11 + Random.nextInt(0, 25);
		            int y = fish.centerPoint().y - 12 + Random.nextInt(0, 25);
		            
		            if (ctx.input.click(new Point(x, y), true)) {
		            	break;
		            }
		        }
		   
		    	int rangeX = cookingRange.centerPoint().x - 11 + Random.nextInt(0, 25);
		        int rangeY = cookingRange.centerPoint().y - 12 + Random.nextInt(0, 25);
		       
		        ctx.input.click(new Point(rangeX, rangeY), false);
	            if (!ctx.menu.click(Menu.filter("Use"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
	            	cookingRange.interact("Use");//if not then interact() as normal, just incase
	            };
	            Condition.wait(new Callable<Boolean>(){
	                @Override
	                public Boolean call() throws Exception {
	                	return cookingMenu.visible();
	                }
	            }, 25, 20);
            } else {
	        	if (allButton.textureId() == 1545) {
	        		allButton.click();
	        	}
        	
	        	Condition.sleep(Random.nextInt(500, 1000));
        		cookButton.click();
        		startAntibanTimer();
        		
        		Condition.wait(new Callable<Boolean>(){
	                @Override
	                public Boolean call() throws Exception {
	                	return !cookingMenu.visible();
	                }
	            }, 25, 20);
	        	
	        	Condition.wait(new Callable<Boolean>(){
	                @Override
	                public Boolean call() throws Exception {
	                    return ctx.inventory.select().id(fishIds).count() == 0;
	                }
	            }, 25, 20);
	        }
    	} else {
    		mcam.turnTo(cookingRange);
    	}
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
