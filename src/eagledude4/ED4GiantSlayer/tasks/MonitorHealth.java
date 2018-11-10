package eagledude4.ED4GiantSlayer.tasks;

import org.powerbot.script.rt4.ClientContext;
import eagledude4.ED4GiantSlayer.Task;

public class MonitorHealth extends Task {
	int lastHealth = ctx.combat.health();
	
	public MonitorHealth(ClientContext ctx, int lastHealth) {
        super(ctx);
        this.lastHealth = lastHealth;
    }

    @Override
    public boolean activate() {
        return ctx.combat.health() < lastHealth;
    }

    @Override
    public void execute() {
    	int currentHealth = ctx.combat.health();
    	System.out.println("Health is now " +currentHealth);
    	lastHealth = ctx.combat.health();
    }
}