package eagledude4.ED4WoodcuttingTrainer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4Utils.DrawPaint;
import eagledude4.ED4Utils.JSON;
import eagledude4.ED4Utils.Utils;
import eagledude4.ED4WoodcuttingTrainer.MConstants;
import eagledude4.ED4WoodcuttingTrainer.tasks.Walk;
import eagledude4.ED4WoodcuttingTrainer.tasks.WalkWillows;
import eagledude4.ED4WoodcuttingTrainer.tasks.Bank;
import eagledude4.ED4WoodcuttingTrainer.tasks.Drop;
import eagledude4.ED4WoodcuttingTrainer.tasks.Chop;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

@Script.Manifest(name="ED4 Woodcutting Trainer", description="Trains Woodcutting at Lumbridge (OSRS)", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> {
	private final Utils utils = new Utils(ctx);
	private final JSON JSON = new JSON(ctx);
	Toolkit toolkit;
	
	public String Status = "Initializing";
    
	//public boolean usingInvHatchet = false;
    public boolean hidePaint = true;
    
	List<Task> taskList = new ArrayList<Task>();
    
	public int startExp = 0;
    public int startLevel = 0;
    public int logsChopped = 0;
    public int logID = 0;
    public int[] Trees;
    public double expYield;
    
    public Tile chopStartTile;
    
    public String modeOptions[] = {"Auto", "Manual"};
    public String logOptions[] = {"Normal", "Oak", "Willow", "Yew"};
    public String bankOptions[] = {"Bank", "Drop"};
    
    public String autoMode;
    public String logType;
    public String bankMode;
    
    public long startTime;
    
    public void chopNormalLogs() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	Trees = MConstants.NORMAL_TREES;
    	chopStartTile = new Tile(3194, 3216, 0);
    	
    	taskList.add(new Walk(this, ctx));
    	
    	logID = 1511;
    	//expYield = 25;
    }
    
    public void chopOakLogs() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	Trees = utils.combine(MConstants.OAK_TREES,MConstants.NORMAL_TREES);
    	chopStartTile = new Tile(3194, 3216, 0);
    	
    	taskList.add(new Walk(this, ctx));
        
    	logID = 1521;
    	//expYield = 37.5;
    }
    
    public void chopWillowLogs() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	Trees = MConstants.WILLOW_TREES;
    	chopStartTile = new Tile(3172, 3266, 0);
    	
    	taskList.add(new WalkWillows(this, ctx));
    	
    	logID = 1519;
    	//expYield = 67.5;
    }
    
    public void chopYewLogs() {
    	//String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	Trees = utils.combine(MConstants.YEW_TREES,MConstants.OAK_TREES); //no willows here
    	Trees = utils.combine(Trees,MConstants.NORMAL_TREES);
    	chopStartTile = new Tile(3176, 3222, 0);
    	
    	taskList.add(new Walk(this, ctx));
		
    	logID = 1515;
    	//expYield = 175;
    }
    
    public void chooseBankMode() {
    	String bankChoice = getBankMode();
    	
    	if (bankChoice != "") {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankChoice);
    	} else {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankOptions[0]);
    	}
    	
    	if(bankMode.equals("Drop")){
			System.out.println("Script will drop logs "+utils.Time(getRuntime()));
    		
			taskList.add(new Drop(this, ctx));
    	} else if(bankMode.equals("Bank")){
    		System.out.println("Script will bank logs "+utils.Time(getRuntime()));
    		
    		taskList.add(new Bank(this, ctx, logID));
    	} else {
            ctx.controller.stop();
        }
    }

    public void chooseAutoMode() {
    	String autoChoice = getAutoMode();
    	
    	if (autoChoice != "") {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, autoChoice);
    	} else {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	}
    		
    	if(autoMode.equals("Auto")){
	        if (startLevel < 15) {
	        	chopNormalLogs();
	        } else if (startLevel >= 15 && startLevel < 30) {
	        	chopOakLogs();
	        } else if (startLevel >= 30 && startLevel < 60) {
	        	chopWillowLogs();
	        } else if (startLevel >= 60) {
	        	chopYewLogs();
	    	}
        } else if(autoMode.equals("Manual")){
        	chooseLogType();
        } else {
        	ctx.controller.stop();
        }
    	
    	chooseBankMode();
    }
    
    public void chooseLogType() {
    	String logChoice = getlogType();
    	
    	if (logChoice != "") {
    		logType = ""+(String)JOptionPane.showInputDialog(null, "Choose Ore", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, logOptions, logChoice);
    	} else {
    		logType = ""+(String)JOptionPane.showInputDialog(null, "Choose Ore", "Woodcutting Trainer", JOptionPane.PLAIN_MESSAGE, null, logOptions, logOptions[0]);
    	}
    	
    	if(logType.equals("Normal")){
    		chopNormalLogs();
        } else if(logType.equals("Oak")){
        	chopOakLogs();
        } else if(logType.equals("Willow")){
        	chopWillowLogs();
        } else if(logType.equals("Yew")){
        	chopYewLogs();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public String getAutoMode() {
    	JSONParser parser = new JSONParser();
    	String automode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4WoodcuttingTrainer_config.json"));

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
    
    public String getlogType() {
    	JSONParser parser = new JSONParser();
    	String logType = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4WoodcuttingTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            logType = (String) jsonObject.get("logType");
            return logType;
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
            Object obj = parser.parse(new FileReader(path+"\\ED4WoodcuttingTrainer_config.json"));

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

    @SuppressWarnings("unchecked")
	public void writeFile() {
    	JSONObject obj = new JSONObject();
        obj.put("automode", autoMode);
        obj.put("logType", logType);
        obj.put("bankmode", bankMode);

        File path = getStorageDirectory();
        try (FileWriter file = new FileWriter(path+"\\ED4WoodcuttingTrainer_config.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.print(obj);
    }

    @Override
    public void start(){
    	startExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
    	startLevel = ctx.skills.level(Constants.SKILLS_WOODCUTTING);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	writeFile();
    	chooseAutoMode();
    	
    	taskList.add(new Chop(this, ctx, Trees, chopStartTile));
    	
    	/*if(hatchetMode.equals("Yes")){
        	usingInvHatchet = true;
        } else {
        	usingInvHatchet = false;
        }*/
    	
    	ctx.game.tab(Tab.INVENTORY);
        
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }
        
        hidePaint = false;
		String[] listenString = {"You get some"};
	    String logIcon = logID + ".gif";
	    new DrawPaint("ED4WoodcuttingTrainer", logIcon, Constants.SKILLS_WOODCUTTING, "WDC", startLevel, startExp, startTime, expYield, "", "Logs Chopped: ", listenString);
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
            
            /*if (!ctx.game.loggedIn()) {
            	ctx.controller.stop();
            }*/
        }
    }
    
    
    
    public void updateStatus(String newStatus) {
    	if (!Status.equalsIgnoreCase(newStatus)) {
    		Status = newStatus;
    		System.out.println(""+newStatus+" - "+utils.Time(getRuntime()));
    	};
    }
}