package eagledude4.ED4Utils;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
//import org.powerbot.script.rt4.Constants;
//import org.powerbot.script.rt4.Skills;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Menu;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;


public class Utils extends PollingScript<ClientContext> {
    @Override
    public void poll() {
    }
    
    public Utils(ClientContext ctx) {
    	ctx.dispatcher.add(this);
    }
    
    public void setGoal(String skill, String levelGoal) {
    	Component xpButton = ctx.widgets.widget(160).component(1);
    	xpButton.click(false);
    	if (ctx.menu.click(Menu.filter("Show"))) {
    		xpButton.interact("Show");
    	}
		Condition.sleep(Random.nextInt(500, 1000));
		xpButton.interact("Setup");
		
		Component Window = ctx.widgets.widget(137).component(2).component(0);
		
		Component chosenCounter = ctx.widgets.widget(137).component(9).component(4);
		Component counterButton = ctx.widgets.widget(137).component(9).component(0);

		Component listItem = ctx.widgets.widget(137).component(16).component(0);
		Component listItemText = ctx.widgets.widget(137).component(16).component(26);
		Component counterListOption = ctx.widgets.widget(137).component(53).component(32);

		if (skill.equals("Attack")) {
			
			listItem = ctx.widgets.widget(137).component(16).component(0);
			listItemText = ctx.widgets.widget(137).component(16).component(26);
			//counterListOption = ctx.widgets.widget(137).component(53).component(2);
		} else if (skill.equals("Strength")) {
			listItem = ctx.widgets.widget(137).component(16).component(1);
			listItemText = ctx.widgets.widget(137).component(16).component(29);
			//counterListOption = ctx.widgets.widget(137).component(53).component(3);
		} else if (skill.equals("Defense")) {
			listItem = ctx.widgets.widget(137).component(16).component(4);
			listItemText = ctx.widgets.widget(137).component(16).component(32);
			//counterListOption = ctx.widgets.widget(137).component(53).component(6);
		}

		Component goalBox = ctx.widgets.widget(137).component(32).component(1);
		Component goalCheckbox = ctx.widgets.widget(137).component(32).component(0);
		Component startPointSet = ctx.widgets.widget(137).component(38);
		Component endPointSet = ctx.widgets.widget(137).component(42);
		Component saveButton = ctx.widgets.widget(137).component(45).component(9);
		Component closeButton = ctx.widgets.widget(137).component(2).component(11);
		Component chatBox = ctx.widgets.widget(162).component(44);
		
		Condition.wait(new Callable<Boolean>(){
	        @Override
	        public Boolean call() throws Exception {
	            return Window.visible();
	        }
	    }, 1000, 5);
		
		if (!chosenCounter.text().equals(skill)) {
			counterButton.click();
			Condition.sleep(Random.nextInt(500, 1000));
			counterListOption.click();
		}
		
		//Check if goal not selected
		if (listItemText.text().contains("No tracker or goal set")) {
			listItem.interact("Configure");
			Condition.wait(new Callable<Boolean>(){
		        @Override
		        public Boolean call() throws Exception {
		            return goalBox.visible();
		        }
		    }, 1000, 5);
			goalBox.click();
			Condition.wait(new Callable<Boolean>(){
		        @Override
		        public Boolean call() throws Exception {
		            return goalCheckbox.textureId() == 699;
		        }
		    }, 1000, 5);
			startPointSet.interact("Set to current XP");
			Condition.sleep(Random.nextInt(500, 1000));
			endPointSet.click(false);
			ctx.menu.click(Menu.filter("Set to level"));
			Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return chatBox.visible();
	            }
	        }, 1000, 5);
			ctx.input.send(levelGoal);
			Condition.sleep(Random.nextInt(500, 1000));
			ctx.input.send("{VK_ENTER down}");
			Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return !chatBox.visible();
	            }
	        }, 1000, 5);
			saveButton.click();
			Condition.sleep(Random.nextInt(500, 1000));
			closeButton.click();
		} else {
			closeButton.click();
		}
    }
    
    public String updateStatus(String oldStatus, String newStatus) {
    	if (!oldStatus.equalsIgnoreCase(newStatus)) {
    		System.out.println(newStatus);
    		newStatus = ""+newStatus+" - "+Time(getRuntime());
    	};
    	return newStatus;
    }
    
    public Image getImage(String name) {
        File file = new File(getStorageDirectory()+name);
        try {
            if (!file.exists()) { //dev only!
            	file = new File("src\\"+name);
            	//file = new File("src\\"+className+"\\"+name);
            	//System.out.println("file: "+file);
            	return ImageIO.read(file);
            } else {
                return ImageIO.read(file); //peeps on the SDN
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
  
  //Convert long type millisecond timer into a String showing hh:mm:ss, helper class.
    public String Time(long i) {
 	   DecimalFormat nf = new DecimalFormat("00");
 	   long millis = i;
 	   long hours = millis / (1000 * 60 * 60);
 	   millis -= hours * (1000 * 60 * 60);
 	   long minutes = millis / (1000 * 60);
 	   millis -= minutes * (1000 * 60);
 	   long seconds = millis / 1000;
 	   return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }
    
    //Method which gets time till next level.
    public String getTimeToNextLevel(final int expLeft, final int xpPerHour) {
        //If not earning exp then return
        if (xpPerHour < 1) {
     	   return "No EXP gained yet.";
        }

        //If gaining exp then measure approximately it will take to level.
        return Time((long)(expLeft * 3600000D / xpPerHour));
    }
    
    public int[] combine(int[] a, int[] b){
        int length = a.length + b.length;
        int[] result = new int[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}