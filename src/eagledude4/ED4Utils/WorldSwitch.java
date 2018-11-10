package eagledude4.ED4Utils;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;

public class WorldSwitch extends ClientAccessor {
	public WorldSwitch(ClientContext ctx) {
        super(ctx);
    }
	
	public void switchWorld() {
    	if (ctx.game.tab() != Tab.LOGOUT) {
    		ctx.game.tab(Tab.LOGOUT);
    		
    		Component LogoutWindow = ctx.widgets.component(182, 0);
    		Component WorldSwitcher = ctx.widgets.component(182, 5);
    		Component WorldList = ctx.widgets.component(69, 0);
    		Component WorldButton = ctx.widgets.component(69, 17);
    		if (LogoutWindow.visible()) {
    			WorldSwitcher.click();
    		} else if (WorldList.visible()) {
    			WorldButton.click();
    		}
    	}
    }
}
