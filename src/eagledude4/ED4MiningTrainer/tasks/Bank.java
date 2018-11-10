package eagledude4.ED4MiningTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4MiningTrainer.Main;
import eagledude4.ED4MiningTrainer.Task;

import java.util.concurrent.Callable;

public class Bank extends Task {
	Main main;
	
	public int oreID;
	
	public Bank(Main main, ClientContext ctx, int oreID) {
        super(ctx);
        this.main = main;
        this.oreID = oreID;
    }


    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() > 27) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6);
    }

    @Override
    public void execute() {
    	final int inventCount = ctx.inventory.select().count();
		
		main.updateStatus("Depositing Ore");
		
        if(ctx.bank.opened()){
        	Condition.sleep(Random.nextInt(500,1000));
 
        	if (ctx.bank.currentTab() != 6) {
    			ctx.bank.currentTab(6);
    		}
        	
            if(ctx.bank.depositInventory()) {
                Condition.wait(new Callable<Boolean>(){
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() != inventCount;
                    }
                }, 250, 20);
                ctx.bank.close();
            }
        } else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}