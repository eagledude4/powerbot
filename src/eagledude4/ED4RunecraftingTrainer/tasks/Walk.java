package eagledude4.ED4RunecraftingTrainer.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import eagledude4.ED4RunecraftingTrainer.Task;
import eagledude4.ED4RunecraftingTrainer.Main;

import Walker.Walker;

public class Walk extends Task {
	Main main;
	
	private final Walker walker = new Walker(ctx);

	public Tile[] toRuinPath;
	public Tile ruinTile;
	public Tile altarTile;
	public Tile bankTile;
	
	public int runeID;
	public int runeEssenceID = 1436;
	
	public Walk(Main main, ClientContext ctx, Tile ruinTile, Tile altarTile, Tile[] toRuinPath, int runeID) {
        super(ctx);
        this.main = main;
        this.ruinTile = ruinTile;
        this.altarTile = altarTile;
        this.toRuinPath = toRuinPath;
        this.runeID = runeID;
    }
    
	public void walkToBank() {
    	main.updateStatus("Walking to Bank");
    	
    	if (ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) > 5) {
			if (walker.walkPathReverse(toRuinPath)) {
				final int randomInt = Random.nextInt(1, 6);
				Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomInt;
		            }
		        }, 200, 10);
			}
    	}
    }
    
    public void walkToRuin() {
    	main.updateStatus("Walking to Ruin");
    	
    	GameObject Ruin = ctx.objects.select().name("Mysterious Ruins").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Ruin) {
				return Ruin.valid();
			}
    	}).nearest().poll();
    	
    	if (ctx.players.local().tile().distanceTo(ruinTile) > 6) {
			if (walker.walkPath(toRuinPath)) {
				final int randomInt = Random.nextInt(1, 6);
				Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		            	return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomInt;
		            }
		        }, 200, 10);
			}
    	} else {
    		if (Ruin.inViewport()) {
    			Ruin.interact("Enter");
    		} else {
    			ctx.camera.turnTo(Ruin);
    		}
    	}
    }
    
    /*public void walkToAltar() {
    	main.updateStatus("Walking to Altar");
    	
    	GameObject Altar = ctx.objects.select().name("Altar").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Altar) {
				return Altar.valid();
			}
    	}).nearest().poll();
    	
    	if (!Altar.inViewport()) {
    		ctx.camera.turnTo(Altar);
    	}
    	
    	if (ctx.players.local().tile().distanceTo(Altar) > 5) {
    		ctx.movement.step(Altar.tile().derive(1, 1));
    		final int randomInt = Random.nextInt(1, 6);
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomInt;
	            }
	        }, 200, 10);
    	}
    }*/
    
    public void walkToAltar() {
    	main.updateStatus("Walking to Altar");
    	
    	GameObject Altar = ctx.objects.select().name("Altar").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Altar) {
				return Altar.valid();
			}
    	}).nearest().poll();
    	
    	if (!Altar.inViewport()) {
    		if (!ctx.players.local().inMotion()) {
    			ctx.movement.step(Altar.tile().derive(1, 1));
    		}
    		ctx.camera.turnTo(Altar);
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return Altar.inViewport();
	            }
	        }, 500, 10);
    	}
    }
    
    /*public void walkToPortal() {
    	main.updateStatus("Walking to Portal");
    	
    	GameObject Portal = ctx.objects.select().name("Portal").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Portal) {
				return Portal.valid();
			}
    	}).nearest().poll();
    	
    	if (!Portal.inViewport()) {
    		ctx.camera.turnTo(Portal);
    	}
    	
    	if (ctx.players.local().tile().distanceTo(Portal) > 5) {
    		ctx.movement.step(Portal.tile().derive(1, 1));
    	} else {
    		if (Portal.inViewport()) {
    			Portal.interact("Use");
    		} else {
    			ctx.movement.step(Portal.tile().derive(1, 1));
    			final int randomInt = Random.nextInt(1, 6);
    			Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < randomInt;
		            }
		        }, 200, 10);
    		}
    	}
    }*/
    
    public void walkToPortal() {
    	main.updateStatus("Walking to Portal");
    	
    	GameObject Portal = ctx.objects.select().name("Portal").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Portal) {
				return Portal.valid();
			}
    	}).nearest().poll();
    	
    	if (!Portal.inViewport()) {
    		if (!ctx.players.local().inMotion()) {
    			ctx.movement.step(Portal.tile().derive(1, 1));
    		}
    		ctx.camera.turnTo(Portal);
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return Portal.inViewport();
	            }
	        }, 500, 10);
    	} else {
    		Portal.interact("Use");
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) == 0;
	            }
	        }, 200, 10);
    	}
    }

    @Override
    public boolean activate() {
    	boolean needToExitPortal = (ctx.inventory.select().id(runeID).count(true) > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) < 50;
    	
    	boolean needToBank = (ctx.inventory.select().id(runeEssenceID).count() == 0 || (ctx.inventory.select().id(runeEssenceID).count() == 0 && ctx.inventory.select().id(runeID).count(true) > 0))
    			&& ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) > 6;
    	
		boolean needToReturnRuin = (ctx.inventory.select().id(runeEssenceID).count() > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) > 50;
    			
		boolean needToReturnAltar = (ctx.inventory.select().id(runeEssenceID).count() > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) < 50;
    	
    	return needToBank || needToReturnRuin || needToReturnAltar || needToExitPortal;
    }
   
    @Override
    public void execute() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Walk execute "+timeStamp);
    	
    	boolean needToExitPortal = (ctx.inventory.select().id(runeID).count(true) > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) < 50;
    			
    	boolean needToBank = (ctx.inventory.select().id(runeEssenceID).count() == 0 || (ctx.inventory.select().id(runeEssenceID).count() == 0 && ctx.inventory.select().id(runeID).count(true) > 0))
    			&& ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) > 6;
    	
		boolean needToReturnRuin = (ctx.inventory.select().id(runeEssenceID).count() > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) > 50;
    			
		boolean needToReturnAltar = (ctx.inventory.select().id(runeEssenceID).count() > 0)
    			&& ctx.players.local().tile().distanceTo(altarTile) < 50;
		
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
    	if (needToExitPortal) {
    		walkToPortal();
    	} else if (needToBank) {
        	walkToBank();
        } else if (needToReturnRuin) {
        	walkToRuin();
        } else if (needToReturnAltar) {
        	walkToAltar();
        }
    }
}
