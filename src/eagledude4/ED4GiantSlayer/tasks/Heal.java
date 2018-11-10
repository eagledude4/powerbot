package eagledude4.ED4GiantSlayer.tasks;

import java.awt.Point;
import java.util.regex.Pattern;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import eagledude4.ED4GiantSlayer.Main;
import eagledude4.ED4GiantSlayer.Task;

public class Heal extends Task {
	Main main;
	Pattern pattern = Pattern.compile("(Trout)|(Salmon)|(Lobster)|(Swordfish)");
	
	public Heal(Main main, ClientContext ctx) {
        super(ctx);
        this.main = main;
    }

    @Override
    public boolean activate() {
       return ctx.combat.healthPercent() < Random.nextInt(40,60);
    }

    @Override
    public void execute() {
    	main.updateStatus("Fighting Giant");

    	for(Item log: ctx.inventory.select().name(pattern)){
            if(ctx.controller.isStopping()){
                break;
            }
            
            int x = log.centerPoint().x - 11 + Random.nextInt(0, 25);
            int y = log.centerPoint().y - 12 + Random.nextInt(0, 25);
            
            ctx.input.click(new Point(x, y), true);
            break;
        }
    }
}