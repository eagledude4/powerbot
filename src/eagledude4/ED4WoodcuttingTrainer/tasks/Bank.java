package eagledude4.ED4WoodcuttingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import eagledude4.ED4Utils.MouseCamera;
import eagledude4.ED4WoodcuttingTrainer.Task;
import eagledude4.ED4WoodcuttingTrainer.Main;

import java.util.concurrent.Callable;

public class Bank extends Task {
	Main main;
	private final MouseCamera mcam = new MouseCamera(ctx);
	int logID;
	
	public Bank(Main main, ClientContext ctx, int logID) {
        super(ctx);
        this.main = main;
        this.logID = logID;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
    	main.updateStatus("Depositing Logs");
		
        if(ctx.bank.opened()){
        	Condition.sleep(Random.nextInt(500,1000));
        	
        	final int startAmtInventory = ctx.inventory.select().count();
        	
        	ctx.bank.depositAllExcept(1359);
            Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                	return ctx.inventory.select().count() != startAmtInventory;
                }
            }, 250, 20);
            
            ctx.bank.close();
        } else {
            if(!ctx.bank.inViewport()) {
            	mcam.turnTo(ctx.bank.nearest());
            }
            if(ctx.bank.open()){
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
