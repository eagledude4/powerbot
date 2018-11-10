package eagledude4.ED4MiningTrainer;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4MiningTrainer.MConstants;
import eagledude4.ED4MiningTrainer.tasks.*;
import eagledude4.ED4Utils.DrawPaint;

@Script.Manifest(name="ED4 Mining Trainer", description="Trains Mining at various locations (OSRS)", properties="client=4; author=Eagledude4; topic=999;")

public class Main extends PollingScript<ClientContext> {
	Toolkit toolkit;

    public boolean hidePaint = false;
    
    List<Task> taskList = new ArrayList<Task>();
    
    public long startTime;
    
    public Image bg;
    public Image cursor;
    public Image oreIcon;
    
    public int startExp;
    public int startLevel;
    public int oreID = 0;
    public int oreMined = 0;
    public double expYield;
    
    public String Status = "Initializing";
    
    //public String userOptions[] = {"Yes", "No"};
    public String modeOptions[] = {"Auto", "Manual"};
    public String oreOptions[] = {"Iron", "Coal", "Mithril", "Adamantite"};
    public String guildOptions[] = {"Yes", "No"};
    public String bankOptions[] = {"Bank", "Drop"};
    
    public String autoMode;
    public String oreType;
    public String bankMode;
    public String guildMode;
    
    public GameObject Rock = null;
    public Player competitingPlayer = null;
    
    public void mineCopperTinTask() {
    	String oreOption[] = {"Copper", "Tin"};
    	String oreMode = ""+(String)JOptionPane.showInputDialog(null, "Choose an Ore", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, oreOption, oreOption[0]);
    	
    	if(oreMode.equals("Copper")){
    		System.out.println("Script will mine Copper "+Time(getRuntime()));
    		
    		oreID = 436;
    		expYield = 17.5;
    		
    		taskList.add(new Mine(this, ctx, MConstants.COPPER_ROCK_IDS));
    		
    		chooseBankMode(MConstants.COPPER_ROCK_IDS);
    	} else if(oreMode.equals("Tin")){
    		System.out.println("Script will mine Tin "+Time(getRuntime()));
    		
    		taskList.add(new Mine(this, ctx, MConstants.TIN_ROCK_IDS));
    		oreID = 438;
    		expYield = 17.5;
    		
    		chooseBankMode(MConstants.TIN_ROCK_IDS);
    	} else {
    		 ctx.controller.stop();
    	}
    }
    
    public void mineIronOreTask() {
    	System.out.println("Script will mine Iron "+Time(getRuntime()));
    	
    	oreID = 440;
		expYield = 35;
		
		taskList.add(new Mine(this, ctx, MConstants.IRON_ROCK_IDS));
    	//taskList.add(new MineCompetitive(this, ctx, MConstants.IRON_ROCK_IDS));
    	
		chooseBankMode(MConstants.IRON_ROCK_IDS);
    }
    
    public void mineCoalTask() {
    	System.out.println("Script will mine Coal "+Time(getRuntime()));
    	
    	oreID = 453;
		expYield = 50;
		
		taskList.add(new pickupOre(this, ctx, oreID));
    	taskList.add(new Mine(this, ctx, MConstants.COAL_ROCK_IDS));
    	//taskList.add(new MineCompetitive(this, ctx, MConstants.COAL_ROCK_IDS));
    	
		chooseBankMode(MConstants.COAL_ROCK_IDS);
    }
    
    public void mineGoldOreTask() {
    	System.out.println("Script will mine Gold "+Time(getRuntime()));
    	
    	oreID = 444;
		expYield = 65;
		
		taskList.add(new Mine(this, ctx, MConstants.GOLD_ROCK_IDS));
    	//taskList.add(new MineCompetitive(this, ctx, MConstants.GOLD_ROCK_IDS));
    	
		chooseBankMode(MConstants.GOLD_ROCK_IDS);
    }
    
    public void mineMithrilOreTask() {
    	System.out.println("Script will mine Mithril "+Time(getRuntime()));
    	
    	oreID = 447;
		expYield = 80;
		
		taskList.add(new pickupOre(this, ctx, oreID));
    	taskList.add(new Mine(this, ctx, MConstants.MITHRIL_ROCK_IDS));
    	//taskList.add(new MineCompetitive(this, ctx, MConstants.MITHRIL_ROCK_IDS));
    	
		chooseBankMode(MConstants.MITHRIL_ROCK_IDS);
    }
    
    public void mineAdamantOreTask() {
    	System.out.println("Script will mine Adamantite "+Time(getRuntime()));
    	
    	oreID = 449;
		expYield = 95;
		
		taskList.add(new Mine(this, ctx, MConstants.ADAMANT_ROCK_IDS));
    	//taskList.add(new MineCompetitive(this, ctx, MConstants.ADAMANT_ROCK_IDS));
    	
		chooseBankMode(MConstants.ADAMANT_ROCK_IDS);
    }
    
