package eagledude4.ED4FishingTrainer.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;

import eagledude4.ED4FishingTrainer.Main;
import eagledude4.ED4FishingTrainer.Task;

//import eagledude4.ED4Utils.Walker;
import STRepo.ST.api.STWalker.STWalker;


public class WalkFishingTutor extends Task {
	Main main;
	
	//private final Walker walker = new Walker(ctx);
	private final STWalker walker = new STWalker(ctx);
	//public String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	
	private Tile walkTile;
	private Tile[] walkPath;
	
	public WalkFishingTutor(Main main, ClientContext ctx, Tile walkTile, Tile[] walkPath) {
		super(ctx);
        this.main = main;
        this.walkTile = walkTile;
        this.walkPath = walkPath;
    }
	
    @Override
    public boolean activate() {        
    	boolean nearFishLocation = (walkTile.distanceTo(ctx.players.local())<=6);
    	boolean needToBank = ctx.inventory.select().count()>27 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
        boolean needToReturn = ctx.inventory.select().count()==1  && (walkTile.distanceTo(ctx.players.local())>6) && !nearFishLocation;
    	
    	return needToBank || needToReturn;
    }
   
    @Override
    public void execute() {
    	//System.out.println("Executing WalkTask "+timeStamp);
    	
    	boolean nearFishLocation = (walkTile.distanceTo(ctx.players.local())<=6);
    	boolean needToBank = ctx.inventory.select().count()>27 && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())>6);
        boolean needToReturn = ctx.inventory.select().count()==1  && (walkTile.distanceTo(ctx.players.local())>6) && !nearFishLocation;
        
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
    	if (needToBank) {
        	main.updateStatus("Walking to Bank");
        	//walker.walkToBank();
        	walker.walkPath(walkPath);
        } else if (needToReturn) {
        	main.updateStatus("Walking to Fish");
        	walker.walkPath(walkTile);
        }
    }
}
