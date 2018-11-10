package eagledude4.ED4WoodcuttingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4WoodcuttingTrainer.Main;
import eagledude4.ED4WoodcuttingTrainer.Task;

import java.awt.Point;
import java.util.regex.Pattern;

public class Drop extends Task{
	Main main;
	
	Pattern pattern = Pattern.compile("(Logs)|(.*logs)");
	
	public Drop(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }

    @Override
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
            
            int x = log.centerPoint().x - 11 + Random.nextInt(0, 25);
            int y = log.centerPoint().y - 12 + Random.nextInt(0, 25);
            
            ctx.input.click(new Point(x, y), false);
            if (!ctx.menu.click(Menu.filter("Drop"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
            	log.interact("Drop"); //if not then interact() as normal, just incase
            }
            Condition.sleep(Random.nextInt(300,600));
        }
    }
    
    @Override
    public void execute() {
    	main.updateStatus("Dropping Logs");
    	
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