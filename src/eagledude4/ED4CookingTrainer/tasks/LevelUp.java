package eagledude4.ED4CookingTrainer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4CookingTrainer.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LevelUp extends Task {
    public String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

    public LevelUp(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
    	Component component1 = ctx.widgets.component(233, 2);
    	Component component2 = ctx.widgets.component(11, 3);
    	
        return component1.visible() || component2.visible();
    }

    @Override
    public void execute() {
    	Component component1 = ctx.widgets.component(233, 2);
    	Component component2 = ctx.widgets.component(11, 3);
    	if (component1.visible()) {
    		component1.interact("Continue");
    		Condition.sleep(Random.nextInt(1000,3000));
    	} else if (component2.visible()) {
    		component2.interact("Continue");
    		Condition.sleep(Random.nextInt(1000,3000));
    	}
		
		if (ctx.game.tab() != Tab.STATS) {
			if (Random.nextInt(0,3) == 3) {
				ctx.game.tab(Tab.STATS);
	    		Condition.sleep(Random.nextInt(100,500));
	    		ctx.widgets.component(320, 22).hover();
	    		Condition.sleep(Random.nextInt(1000,3000));
	    		ctx.game.tab(Tab.INVENTORY);
			}
		}
    }
}