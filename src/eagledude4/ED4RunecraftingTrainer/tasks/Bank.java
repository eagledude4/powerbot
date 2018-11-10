package eagledude4.ED4RunecraftingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4RunecraftingTrainer.Main;
import eagledude4.ED4RunecraftingTrainer.Task;

import java.awt.Point;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class Bank extends Task {
	Main main;
	
	public int runeID;
	public int runeEssenceID = 1436;
	
	public Bank(Main main, ClientContext ctx, int runeID) {
        super(ctx);
        this.main = main;
        this.runeID = runeID;
    }

	public void Deposit() {
		main.updateStatus("Depositing runes");
		
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
		main.updateStatus("Withdrawing rune essence");
		
		if (ctx.bank.currentTab() != 8) {
			ctx.bank.currentTab(8);
		}
		
		if (ctx.bank.select().id(runeEssenceID).isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ran out of essence");
			ctx.controller.stop();
		}
		
		for(Item essence: ctx.bank.select().id(runeEssenceID)) {
			if (essence.stackSize() > 0) {
				 int x = essence.centerPoint().x - 11 + Random.nextInt(0, 25);
		         int y = essence.centerPoint().y - 12 + Random.nextInt(0, 25);
		         ctx.input.click(new Point(x, y), false);
		         if (!ctx.menu.click(Menu.filter("Withdraw-All"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
		         	essence.interact("Withdraw-All"); //if not then interact() as normal, just incase
		         }
		         Condition.sleep(Random.nextInt(300,600));
			}
		}
		
    	ctx.bank.close();
	}
	
	@Override
    public boolean activate() {
		boolean needToWithdraw = ctx.inventory.select().id(runeEssenceID).count() == 0;
		boolean needToDeposit = ctx.inventory.select().id(runeID).count(true) > 0;
		
        return (needToWithdraw || needToDeposit) && (ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6);
    }

	@Override
    public void execute() {
		//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	//System.out.println("Bank execute "+timeStamp);
    	
		boolean needToWithdraw = ctx.inventory.select().id(runeEssenceID).count() == 0;
		boolean needToDeposit = ctx.inventory.select().id(runeID).count(true) > 0;
	
        if(ctx.bank.opened()){
        	if (needToDeposit) {
        		Deposit();
        	} else if (needToWithdraw) {
        		Withdraw();
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