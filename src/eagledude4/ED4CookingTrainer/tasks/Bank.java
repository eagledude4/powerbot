package eagledude4.ED4CookingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4CookingTrainer.Task;
import eagledude4.ED4CookingTrainer.Main;

import eagledude4.ED4Utils.MouseCamera;

import java.awt.Point;
import java.util.Comparator;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class Bank extends Task {
	private final MouseCamera mcam = new MouseCamera(ctx);
	
	Main main;
	int fishIds[];
    
	public Bank(Main main, ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.fishIds = fishIds;
        this.main = main;
    }
	
	public void Deposit() {
		String fishString = ctx.inventory.itemAt(0).name().toLowerCase();
		main.updateStatus("Depositing "+fishString);
		if(ctx.bank.depositInventory()){
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
		ItemQuery<Item> fishArr = ctx.bank.select().id(fishIds);
    	fishArr.sort(new Comparator<Item>() {
    	    @Override
    	    public int compare(Item o1, Item o2) {
    	    	return o1.stackSize() > o2.stackSize() ? 1 : -1;
    	    }
    	});
    	Item Fish = fishArr.select(new Filter<Item>() {
            @Override
            public boolean accept(Item Fish) {
                return Fish.stackSize() > 0;
            }
        }).first().poll();
    	
    	String fishString = Fish.name().toLowerCase();
    	main.updateStatus("Withdrawing "+fishString);
		
		if (Fish.valid()) {
			int x = Fish.centerPoint().x - 11 + Random.nextInt(0, 25);
	         int y = Fish.centerPoint().y - 12 + Random.nextInt(0, 25);
	         ctx.input.click(new Point(x, y), false);
	         if (!ctx.menu.click(Menu.filter("Withdraw-All"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
	         	Fish.interact("Withdraw-All"); //if not then interact() as normal, just incase
	         }
	         Condition.sleep(Random.nextInt(300,600));
		} else {
			JOptionPane.showMessageDialog(null, "Ran out of Fish");
			ctx.controller.stop();
		}
		
    	ctx.bank.close();
	}

    @Override
    public boolean activate() {
		
		boolean needToWithdraw = ctx.inventory.select().count() == 0;
		boolean needToDeposit = (ctx.inventory.select().id(fishIds).count() == 0) && (ctx.inventory.select().count() > 0);
		
        return (needToWithdraw || needToDeposit) && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
    	boolean needToWithdraw = ctx.inventory.select().count() == 0;
		boolean needToDeposit = (ctx.inventory.select().id(fishIds).count() == 0) && (ctx.inventory.select().count() > 0);
	
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
                mcam.turnTo(ctx.bank.nearest());
            }
        }
    }
}