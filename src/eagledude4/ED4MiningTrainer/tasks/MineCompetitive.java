package eagledude4.ED4MiningTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.BasicQuery;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;
import org.powerbot.script.rt4.Player;

import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4Utils.WorldSwitch;
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

public class MineCompetitive extends Task {
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final WorldSwitch worldSwitch = new WorldSwitch(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	Main main;
	
	int rockIds[];

    //Tile mineStartTile;

    public MineCompetitive(Main main, ClientContext ctx, int[] rockIds) {
        super(ctx);
        this.main = main;
        this.rockIds = rockIds;
    }

    @Override
    public boolean activate() {
    	final List<Player> cache = new ArrayList<Player>();
    	ctx.players.select().addTo(cache);
    	BasicQuery<GameObject> nearRocks = ctx.objects.select(15).id(rockIds);
    	GameObject Rock = nearRocks.select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Rock) {
				//return ctx.players.local().tile().distanceTo(Rock) <= 1; //set for competitive iron mining
				return Rock.tile().y() > 9730;
			}
    	}).nearest().poll();
    	
    	Player competitingPlayer = ctx.players.select().select(new Filter<Player>() {
			@Override
			public boolean accept(Player player) {
				return !player.equals(ctx.players.local());
			}
    	}).within(Rock, 1).nearest().poll();
    	main.competitingPlayer = competitingPlayer;
    	
    	return  ctx.players.local().tile().y() > 9000 &&
    			ctx.players.local().tile().x() >= 3026 &&
    			ctx.inventory.select().count() <= 27 &&
    			//!ctx.inventory.selectedItem().valid() && 
    			!ctx.players.local().inCombat() &&
    			//!ctx.players.local().inMotion() &&
    			((nearRocks.size() > 1) && competitingPlayer.valid() && (ctx.players.local().tile().distanceTo(Rock) > competitingPlayer.tile().distanceTo(Rock)));
    			//ctx.groundItems.select(20).id(main.oreID).size() == 0 &&
    }

    @Override
    public void execute() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("MINE COMPETITIVE "+timeStamp);
    	
    	BasicQuery<GameObject> nearRocks = ctx.objects.select(15).id(rockIds);
    	if (nearRocks.size() > 1) {
    		final List<Player> cache = new ArrayList<Player>();
        	ctx.players.select().addTo(cache);
	    	nearRocks = nearRocks.select(new Filter<GameObject>() {
				@Override
				public boolean accept(GameObject Rock) {
					//return ctx.players.local().tile().distanceTo(Rock) <= 1; //set for competitive iron mining
					return Rock.tile().y() > 9730 && reachable(Rock) && ctx.players.select(cache).within(Rock, 1).size() == 0;
				}
	    	});
    	} else {
    		nearRocks = nearRocks.select(new Filter<GameObject>() {
				@Override
				public boolean accept(GameObject Rock) {
					//return ctx.players.local().tile().distanceTo(Rock) <= 1; //set for competitive iron mining
					return Rock.tile().y() > 9730 && reachable(Rock);
				}
	    	});
    	}
    	
    	/*final List<Player> cache = new ArrayList<Player>();
    	ctx.players.select().addTo(cache);
    	double radius = 3d;
    	nearRocks.sort(new Comparator<GameObject>() {
    	    @Override
    	    public int compare(GameObject o1, GameObject o2) {
    	        int c1 = ctx.players.select(cache).within(o1, radius).size();
    	        int c2 = ctx.players.select(cache).within(o2, radius).size();
    	        return Integer.compare(c1, c2);
    	    }
    	});*/
    	
    	GameObject Rock = nearRocks.nearest().poll();
    	
    	if ((ctx.players.local().tile().distanceTo(Rock) > 1) ||  (ctx.players.local().animation() == -1)) {
	    	if (!Rock.inViewport()) {
	    		if (ctx.players.local().tile().distanceTo(Rock) < 10) {
	        		//ctx.camera.turnTo(Rock);
	        		mcam.turnTo(Rock);
	        	} else {
	        		if (Rock.tile().y() > 9730 && reachable(Rock)) {
	        			ctx.movement.step(Rock.tile().derive(2, 2));
	        		}
	        		
	        		if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
	                    ctx.movement.running(true);
	                }
	        	}
	    	}
	    	
	    	mine(Rock);
    	}
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
    	main.updateStatus("Mining Competitively");
    	
    	if (Rock.hover()) {
        	if (ctx.menu.items()[0].toString().contains("Mine")) {
	        	if (Rock.click()) {
	        		newDefeatTimer();
	        		newAntibanTimer();
	        		main.Rock = Rock;
	        	}
        	} else {
        		if (interact(Rock,"Mine")) {
	        		newDefeatTimer();
	        		newAntibanTimer();
	        		main.Rock = Rock;
	        	}
        	}
    	}
    	
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
    
    public void newDefeatTimer() {
    	Timer timer = new Timer();
    	int seconds = 10;
    	int time = (60*seconds) * 1000;
    	final int startinv = ctx.inventory.select().count();
    	timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  //String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			  //System.out.println("DEFEAT TIMER ACTIVATE "+timeStamp);
			  
			  if (ctx.inventory.select().count() == startinv) {
				  worldSwitch.switchWorld();
			  }
		  }
		}, time);
    }
    
    public void newAntibanTimer() {
    	Timer timer = new Timer();
    	int seconds = 60;
    	int time = seconds*1000;
    	timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  //String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			  //System.out.println("ANTIBAN TIMER ACTIVATE "+timeStamp);
			  
			  antiban.execute(Constants.SKILLS_MINING);
		  }
		}, time);
    }
}
