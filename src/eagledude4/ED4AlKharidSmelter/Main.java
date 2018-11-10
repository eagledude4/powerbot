package eagledude4.ED4AlKharidSmelter;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;
//import org.powerbot.script.rt6.GeItem;

//import eagledude4.ED4FishingTrainer.AntibanTimer;
import eagledude4.ED4AlKharidSmelter.MConstants;
import eagledude4.ED4AlKharidSmelter.tasks.Walk;
import eagledude4.ED4Utils.DrawPaint;
import eagledude4.ED4AlKharidSmelter.tasks.Bank;
import eagledude4.ED4AlKharidSmelter.tasks.Smelt;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

@Script.Manifest(name="ED4 Al-Kharid Smelter", description="Trains smithing by smelting bars at Al-Kharid (OSRS)", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> {
	Toolkit toolkit;
	
	public String Status = "Initializing";
    //public boolean usingInvHatchet = false;
	
    boolean hidePaint = true;
    
	public List<Task> taskList = new ArrayList<Task>();
	public List<Integer> oreIDList;
    
	public int startExp = 0;
    public int startLevel = 0;
    public int currentLevel = 0;
    public int barID = 0;
    public int barPrice;
    public int barsMade = 0;
    public int[] oreIds;
    
    public double expYield;
    
    public long startTime;
    
    public String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    
    public void smeltBronze() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Bronze bars "+timeStamp);
		oreIDList = Arrays.asList(436, 438);
		expYield = 6.2;
		barID = 2349;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void smeltIron() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Iron bars "+timeStamp);
		oreIDList = Arrays.asList(440);
		expYield = 12.5;
		barID = 2351;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void smeltSteel() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Steel bars "+timeStamp);
		oreIDList = Arrays.asList(440, 453);
		expYield = 17.5;
		barID = 2353;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void smeltGold() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Gold bars "+timeStamp);
		oreIDList = Arrays.asList(444);
		expYield = 22.5;
		barID = 2357;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void smeltMithril() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Mithril bars "+timeStamp);
		oreIDList = Arrays.asList(447, 453);
		expYield = 30;
		barID = 2359;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void smeltAdamantite() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println("Script will smelt Adamantite bars "+timeStamp);
		oreIDList = Arrays.asList(449, 453);
		expYield = 37.5;
		barID = 2361;
		//barPrice = GeItem.price(barID);
    	oreIds = oreIDList.stream().mapToInt(i -> i).toArray();
    	
    	taskList.add(new Smelt(this, ctx, MConstants.FURNACE_TILE, oreIds, barID));
    }
    
    public void chooseBarType() {
    	String barOptions[] = {"Bronze", "Iron", "Steel", "Gold", "Mithril", "Adamantite"};
    	String barType = ""+(String)JOptionPane.showInputDialog(null, "Choose Bar", "Al-Kharid Smelter", JOptionPane.PLAIN_MESSAGE, null, barOptions, barOptions[0]);
    	
    	if(barType.equals("Bronze")){
    		smeltBronze();
        } else if(barType.equals("Iron")){
        	smeltIron();
        } else if(barType.equals("Steel")){
        	smeltSteel();
        } else if(barType.equals("Gold")){
        	smeltGold();
        } else if(barType.equals("Mithril")){
        	smeltMithril();
        } else if(barType.equals("Adamantite")){
        	smeltAdamantite();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void openDonatePage() {
    	try {
			Desktop.getDesktop().browse(new URL("https://www.paypal.me/JHachey").toURI());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    @Override
    public void start(){
    	//openDonatePage();
    	
    	startExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
    	startLevel = ctx.skills.level(Constants.SKILLS_SMITHING);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	String modeOptions[] = {"Auto", "Manual"};
    	String autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "AlKharid Smelter", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	
    	if(autoMode.equals("Auto")){
	        if (startLevel < 15) {
	        	smeltBronze();
	        } else if (startLevel >= 15 && startLevel < 30) {
	        	smeltIron();
	    	} else if (startLevel >= 30 && startLevel < 40) {
	    		smeltSteel();
	    	} else if (startLevel >= 40 && startLevel < 50) {
	    		smeltGold();
	    	} else if (startLevel >= 50 && startLevel < 70) {
	    		smeltMithril();
	    	} else if (startLevel > 70 && startLevel < 90) {
	    		smeltAdamantite();
	        }
        } else if(autoMode.equals("Manual")){
        	chooseBarType();
        } else {
        	ctx.controller.stop();
        }
    	
    	taskList.add(new Walk(this, ctx, MConstants.FURNACE_TILE, barID, oreIds));
    	taskList.add(new Bank(this, ctx, oreIds, barID));

        ctx.game.tab(Tab.INVENTORY);
        
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }
        
        String barIcon = barID + ".gif";
        
        hidePaint = false;
        String[] listenString = {"You retrieve"};
        new DrawPaint("ED4ALKharidSmelter", barIcon, Constants.SKILLS_SMITHING, "SMT", startLevel, startExp, startTime, expYield, "Bars/Hour: ", "Bars Smelted: ", listenString);
    }

    @Override
    public void poll() {
    	for(Task task : taskList){
        	if(ctx.controller.isStopping()){
            	//AntibanTimer.cleanup();
            	toolkit = Toolkit.getDefaultToolkit();
            	toolkit.beep();
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }
    	
        /*if (!ctx.game.loggedIn()) {
        	ctx.controller.stop();
        }*/
    	
    	Component component = ctx.widgets.component(229, 0);
    	if (component.visible()) {
    		ctx.controller.stop();
    	};
        
        Component newLevel = ctx.widgets.component(233, 0);
		Component continueButton = ctx.widgets.component(233, 2);
    	if (newLevel.visible()) {
    		continueButton.interact("Continue");
    	};
    }
    
    public void updateStatus(String newStatus) {
    	if (!Status.equalsIgnoreCase(newStatus)) {
    		Status = newStatus;
    		System.out.println(""+newStatus+" - "+Time(getRuntime()));
    	};
    }
    
    public void messaged(MessageEvent event) {
		if (event.text().contains("You retrieve")) {
			barsMade++;
		}
		if (event.text().contains("Congratulations")) {
			Component newLevel = ctx.widgets.component(233, 0);
			Component continueButton = ctx.widgets.component(233, 2);
        	if (newLevel.visible()) {
        		continueButton.interact("Continue");
        	};
		}
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
}