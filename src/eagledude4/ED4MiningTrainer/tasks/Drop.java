package eagledude4.ED4MiningTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4MiningTrainer.Task;
import eagledude4.ED4MiningTrainer.Main;

import java.awt.Point;
import java.util.regex.Pattern;

public class Drop extends Task{
	Main main;
	
	Pattern pattern = Pattern.compile("(.*ore)|(Clay)|(Coal)");
	
    public Drop(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }

    public boolean activate() {
        return ctx.inventory.select().count()>27;
    }
    
    public void doShiftDrop() {
    	ctx.input.send("{VK_SHIFT down}");
    	
    	
    	for(Item log: ctx.inventory.select().name(pattern)){
            if(ctx.controller.isStopping()){
                break;
            }
            
            int x = log.centerPoint().x - 11 + Random.nextInt(0, 25);
            int y = log.centerPoint().y - 12 + Random.nextInt(0, 25);
            
            if (!ctx.input.click(new Point(x, y), true)) {
            	ctx.input.send("{VK_SHIFT up}"); //if failed clicking, releases shift key
            }
        }
    	
    	ctx.input.send("{VK_SHIFT up}");
    }
    
    public void doDrop() {
    	for(Item log: ctx.inventory.select().name(pattern)){
            if(ctx.controller.isStopping()){
                break;
            }
            
            interact(log, "Drop");
            Condition.sleep(Random.nextInt(300,600));
        }
    }
    
    public boolean interact(Item item, String choice) {
    	int x = item.centerPoint().x - 11 + Random.nextInt(0, 25);
        int y = item.centerPoint().y - 12 + Random.nextInt(0, 25);
        
        ctx.input.click(new Point(x, y), false);
        if (!ctx.menu.click(Menu.filter(choice))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
        	if (item.interact(choice)) { //if not then interact() as normal, just incase
        		return true;
        	}
        }
    	
        return false;
    }
    
    @Override
    public void execute() {
    	main.updateStatus("Dropping Ore");
    	
    	if (ctx.game.tab() != Tab.INVENTORY) {
    		ctx.game.tab(Tab.INVENTORY);
    	}
    	
    	if ((ctx.varpbits.varpbit(1055) & 131072) > 0) {
    		doShiftDrop();
    	} else {
    		doDrop();
    	}
    }
}