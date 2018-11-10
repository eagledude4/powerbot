package eagledude4.ED4GiantSlayer.tasks;

import java.awt.Point;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import eagledude4.ED4GiantSlayer.Main;
import eagledude4.ED4GiantSlayer.Task;

public class BuryBones extends Task {
	Main main;
	Pattern pattern = Pattern.compile("(.*bones)");
	
	public BuryBones(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }

    @Override
    public boolean activate() {
       return !ctx.players.local().interacting().valid() && 
   				!ctx.players.local().inCombat() && 
   				ctx.inventory.select().name(pattern).count() > 0;
    }

    @Override
    public void execute() {
    	main.updateStatus("Burying Bones");

    	for(Item bones: ctx.inventory.select().name(pattern)){
            if(ctx.controller.isStopping()){
                break;
            }
            
            int x = bones.centerPoint().x - 11 + Random.nextInt(0, 25);
            int y = bones.centerPoint().y - 12 + Random.nextInt(0, 25);
            int bonesCount = ctx.inventory.select().name(pattern).count();
            
            ctx.input.click(new Point(x, y), true);
            
            Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	            	return ctx.inventory.select().name(pattern).count() < bonesCount
	            	 && ctx.players.local().animation() == -1;
	            }
			 }, 200, 10);
            
            break;
        }
    }
}