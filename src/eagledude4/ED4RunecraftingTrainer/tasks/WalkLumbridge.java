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

public class WalkLumbridge extends Task {
	Main main;
	
	private final Walker walker = new Walker(ctx);
	
	public Tile[] toRuinPath;
	public Tile stairsBottomTile;
	public Tile stairsTopTile;
	public Tile ruinTile;
	public Tile altarTile;
	public Tile bankTile;
	//public Tile bankTile = toRuinPath[0];
	
	public int runeID;
	public int runeEssenceID = 1436;
	
	int barID;

	public WalkLumbridge(Main main, ClientContext ctx, Tile stairsBottomTile, Tile stairsTopTile, Tile ruinTile, Tile altarTile, Tile[] toRuinPath, int runeID) {
		super(ctx);
		this.main = main;
		this.stairsBottomTile = stairsBottomTile;
        this.stairsTopTile = stairsTopTile;
        this.ruinTile = ruinTile;
        this.altarTile = altarTile;
        this.toRuinPath = toRuinPath;
        this.runeID = runeID;
    }
	
	public Tile getBankTile(GameObject Bank) {
		Tile bankTile = Bank.tile();
		Tile newBankTile = bankTile;
		if (Bank.orientation() == 0) {
			newBankTile = new Tile(bankTile.x(), bankTile.y() + 1, bankTile.floor());
			newBankTile = newBankTile.derive(1, 0);
		} else if (Bank.orientation() == 90) {
			newBankTile = new Tile(bankTile.x() + 1, bankTile.y(), bankTile.floor());
			newBankTile = newBankTile.derive(0, 1);
		} else if (Bank.orientation() == 180) {
			newBankTile = new Tile(bankTile.x(), bankTile.y() - 1, bankTile.floor());
			newBankTile = newBankTile.derive(0, 1);
		} else if (Bank.orientation() == 270) {
			newBankTile = new Tile(bankTile.x() - 1, bankTile.y(), bankTile.floor());
			newBankTile = newBankTile.derive(1, 0);
		}
		
		return newBankTile;
	}
    
    public void walkToBank() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("WALK TO BANK "+timeStamp);
    	
    	main.updateStatus("Walking to Bank");
        
    	GameObject stairsBottom = ctx.objects.select().id(16671).nearest().poll();
        GameObject stairsMiddle = ctx.objects.select().id(16672).nearest().poll();
        
        GameObject Bank = ctx.objects.select().at(ctx.bank.nearest().tile()).select(new Filter<GameObject>() {
		@Override
			public boolean accept(GameObject Bank) {
				return Bank.valid();
			}
		}).nearest().poll();
        Tile bankTile = getBankTile(Bank);
        
