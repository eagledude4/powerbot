package eagledude4.ED4FishingTrainer.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;

//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;

import eagledude4.ED4FishingTrainer.Main;
import eagledude4.ED4FishingTrainer.Task;

public class Bank extends Task {
	Main main;
	int fishIds[];
	int keepItems[];
    
	public Bank(Main main, ClientContext ctx, int[] fishIds, int[] keepItems) {
        super(ctx);
        this.main = main;
        this.fishIds = fishIds;
        this.keepItems = keepItems;
    }

	private boolean reachable(Locatable Bank) {
        int a = 1;
        Tile t1 = new Tile(Bank.tile().x() + a, Bank.tile().y(), Bank.tile().floor());
        Tile t2 = new Tile(Bank.tile().x() - a, Bank.tile().y(), Bank.tile().floor());
        Tile t3 = new Tile(Bank.tile().x(), Bank.tile().y() + a, Bank.tile().floor());
        Tile t4 = new Tile(Bank.tile().x(), Bank.tile().y() - a, Bank.tile().floor());

        return (t1.matrix(ctx).reachable() || t2.matrix(ctx).reachable() || t3.matrix(ctx).reachable() || t4.matrix(ctx).reachable());
    }
	
	@Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 && 
        	   ctx.bank.nearest().tile().distanceTo(ctx.players.local())<=3;
    }

    @Override
    public void execute() {
    	//System.out.println("BANK EXECUTE");
    	
    	if (ctx.camera.find(ctx.bank.nearest().tile().matrix(ctx))) {
	        if(ctx.bank.opened()){
	        	//System.out.println("BANK OPENED");
	        	Condition.sleep(Random.nextInt(500,1000));
	  
	        	final int startAmtInventory = ctx.inventory.select().count();
	        	ctx.bank.depositAllExcept(keepItems);
	            Condition.wait(new Callable<Boolean>(){
	                @Override
	                public Boolean call() throws Exception {
	                	return ctx.inventory.select().count() != startAmtInventory;
	                }
	            }, 250, 20);
	            ctx.bank.close();
	        } else {
	        	 //System.out.println("BANK NOT OPENED");
	        	 main.updateStatus("Opening Bank");
	        	 if(ctx.bank.open()){
	        		main.updateStatus("Depositing Fish");
                    Condition.wait(new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
	        	}
	        }
	    }
    }
}