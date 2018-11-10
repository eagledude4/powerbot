package eagledude4.ED4FishingTrainer;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
//import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
//import org.powerbot.script.rt4.ClientContext;
import STRepo.ST.api.STai.amplified_api.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;

//import eagledude4.ED4FishingTrainer.AntibanTimer;
import eagledude4.ED4FishingTrainer.MConstants;
import eagledude4.ED4FishingTrainer.tasks.Fish;
//import eagledude4.ED4FishingTrainer.tasks.pickupFish;
import eagledude4.ED4FishingTrainer.tasks.LevelUp;
import eagledude4.ED4FishingTrainer.tasks.Bank;
import eagledude4.ED4FishingTrainer.tasks.Drop;
import eagledude4.ED4FishingTrainer.tasks.WalkFishingTutor;
import eagledude4.ED4FishingTrainer.tasks.WalkEdgeville;
import eagledude4.ED4FishingTrainer.tasks.WalkKaramja;
import eagledude4.ED4Utils.DrawPaint;

@Script.Manifest(name="ED4 Fishing Trainer", description="Trains Fishing at various locations (OSRS). Requires ED4Utils", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> {
	Toolkit toolkit;
	
	public String Status = "Initializing";
    public boolean hidePaint = false;
   
    public List<Integer> fishIDList;
    public List<Integer> keepItemsIDList;
    List<Task> taskList = new ArrayList<Task>();
    
    public long startTime;
    
    public String modeOptions[] = {"Auto", "Manual"};
    public String locationOptions[] = {"Lumbridge", "Barb Village", "Karamja"};
    public String bankOptions[] = {"Bank", "Drop"};
    
    public String autoMode;
    public String fishMethod;
    public String fishLocation;
    public String bankMode;
    
    public int startExp;
    public static int startLevel;
    public double expYield;
    
    public void addTaskNetFishing() {
    	taskList.add(new Fish(this, ctx, MConstants.NET_SPOT, fishMethod, fishLocation));
    	
    	if (startLevel >= 1 && startLevel < 15) {
    		System.out.println("Script will net shrimp "+Time(getRuntime()));
    		fishIDList = Arrays.asList(317);
    		expYield = 10;
    	} else if (startLevel >= 15) {
    		System.out.println("Script will net shrimp & anchovies "+Time(getRuntime()));
    		fishIDList = Arrays.asList(317, 321);
    		expYield = 40;
    	}
    	
    	int[] fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
		keepItemsIDList = Arrays.asList(303); //net
    	int[] keepItems = keepItemsIDList.stream().mapToInt(i -> i).toArray();
    	
    	/*for(Item item :  ctx.inventory.select().id(keepItems)){
    		if (!item.valid()) {
    			JOptionPane.showMessageDialog(null, "Missing Required Item(s)");
    			ctx.controller.stop();
    		}
    	}*/
    	
    	chooseBankMode(fishIds, keepItems);
    }
    
    public void addTaskLureFishing() {
    	taskList.add(new Fish(this, ctx, MConstants.LURE_SPOT, fishMethod, fishLocation));
    	
    	if (startLevel >= 20 && startLevel < 30) {
    		System.out.println("Script will lure trout "+Time(getRuntime()));
    		fishIDList = Arrays.asList(335);
    		expYield = 50;
    	} else if (startLevel >= 30) {
    		System.out.println("Script will lure trout & salmon "+Time(getRuntime()));
    		fishIDList = Arrays.asList(335, 331);
    		expYield = 70;
    	}
    	
    	int[] fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
		keepItemsIDList = Arrays.asList(314, 309); //rod & feathers
    	int[] keepItems = keepItemsIDList.stream().mapToInt(i -> i).toArray();
    	
    	/*for(Item item :  ctx.inventory.select().id(keepItems)){
    		if (!item.valid()) {
    			JOptionPane.showMessageDialog(null, "Missing Required Item(s)");
    			ctx.controller.stop();
    		}
    	}*/
    	
    	chooseBankMode(fishIds, keepItems);
    }

    public void addTaskHarpoonFishing() {
    	taskList.add(new Fish(this, ctx, MConstants.CAGE_HARPOON_SPOT, fishMethod, fishLocation));
    	
    	if (startLevel >= 35 && startLevel < 40) {
    		System.out.println("Script will harpoon tuna "+Time(getRuntime()));
    		fishIDList = Arrays.asList(359);
    		expYield = 80;
    	} else if (startLevel >= 50) {
    		System.out.println("Script will harpoon swordfish "+Time(getRuntime()));
    		fishIDList = Arrays.asList(359, 371);
    		expYield = 100;
    	}
    	
    	int[] fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
		keepItemsIDList = Arrays.asList(311, 995); //harpoon
    	int[] keepItems = keepItemsIDList.stream().mapToInt(i -> i).toArray();
    	
    	/*for(Item item :  ctx.inventory.select().id(keepItems)){
    		if (!item.valid()) {
    			JOptionPane.showMessageDialog(null, "Missing Required Item(s)");
    			ctx.controller.stop();
    		}
    	}*/
    	
    	chooseBankMode(fishIds, keepItems);
    }
    
    public void addTaskCageFishing() {
    	taskList.add(new Fish(this, ctx, MConstants.CAGE_HARPOON_SPOT, fishMethod, fishLocation));
    	
    	System.out.println("Script will cage lobsters "+Time(getRuntime()));
		
    	fishIDList = Arrays.asList(377);
		expYield = 90;
		
		int[] fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
		keepItemsIDList = Arrays.asList(301, 995); //lobster cage
    	int[] keepItems = keepItemsIDList.stream().mapToInt(i -> i).toArray();
    	
    	/*for(Item item :  ctx.inventory.select().id(keepItems)){
    		if (!item.valid()) {
    			JOptionPane.showMessageDialog(null, "Missing Required Item(s)");
    			ctx.controller.stop();
    		}
    	}*/
    	
    	chooseBankMode(fishIds, keepItems);
    }
    
    public void chooseAutoMode() {
    	String savedAutoMode = getAutoMode();
    	
    	if (savedAutoMode != "") {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, savedAutoMode);
    	} else {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	}
    	
    	//System.out.println(autoMode);
    	
    	if(autoMode.equals("Auto")){
	        if (startLevel >= 1 && startLevel < 20) {
	        	addTaskNetFishing();
	        } else if (startLevel >= 20 && startLevel < 35) {
	        	addTaskLureFishing();
	    	} else if (startLevel >= 35 && startLevel < 40) {
	    		addTaskHarpoonFishing();
	    	} else if (startLevel >= 50) {
	    		addTaskHarpoonFishing();
	    	} else if (startLevel >= 40 && startLevel < 50) {
	        	addTaskCageFishing();
	        }
        } else if(autoMode.equals("Manual")){
        	chooseLocation();
        	chooseFishingMethod();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void chooseFishingMethod() {
    	String methodChoice = getMethodChoice();

    	ArrayList<String> methodOptionsList = new ArrayList<String>();
    	if (fishLocation.equals("Karamja")) {
    		methodOptionsList.add("Cage");
    		methodOptionsList.add("Harpoon");
    		//String methodOptions[] = {"Cage", "Harpoon"};
    	} else if (fishLocation.equals("Barb Village")) {
    		methodOptionsList.add("Bait");
    		methodOptionsList.add("Lure");
    		//String methodOptions[] = {"Bait", "Lure"};
    	} else if (fishLocation.equals("Lumbridge")) {
    		methodOptionsList.add("Net");
    		methodOptionsList.add("Bait");
    		//String methodOptions[] = {"Net", "Bait"};
    	}
    	
    	String[] methodOptions = new String[methodOptionsList.size()];
    	methodOptions = methodOptionsList.toArray(methodOptions);
    	
    	if (methodChoice != "") {
    		fishMethod = ""+(String)JOptionPane.showInputDialog(null, "Choose Fishing Method", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, methodOptions, methodChoice);
    	} else {
    		fishMethod = ""+(String)JOptionPane.showInputDialog(null, "Choose Fishing Method", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, methodOptions, methodOptions[0]);
    	}
    	
    	//System.out.println(fishMethod);
    	
    	if(fishMethod.equals("Net")){
    		addTaskNetFishing();
        } else if(fishMethod.equals("Lure")){
        	addTaskLureFishing();
        } else if(fishMethod.equals("Cage")){
        	addTaskCageFishing();
        } else if(fishMethod.equals("Harpoon")){
        	addTaskHarpoonFishing();
    	} else {
        	ctx.controller.stop();
        }
    }
    
    public void chooseLocation() {
    	String locationChoice = getLocationChoice();
    	
    	if (locationChoice != "") {
    		fishLocation = ""+(String)JOptionPane.showInputDialog(null, "Choose Fishing Location", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationChoice);
    	} else {
    		fishLocation = ""+(String)JOptionPane.showInputDialog(null, "Choose Fishing Location", "Fishing Trainer", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);
    	}
    	
    	//System.out.println(fishLocation);
    }
    
    public void chooseBankMode(int[] fishIds, int[] keepItems) {
    	String bankChoice = getBankMode();
    	
    	if (bankChoice != "") {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankChoice);
    	} else {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankOptions[0]);
    	}
    	
    	if (bankMode.equals("Drop")) {
    		taskList.add(new Drop(this, ctx));
    	} else if (bankMode.equals("Bank")) {
    		if (fishLocation.equals("Karamja")) {
    			taskList.add(new WalkKaramja(this, ctx, MConstants.WALK_TILE_KARAMJA));
    		} else if (fishLocation.equals("Barb Village")) {
    			taskList.add(new WalkEdgeville(this, ctx, MConstants.WALK_TILE_EDGEVILLE, MConstants.TOBANK_EDGEVILLE));
    		} else if (fishLocation.equals("Lumbridge")) {
    			taskList.add(new WalkFishingTutor(this, ctx, MConstants.WALK_TILE_LUMBRIDGE, MConstants.TOBANK_LUMBRIDGE));
    		}
    		taskList.add(new Bank(this, ctx, fishIds, keepItems));
    	} else {
            ctx.controller.stop();
        }
    }
    
    public String getAutoMode() {
    	JSONParser parser = new JSONParser();
    	String automode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4FishingTrainer_config.json"));

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
    
    public String getMethodChoice() {
    	JSONParser parser = new JSONParser();
    	String methodChoice = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4FishingTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            methodChoice = (String) jsonObject.get("fishmethod");
            return methodChoice;
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
    
    public String getLocationChoice() {
    	JSONParser parser = new JSONParser();
    	String locationChoice = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4FishingTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            locationChoice = (String) jsonObject.get("fishlocation");
            return locationChoice;
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
            Object obj = parser.parse(new FileReader(path+"\\ED4FishingTrainer_config.json"));

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
        obj.put("fishmethod", fishMethod);
        obj.put("fishlocation", fishLocation);
        obj.put("bankmode", bankMode);

        File path = getStorageDirectory();
        try (FileWriter file = new FileWriter(path+"\\ED4FishingTrainer_config.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //String string = obj.toJSONString();
        //System.out.print(string);
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
    	startExp = ctx.skills.experience(Constants.SKILLS_FISHING);
    	startLevel = ctx.skills.level(Constants.SKILLS_FISHING);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	chooseAutoMode();
    	writeFile();
    	
        ctx.game.tab(Tab.INVENTORY);
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.game.tab() == Tab.INVENTORY;
            }
        }, 1000, 5);
        
        if (ctx.inventory.select().count()==0) {
        	JOptionPane.showMessageDialog(null, "Missing Required Item(s)");
			ctx.controller.stop();
        }
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }
        
        int fishID = fishIDList.get(fishIDList.size() - 1);
        String fishIcon = fishID + ".gif";
		
		hidePaint = false;
		String[] listenString = {"You catch a"};
		new DrawPaint("ED4FishingTrainer", fishIcon, Constants.SKILLS_FISHING, "FSH", startLevel, startExp, startTime, expYield, "Fish/Hour: ", "Fish Caught: ", listenString);
		
		taskList.add(new LevelUp(ctx));
		
		/*Frame thisFrame = JFrame.getFrames()[0];
		String fileName = "Screenshot_"+Time(getRuntime());
		String fileName = "Screenshot";
		makeScreenshot(thisFrame, fileName);*/
    }
    
    @Override
    public void poll() {
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
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
        
        if (ctx.game.loggedIn()) {
	        if (fishLocation.equals("Karamja") && (ctx.inventory.select().id(995).count(true) < 60)) {
	        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	        	System.out.println("Stopping Script, no more money "+timeStamp);
	        	ctx.controller.stop();
	        }
	        
	        if (fishMethod.equals("Lure") && (ctx.inventory.select().id(314).count(true) < 1)) {
	        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	        	System.out.println("Stopping Script, no more feathers "+timeStamp);
	        	ctx.controller.stop();
	        }
        }
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
