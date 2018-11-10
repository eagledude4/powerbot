package eagledude4.ED4SmithingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4SmithingTrainer.Task;
import eagledude4.ED4SmithingTrainer.Main;

import java.awt.Point;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class Bank extends Task {
	Main main;
	
	public int barID;
    
	public Bank(Main main, ClientContext ctx, int barID) {
        super(ctx);
        this.main = main;
        this.barID = barID;
    }
	
	public void Deposit() {
		main.updateStatus("Depositing Smithed Items");
		
		if( ctx.bank.depositAllExcept(2347)) {
            final int inventCount = ctx.inventory.select().count();
            Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != inventCount;
                }
            }, 250, 20);
        }
	}
	
	public void Withdraw() {
		main.updateStatus("Getting Bars");
		
		if (ctx.bank.currentTab() != 0) {
			ctx.bank.currentTab(0);
		}
		
		Item bar = ctx.bank.select().id(barID).poll();

		if (bar.stackSize() >= barsToWithdraw(main.currentLevel)) {
			ctx.bank.withdraw(bar.id(), barsToWithdraw(main.currentLevel));
			Condition.sleep(Random.nextInt(300,600));
		} else {
			JOptionPane.showMessageDialog(null, "Ran out of bars");
			ctx.controller.stop();
		}
		
    	ctx.bank.close();
	}
	
	public int barsToWithdraw(int smithingLevel) {
		if (smithingLevel >= 1 && smithingLevel < 5) {
			return 25;
		} else if (smithingLevel >= 5 && smithingLevel < 9) {
			return 26;
		} else if (smithingLevel >= 9 && smithingLevel < 15) {
			return 27;
		}
		
		if (smithingLevel >= 15 && smithingLevel < 20) {
			return 25;
    	} else if (smithingLevel >= 20 && smithingLevel < 24) {
    		return 26;
    	} else if (smithingLevel >= 24 && smithingLevel < 30) {
    		return 27;
    	}
		
		if (smithingLevel >= 30 && smithingLevel < 35) {
			return 25;
    	} else if (smithingLevel >= 35 && smithingLevel < 39) {
    		return 26;
    	} else if (smithingLevel >= 39 && smithingLevel < 50) {
    		return 27;
    	}
		
		if (smithingLevel >= 30 && smithingLevel < 35) {
			return 25;
    	} else if (smithingLevel >= 35 && smithingLevel < 39) {
    		return 26;
    	} else if (smithingLevel >= 39 && smithingLevel < 50) {
    		return 27;
    	}
		
		if (smithingLevel >= 50 && smithingLevel < 55) {
			return 25;
    	} else if (smithingLevel >= 55 && smithingLevel < 59) {
    		return 26;
    	} else if (smithingLevel >= 59 && smithingLevel < 70) {
    		return 27;
    	}
		
		return 1;
	}

    @Override
    public boolean activate() {
		boolean needToWithdraw = ctx.inventory.select().count() == 1;
		boolean needToDeposit = ctx.inventory.select().id(barID).count() == 0;
		
        return (needToWithdraw || needToDeposit) && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
		boolean needToWithdraw = ctx.inventory.select().count() == 1;
		boolean needToDeposit = ctx.inventory.select().id(barID).count() == 0;
	
        if(ctx.bank.opened()){
        	if (needToWithdraw) {
        		Withdraw();
        	} else if (needToDeposit) {
        		Deposit();
        	} else {
        		ctx.controller.stop();
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