        GameObject Portal = ctx.objects.select().name("Portal").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Portal) {
				return Portal.valid();
			}
    	}).nearest().poll();
        
        if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())>3)) {
        	if (ctx.players.local().tile().distanceTo(Portal) < 50) {
        		if (Portal.inViewport()) {
        			Portal.interact("Use");
        		} else {
        			ctx.movement.step(Portal.tile().derive(1, 1));
        			Condition.wait(new Callable<Boolean>(){
    		            @Override
    		            public Boolean call() throws Exception {
    		                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    		            }
    		        }, 200, 10);
        		}
        	} else {
    	    	if (ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) > 6) {
    				if (walker.walkPathReverse(toRuinPath)) {
    					Condition.wait(new Callable<Boolean>(){
    			            @Override
    			            public Boolean call() throws Exception {
    			                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
    			            }
    			        }, 200, 10);
    				}
    	    	}
        	}
        } else if (ctx.players.local().tile().floor() == 0 && (stairsBottomTile.distanceTo(ctx.players.local())<=3)) {
        	Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return !ctx.players.local().inMotion();
	            }
	        }, 200, 10);
        	
        	if (stairsBottom.inViewport()) {
	        	if (stairsBottom.interact("Climb-up")) {
	    			Condition.wait(new Callable<Boolean>(){
	    	            @Override
	    	            public Boolean call() throws Exception {
	    	                return ctx.players.local().tile().floor() == 1;
	    	            }
	    	        }, 200, 10);
	    		}
        	} else {
        		ctx.camera.turnTo(stairsBottom);
        	}
    	} else if (ctx.players.local().tile().floor() == 1) {
    		if (stairsMiddle.interact("Climb-up")) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().floor() == 2;
    	            }
    	        }, 200, 10);
    		}
    	} else if (ctx.players.local().tile().floor() == 2 && (bankTile.distanceTo(ctx.players.local().tile())>6)) {
    		ctx.movement.step(bankTile);
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
	            }
	        }, 200, 10);
    	}
    }
    
    public void walkToRuin() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("WALK TO RUIN "+timeStamp);
    	
    	main.updateStatus("Walking to Ruin");
    	
        GameObject stairsMiddle = ctx.objects.select().id(16672).nearest().poll();
        GameObject stairsTop = ctx.objects.select().id(16673).nearest().poll();
        
        GameObject Ruin = ctx.objects.select().name("Mysterious Ruins").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Ruin) {
				return Ruin.valid();
			}
    	}).nearest().poll();
    	
        if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())>3)) {
        	ctx.movement.step(stairsTopTile.derive(1, 1)); //walk to stairs
        } else if (ctx.players.local().tile().floor() == 2 && (stairsTopTile.distanceTo(ctx.players.local())<=3)) {
        	Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return !ctx.players.local().inMotion();
	            }
	        }, 200, 10);
        	
        	if (stairsTop.inViewport()) {
	        	if (stairsTop.interact("Climb-down")) {
	        		Condition.wait(new Callable<Boolean>(){
	    	            @Override
	    	            public Boolean call() throws Exception {
	    	                return ctx.players.local().tile().floor() == 1;
	    	            }
	    	        }, 200, 10);
	        	}
        	} else {
        		ctx.camera.turnTo(stairsTop);
        	}
    	} else if (ctx.players.local().tile().floor() == 1) {
    		if (stairsMiddle.interact("Climb-down")) {
    			Condition.wait(new Callable<Boolean>(){
    	            @Override
    	            public Boolean call() throws Exception {
    	                return ctx.players.local().tile().floor() == 0;
    	            }
    	        }, 200, 10);
    		}
    	} else if (ctx.players.local().tile().floor() == 0) {
    		if (ctx.players.local().tile().distanceTo(ruinTile) > 6 && ctx.players.local().tile().distanceTo(altarTile) > 50) {
    			if (walker.walkPath(toRuinPath)) {
    				Condition.wait(new Callable<Boolean>(){
    		            @Override
    		            public Boolean call() throws Exception {
    		                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
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
    }
    
    public void walkToAltar() {
    	main.updateStatus("Walking to Altar");
    	
    	GameObject Altar = ctx.objects.select().name("Altar").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Altar) {
				return Altar.valid();
			}
    	}).nearest().poll();
    	
    	if (ctx.players.local().tile().distanceTo(Altar) > 5) {
    		ctx.movement.step(Altar.tile().derive(1, 1));
    		Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
	            }
	        }, 200, 10);
    	}
    }

    public void walkToPortal() {
    	main.updateStatus("Walking to Portal");
    	
    	GameObject Portal = ctx.objects.select().name("Portal").select(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject Portal) {
				return Portal.valid();
			}
    	}).nearest().poll();
    	
    	if (ctx.players.local().tile().distanceTo(Portal) > 5) {
    		ctx.movement.step(Portal.tile().derive(1, 1));
    	} else {
    		if (Portal.inViewport()) {
    			Portal.interact("Use");
    		} else {
    			ctx.movement.step(Portal.tile().derive(1, 1));
    			Condition.wait(new Callable<Boolean>(){
		            @Override
		            public Boolean call() throws Exception {
		                return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < Random.nextInt(1, 6);
		            }
		        }, 200, 10);
    		}
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
