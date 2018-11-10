package eagledude4.ED4WoodcuttingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.BasicQuery;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4Utils.AntibanTimer;
import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4WoodcuttingTrainer.Task;
import eagledude4.ED4WoodcuttingTrainer.Main;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.concurrent.Callable;

public class Chop extends Task {
	Main main;
	public Timer timer;
	private final AntibanTimer antiban = new AntibanTimer(ctx);
	private final MouseCamera mcam = new MouseCamera(ctx);
	
    private int treeIds[];
    private final Tile chopStartTile;
    
    public Chop(Main main, ClientContext ctx, int[] treeIds, Tile chopStartTile) {
        super(ctx);
        this.main = main;
        this.treeIds = treeIds;
        this.chopStartTile = chopStartTile;
    }

    @Override
    public boolean activate() {
    	return  ctx.inventory.select().count() <= 27 && 
    			ctx.players.local().animation() == -1 && 
    			!ctx.players.local().inCombat() && 
    			ctx.players.local().tile().distanceTo(chopStartTile) < 20 &&
    			ctx.players.local().tile().x() < 3200;
    }

    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Executing Chop Task "+timeStamp);
        
    	BasicQuery<GameObject> Trees = ctx.objects.select(20).id(treeIds);
        Trees = Trees.select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject Tree) {
                return Tree.tile().distanceTo(chopStartTile)<20;
            }
        });
        GameObject Tree =  Trees.id(Trees.stream().mapToInt(GameObject::id).max().getAsInt()).nearest().poll();
        
        if (!Tree.inViewport()) {
    		main.updateStatus("Looking for "+Tree.name());
    		mcam.turnTo(Tree);
    		/*if (Tree.tile().distanceTo(ctx.players.local()) > 10) {
    			main.updateStatus("Moving to Tree");
    			
    			Tile testTile = Tree.tile().derive(2, 2);
    			if (testTile.matrix(ctx).reachable()) {
    				ctx.movement.step(testTile);
    			}
    		} else if (Tree.tile().distanceTo(ctx.players.local()) < 10) {
		        mcam.turnTo(Tree);
			}*/
		}
        
        chop(Tree);
    }
    
    public void chop(GameObject Tree) {
    	main.updateStatus("Chopping "+Tree.name());
    	killAntibanTimer();
    	
    	if (Tree.hover()) {
        	if (ctx.menu.items()[0].toString().contains("Chop down")) {
	        	if (Tree.click()) {
	        		//main.Tree = Tree;
	        		startAntibanTimer();
	        	}
        	} else {
        		if (interact(Tree,"Chop down")) {
        			//main.Tree = Tree;
        			startAntibanTimer();
	        	}
        	}
        	
        	Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().animation() != -1; //prevent spam clicking of the Tree
	            }
	        }, 500, 10);
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
    	if (timer == null) {
	    	timer = new Timer();
	    	int seconds = 60;
	    	int time = seconds*1000;
	    	timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
				  //System.out.println("ANTIBAN TIMER ACTIVATE "+timeStamp);
				  
				  antiban.execute(Constants.SKILLS_MINING);
			  }
			}, time);
    	}
    }
}
