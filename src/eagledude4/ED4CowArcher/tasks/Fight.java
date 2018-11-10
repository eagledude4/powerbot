package eagledude4.ED4CowArcher.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;

import eagledude4.ED4CowArcher.Main;
import eagledude4.ED4CowArcher.Task;

public class Fight extends Task {
	Main main;
	
	int arrowID;
	int cowIds[];
	
    public Fight(Main main, ClientContext ctx, int[] cowIds, int arrowID) {
        super(ctx);
        this.main = main;
        this.cowIds = cowIds;
        this.arrowID = arrowID;
    }

    @Override
    public boolean activate() {
    	GroundItem groundLoot = ctx.groundItems.select().id(arrowID).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundLoot) {
                return groundLoot.tile().matrix(ctx).reachable() && groundLoot.tile().distanceTo(ctx.players.local()) < 10;
            }
        }).nearest().poll();
    	
    	return !groundLoot.valid() && ctx.players.local().animation()==-1 && !ctx.players.local().interacting().valid();
    }

    @Override
    public void execute() {
    	main.updateStatus("Fighting Cow");
    	
    	Npc Cow = ctx.npcs.select().id(cowIds).select(new Filter<Npc>() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean accept(Npc npc) {
				return !npc.interacting().valid() && npc.tile().matrix(ctx).reachable() && npc.health() > 0 && (npc.tile().distanceTo(ctx.players.local().tile()) < 10);
			}
    	}).nearest().poll();
    	
    	if (!Cow.inViewport()) {
			ctx.camera.turnTo(Cow);
			Condition.sleep(Random.nextInt(500, 1000));
		} else {
			if (Cow.inMotion()) {
				if (Cow.interact(false,"Attack", Cow.name())) {
					//new AntibanTimer(ctx);
					//ctx.camera.turnTo(ctx.players.local().interacting());
					Condition.sleep(Random.nextInt(500, 1000));
				}
			} else {
				ctx.camera.turnTo(Cow);
				if (Cow.interact("Attack", Cow.name())) {
					//new AntibanTimer(ctx);
					//ctx.camera.turnTo(ctx.players.local().interacting());
					Condition.sleep(Random.nextInt(500, 1000));
				}
			}
		}
    }
}