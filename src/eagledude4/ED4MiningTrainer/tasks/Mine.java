package eagledude4.ED4MiningTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;
import org.powerbot.script.rt4.Player;

import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4MiningTrainer.Main;
import eagledude4.ED4MiningTrainer.Task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Mine extends Task {
	public Timer timer;
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	Main main;
	
	int rockIds[];
	int rockMining[];

    //Tile mineStartTile;

    public Mine(Main main, ClientContext ctx, int[] rockIds) {
        super(ctx);
        this.main = main;
        this.rockIds = rockIds;
    }

    @Override
    public boolean activate() {
    	return  ctx.players.local().tile().y() > 9000 &&
    			ctx.players.local().tile().x() >= 3026 &&
    			ctx.inventory.select().count() <= 27 &&
    			!ctx.inventory.selectedItem().valid() && 
    			!ctx.players.local().inCombat() &&
    			!ctx.players.local().inMotion() &&
    			ctx.groundItems.select(20).id(main.oreID).size() == 0;
    }

    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("MINE "+timeStamp);
    	
    	final List<Player> cache = new ArrayList<Player>();
    	ctx.players.select().addTo(cache);
    	GameObject Rock = getRock();
    	
    	Player competitingPlayer = ctx.players.select().select(new Filter<Player>() {
			@Override
			public boolean accept(Player player) {
				return !player.equals(ctx.players.local());
			}
    	}).within(Rock, 1).nearest().poll();
    	
    	
    	if (((ctx.players.local().tile().distanceTo(Rock) > 1) ||  (ctx.players.local().animation() == -1))) {
    		if (Rock.valid()) {
    			killAntibanTimer();
	    		
    			if (!Rock.inViewport()) {
		    		if (ctx.players.local().tile().distanceTo(Rock) < 10) {
		        		//ctx.camera.turnTo(Rock);
		        		mcam.turnTo(Rock);
		        	} else {
		        		if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
		                    ctx.movement.running(true);
		                }
		        		
		        		if (Rock.tile().y() > 9730 && reachable(Rock)) {
		        			ctx.movement.step(Rock.tile().derive(2, 2));
		        		}
		        	}
		    	}
		    	
		    	if (!competitingPlayer.valid()) {
		    		mine(Rock);
		    	} else {
		    		mineCompetitive(Rock, competitingPlayer);
		    	}
    		} else {
    			main.updateStatus("Waiting for Rock");
        		startAntibanTimer();
    		}
    	} else {
    		main.updateStatus("Mining");
    		hoverNextRock();
    	}
    }
    
    private void hoverNextRock() {
    	GameObject Rock = ctx.objects.select().id(rockMining).select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Rock) {
				//return ctx.players.local().tile().distanceTo(Rock) <= 1; //set for competitive iron mining
				return Rock.tile().y() > 9730 && Rock.tile().y() < 9756 && Rock.tile().distanceTo(ctx.players.local().tile()) > 1;
			}
    	}).nearest().poll();
    	
    	if (Rock.valid()) {
	    	double RockPointX = Rock.centerPoint().getX();
			double RockPointY = Rock.centerPoint().getX();
			double MousePointX = ctx.input.getLocation().getX();
			double MousePointY = ctx.input.getLocation().getY();
			//double MouseToRockDistance = Math.hypot(RockPointX - MousePointX, RockPointY - MousePointY);
			//System.out.println("Dist 1 "+MouseToRockDistance);
			Rock.hover();
			Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().animation() == -1; //prevent spam clicking of the rock
	            }
	        }, 500, 10);
    	} else {
    		startAntibanTimer();
    	}
    }
    
    private GameObject getRock() {
    	GameObject Rock = ctx.objects.select().id(rockIds).select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Rock) {
				//return ctx.players.local().tile().distanceTo(Rock) <= 1; //set for competitive iron mining
				return Rock.tile().y() > 9730 && Rock.tile().y() < 9756;
			}
    	}).nearest().poll();
    	
    	if (!Rock.valid() && (main.oreID != 453)) {
	    	int COAL_ROCK_IDS[] = { 7456, 7489 };
	    	rockMining = COAL_ROCK_IDS;
	    	return ctx.objects.select().id(COAL_ROCK_IDS).select(new Filter<GameObject>() {
				@Override
				public boolean accept(GameObject Coal) {
					return Coal.tile().y() > 9730 && Coal.tile().y() < 9756;
				}
	    	}).nearest().poll();
	    } else {
	    	rockMining = rockIds;
	    	return Rock;
	    }
    	
    	//return rockToReturn;
    }
    
    private boolean reachable(GameObject go) {
        int a = go.width();
        Tile t1 = new Tile(go.tile().x() + a, go.tile().y(), go.tile().floor());
        Tile t2 = new Tile(go.tile().x() - a, go.tile().y(), go.tile().floor());
        Tile t3 = new Tile(go.tile().x(), go.tile().y() + a, go.tile().floor());
        Tile t4 = new Tile(go.tile().x(), go.tile().y() - a, go.tile().floor());

        return (t1.matrix(ctx).reachable() || t2.matrix(ctx).reachable() || t3.matrix(ctx).reachable() || t4.matrix(ctx).reachable());
    }
    
    public void mine(GameObject Rock) {
    	//main.updateStatus("Mining");
    	killAntibanTimer();
    	
    	if (Rock.hover()) {
        	if (ctx.menu.items()[0].toString().contains("Mine")) {
	        	if (Rock.click()) {
	        		//main.Rock = Rock;
	        		startAntibanTimer();
	        	}
        	} else {
        		if (interact(Rock,"Mine")) {
        			//main.Rock = Rock;
        			startAntibanTimer();
	        	}
        	}
        	
        	Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().animation() != -1; //prevent spam clicking of the rock
	            }
	        }, 500, 10);
    	}
    }
    
    public void mineCompetitive(GameObject Rock, Player player) {
    	//main.updateStatus("Mining");
    	killAntibanTimer();
    	
    	Rock.click();
    	
    	Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1; //prevent spam clicking of the rock
            }
        }, 500, 10);
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
    	if (timer == null) {
	    	timer = new Timer();
	    	int seconds = 60;
	    	int time = seconds*1000;
	    	timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
				  System.out.println("ANTIBAN TIMER ACTIVATE "+timeStamp);
				  
				  antiban.execute(Constants.SKILLS_MINING);
			  }
			}, time);
    	}
    }
}
