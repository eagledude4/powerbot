package eagledude4.ED4FishingTrainer.tasks;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4FishingTrainer.Task;

public class LevelUp extends Task {
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
    	makeScreenshot(JFrame.getFrames()[0]);
    	
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
    
    public void makeScreenshot(Frame argFrame) {
        Rectangle rec = argFrame.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());

        try {
        	//File path = getStorageDirectory();
        	File path = new File("src\\screenshot");
        	ImageIO.write(bufferedImage, "bmp", path);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}