    public void chooseBankMode(int[] rockIds) {
    	String bankChoice = getBankMode();
    	
    	if (bankChoice != "") {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankChoice);
    	} else {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankOptions[0]);
    	}
    	
    	if(bankMode.equals("Drop")){
			System.out.println("Script will drop ore "+Time(getRuntime()));
    		
			taskList.add(new Drop(this, ctx));
    	} else if(bankMode.equals("Bank")){
    		System.out.println("Script will bank ore "+Time(getRuntime()));
    		
    		if (startLevel >= 60 && ((oreID == 440) || (oreID == 453) || (oreID == 447) || (oreID == 449))) {
    			chooseGuildMode(rockIds);
        	}
  
    		taskList.add(new Bank(this, ctx, oreID));
    	} else {
            ctx.controller.stop();
        }
    }

	public void chooseGuildMode(int[] rockIds) {
		String guildChoice = getGuildMode();
		
		if (guildChoice != "") {
			guildMode = ""+(String)JOptionPane.showInputDialog(null, "Use Mining Guild", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, guildOptions, guildChoice);
		} else {
			guildMode = ""+(String)JOptionPane.showInputDialog(null, "Use Mining Guild", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, guildOptions, guildOptions[0]);
		}
			
		if(guildMode.equals("Yes")){
    		System.out.println("Script will use mining guild "+Time(getRuntime()));
    		taskList.add(new WalkMiningGuild(this, ctx));
    	} else if(guildMode.equals("No")){
    		System.out.println("Script will use dwarven mine "+Time(getRuntime()));
    		taskList.add(new WalkDwarvenMine(this, ctx));
    	} else {
            ctx.controller.stop();
        }
    }
    
    
    public void chooseAutoMode() {
    	String autoChoice = getAutoMode();
    	
    	if (autoChoice != "") {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, autoChoice);
    	} else {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	}
    		
    	if(autoMode.equals("Auto")){
	        if (startLevel >= 1 && startLevel < 15) {
	        	mineCopperTinTask();
	        } else if (startLevel >= 15 && startLevel < 30) {
	        	mineIronOreTask();
	    	} else if (startLevel >= 30 && startLevel < 40) {
	    		mineCoalTask();
	    	} else if (startLevel >= 40 && startLevel < 55) {
	    		mineGoldOreTask();
	    	} else if (startLevel >= 55 && startLevel < 70) {
	    		mineMithrilOreTask();
	    	} else if (startLevel >= 70) {
	        	mineAdamantOreTask();
	        }
        } else if(autoMode.equals("Manual")){
        	chooseOreType();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void chooseOreType() {
    	String oreChoice = getOreType();
    	
    	if (oreChoice != "") {
    		oreType = ""+(String)JOptionPane.showInputDialog(null, "Choose Ore", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, oreOptions, oreChoice);
    	} else {
    		oreType = ""+(String)JOptionPane.showInputDialog(null, "Choose Ore", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, oreOptions, oreOptions[0]);
    	}
    	
    	if(oreType.equals("Iron")){
    		mineIronOreTask();
        } else if(oreType.equals("Coal")){
    		mineCoalTask();
        } else if(oreType.equals("Mithril")){
    		mineMithrilOreTask();
        } else if(oreType.equals("Adamantite")){
        	mineAdamantOreTask();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public String getAutoMode() {
    	JSONParser parser = new JSONParser();
    	String automode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4MiningTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            automode = (String) jsonObject.get("automode");
            return automode;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public String getOreType() {
    	JSONParser parser = new JSONParser();
    	String oretype = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4MiningTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            oretype = (String) jsonObject.get("oretype");
            return oretype;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public String getBankMode() {
    	JSONParser parser = new JSONParser();
    	String bankmode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4MiningTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            bankmode = (String) jsonObject.get("bankmode");
            return bankmode;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public String getGuildMode() {
    	JSONParser parser = new JSONParser();
    	String guildmode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4MiningTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            guildmode = (String) jsonObject.get("guildmode");
            return guildmode;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    @SuppressWarnings("unchecked")
	public void writeFile() {
    	JSONObject obj = new JSONObject();
        obj.put("automode", autoMode);
        obj.put("oretype", oreType);
        obj.put("bankmode", bankMode);
        obj.put("guildmode", guildMode);

        File path = getStorageDirectory();
        try (FileWriter file = new FileWriter(path+"\\ED4MiningTrainer_config.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.print(obj);
    }
    
    public void makeScreenshot(Frame argFrame, String fileName) {
        Rectangle rec = argFrame.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());

        try {
        	File path = getStorageDirectory();
        	//File path = new File("src\\" +fileName);
        	ImageIO.write(bufferedImage, "bmp", path);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void start(){
    	startExp = ctx.skills.experience(Constants.SKILLS_MINING);
    	startLevel = ctx.skills.level(Constants.SKILLS_MINING);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	//pickaxeMode = ""+(String)JOptionPane.showInputDialog(null, "Use Inventory pickaxe?", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);
    	
    	/*if(pickaxeMode.equals("Yes")){
        	usingInvPickaxe = true;
        } else {
        	usingInvPickaxe = false;
        }*/
    	
    	chooseAutoMode();
    	writeFile();
    	
    	if (ctx.game.tab() != Tab.INVENTORY) {
    		ctx.game.tab(Tab.INVENTORY);
    	}
        
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }
		
		 hidePaint = false;
		 String[] listenString = {"You manage"};
	     String oreIcon = oreID + ".gif";
	     new DrawPaint("ED4MiningTrainer", oreIcon, Constants.SKILLS_MINING, "MNG", startLevel, startExp, startTime, expYield, "Ore/Hour: ", "Ore Mined: ", listenString);
    }

    @Override
    public void poll() {
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
            	toolkit = Toolkit.getDefaultToolkit();
            	toolkit.beep();
            	//ctx.game.escapeClosing();
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }
        
        /*if ((ctx.players.local().animation() == -1) && (!ctx.players.local().inMotion())) {
        	updateStatus("Waiting");
        }*/
       
        /*if (!ctx.game.loggedIn()) {
        	ctx.controller.stop();
        }*/
        
        /*if (ctx.menu.items()[0].toString().contains("Use")) {
        	Item selectedItem = ctx.inventory.selectedItem();
        	selectedItem.click();
        }*/
    }
    
    public void updateStatus(String newStatus) {
    	if (!Status.equalsIgnoreCase(newStatus)) {
    		Status = newStatus;
    		System.out.println(""+newStatus+" - "+Time(getRuntime()));
    	};
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