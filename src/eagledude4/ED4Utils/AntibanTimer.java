package eagledude4.ED4Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.PlayerQuery;
import org.powerbot.script.rt4.Game.Tab;

public class AntibanTimer extends ClientAccessor {
    public AntibanTimer(ClientContext ctx) {
    	super(ctx);
	}
    
    public void execute(int skill) {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Performing random anti-ban method "+timeStamp);
    	
    	int randomX = Random.nextInt(0,800);
		int randomY = Random.nextInt(0,600);
		
		PlayerQuery<Player> nearPlayers = ctx.players.within(10);
		if ((Random.nextInt(0,50) == 20) && !nearPlayers.isEmpty()) {
			if (Random.nextInt(0,1) == 0) {
				for(Player player :  nearPlayers){
					if (Random.nextInt(0,6) == 5) {
						//System.out.println(player.name());
						player.click(false);
			    		Condition.sleep(Random.nextInt(1000,3000));
			    		ctx.input.move(randomX,randomY);
			    		break;
					}
				}
			} else if (Random.nextInt(0,1) == 1) {
				for(Player player :  nearPlayers){
					if (Random.nextInt(0,6) == 5) {
						//System.out.println(player.name());
						player.hover();
			    		Condition.sleep(Random.nextInt(1000,3000));
			    		break;
					}
				}
			}
		}
    	
		if (Random.nextInt(0,4) == 0) {
			ctx.camera.pitch(Random.nextInt(0,100));
		} else if (Random.nextInt(0,4) == 1) {
	    	ctx.camera.angle(Random.nextInt(0, 359));
		} else if (Random.nextInt(0,4) == 2) {
			ctx.input.move(randomX,randomY);
		 } else if (Random.nextInt(0,4) == 3) {
			ctx.input.defocus();
		}
   
		if (Random.nextInt(0,20) == 19) {
			Component component = null;
			
			 switch (skill) {
	            case Constants.SKILLS_MINING: component = ctx.widgets.component(320, 17);
	            	break;
	            case Constants.SKILLS_WOODCUTTING: component = ctx.widgets.component(320, 22);
            		break;
	            case Constants.SKILLS_SMITHING: component = ctx.widgets.component(320, 18);
        			break;
	            case Constants.SKILLS_RUNECRAFTING: component = ctx.widgets.component(320, 7);
    				break;
	            case Constants.SKILLS_FISHING: component = ctx.widgets.component(320, 19);
					break;
	            case Constants.SKILLS_COOKING: component = ctx.widgets.component(320, 20);
					break;
			}
			
	    	ctx.game.tab(Tab.STATS);
			Condition.sleep(Random.nextInt(100,500));
			component.hover();
			Condition.sleep(Random.nextInt(1000,3000));
			ctx.game.tab(Tab.INVENTORY);
		}
    }
}