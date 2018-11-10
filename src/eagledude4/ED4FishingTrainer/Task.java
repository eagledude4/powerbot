package eagledude4.ED4FishingTrainer;

import org.powerbot.script.rt4.ClientAccessor;
import STRepo.ST.api.STai.amplified_api.ClientContext;

public abstract class Task {

	public ClientContext ctx;
	
    public Task(ClientContext ctx) {
        this.ctx = ctx;
    }

    public abstract boolean activate();
    public abstract void execute();
}