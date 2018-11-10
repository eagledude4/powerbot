package eagledude4.ED4GiantSlayer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4GiantSlayer.Task;
import eagledude4.ED4GiantSlayer.Main;

import java.awt.Point;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Bank extends Task {
	Main main;
	Pattern pattern = Pattern.compile("(Lobster)|(Swordfood)|(Salmon)|(Trout)");
    
	public Bank(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }
	
	public void Withdraw() {
		main.updateStatus("Withdrawing Food");
		
		if (ctx.bank.currentTab() != 8) {
			ctx.bank.currentTab(8);
		}
		
		ItemQuery<Item> foodArr = ctx.bank.select().name(pattern);
    	foodArr.sort(new Comparator<Item>() {
    	    @Override
    	    public int compare(Item o1, Item o2) {
    	    	return o1.stackSize() < o2.stackSize() ? 1 : -1;
    	    }
    	});
    	Item food = foodArr.select(new Filter<Item>() {
            @Override
            public boolean accept(Item food) {
                return food.stackSize() > 0;
            }
        }).first().poll();
		
		if (food.valid()) {
			int x = food.centerPoint().x - 11 + Random.nextInt(0, 25);
	         int y = food.centerPoint().y - 12 + Random.nextInt(0, 25);
	         ctx.input.click(new Point(x, y), false);
	         if (!ctx.menu.click(Menu.filter("Withdraw-5"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
	         	food.interact("Withdraw-5"); //if not then interact() as normal, just incase
	         }
	         Condition.sleep(Random.nextInt(300,600));
		} else {
			JOptionPane.showMessageDialog(null, "Ran out of food");
			ctx.controller.stop();
		}
		
    	ctx.bank.close();
	}

    @Override
    public boolean activate() {
		boolean needToWithdraw = ctx.inventory.select().name(pattern).count() == 0;
		
        return needToWithdraw & ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
        if(ctx.bank.opened()){
        	Withdraw();
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