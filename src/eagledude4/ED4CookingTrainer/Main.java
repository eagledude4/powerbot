package eagledude4.ED4CookingTrainer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4CookingTrainer.tasks.Cook;
import eagledude4.ED4CookingTrainer.tasks.LevelUp;
import eagledude4.ED4CookingTrainer.tasks.Bank;
import eagledude4.ED4CookingTrainer.tasks.Walk;
import eagledude4.ED4Utils.DrawPaint;
import eagledude4.ED4CookingTrainer.tasks.Drop;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

@Script.Manifest(name="ED4 Cooking Trainer", description="Trains Cooking in Lumbridge Castle (OSRS)", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> implements MessageListener {
	Toolkit toolkit;
	
	public String Status = "Initializing";
    public boolean hidePaint = true;
   
    public List<Integer> fishIDList;
    public List<Integer> keepItemsIDList;
    public List<Task> taskList = new ArrayList<Task>();
    
    public long startTime;
    
    public String modeOptions[] = {"Auto", "Manual"};
    public String fishOptions[] = {"Shrimp & Anchovies", "Trout & Salmon", "Tuna", "Lobster", "Swordfish"};
    public String bankOptions[] = {"Bank", "Drop"};
    
    public String autoMode;
    public String fishType;
    public String bankMode;
    
    int[] fishIds;
    int startExp;
    int startLevel;
    int fishCooked = 0;
    double expYield;
   
    public void addTaskShrimpCooking() { //shrimp & anchovies
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	if (startLevel >= 1 && startLevel < 15) {
    		System.out.println("Script will net shrimp & anchovies "+timeStamp);

    		fishIDList = Arrays.asList(317, 321);
    		//expYield = 40;
    	} 
   
    	fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
    }
    
    public void addTaskTroutCooking() { //trout & salmon
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	if (startLevel >= 15 && startLevel < 30) {
    		System.out.println("Script will cook trout "+timeStamp);
    		
    		fishIDList = Arrays.asList(335);
    		//expYield = 50;
    	} else {
    		System.out.println("Script will cook trout & salmon "+timeStamp);
    		
    		fishIDList = Arrays.asList(335, 331);
    		//expYield = 70;
    	}
    	
    	fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
    }

    public void addTaskTunaCooking() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	
    	if (startLevel >= 30 && startLevel < 40) {
    		System.out.println("Script will harpoon tuna "+timeStamp);

    		fishIDList = Arrays.asList(359);
    		//expYield = 80;
    	}
    	
    	fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
    }
    
    public void addTaskLobsterCooking() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will cook lobsters "+timeStamp);
		
		fishIDList = Arrays.asList(377);
		//expYield = 90;
		
		fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
    }
    
    public void addTaskSwordfishCooking() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will cook lobsters "+timeStamp);
		
		fishIDList = Arrays.asList(371);
		expYield = 100;
		
		fishIds = fishIDList.stream().mapToInt(i -> i).toArray();
    }
    
    public void chooseBankMode() {
    	String bankChoice = getBankMode();
    	
    	if (bankChoice != "") {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankChoice);
    	} else {
    		bankMode = ""+(String)JOptionPane.showInputDialog(null, "Drop or Bank?", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, bankOptions, bankOptions[0]);
    	}
    	
    	if(bankMode.equals("Drop")){
			System.out.println("Script will drop cooked fish "+Time(getRuntime()));
    		
			//taskList.add(new Drop(this, ctx));
    	} else if(bankMode.equals("Bank")){
    		System.out.println("Script will bank cooked fish "+Time(getRuntime()));
    		
    		taskList.add(new Bank(this, ctx, fishIds));
    	} else {
            ctx.controller.stop();
        }
    }

    public void chooseAutoMode() {
    	String autoChoice = getAutoMode();
    	
    	if (autoChoice != "") {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, autoChoice);
    	} else {
    		autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	}
    		
    	if(autoMode.equals("Auto")){
	        if (startLevel < 15) {
	        	addTaskShrimpCooking();
	        } else if (startLevel >= 15 && startLevel < 30) {
	        	addTaskTroutCooking();
	    	} else if (startLevel >= 30 && startLevel < 40) {
	    		addTaskTunaCooking();
	    	} else if (startLevel >= 40 && startLevel < 45) {
	    		addTaskLobsterCooking();
	    	} else if (startLevel >= 45) {
	    		addTaskSwordfishCooking();
	        }
        } else if(autoMode.equals("Manual")){
        	chooseFishType();
        } else {
        	ctx.controller.stop();
        }
    	
    	chooseBankMode();
    }
    
    public void chooseFishType() {
    	String savedFishChoice = getFishType();
    	
    	if (savedFishChoice != "") {
    		fishType = ""+(String)JOptionPane.showInputDialog(null, "Choose Fish", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, fishOptions, savedFishChoice);
    	} else {
    		fishType = ""+(String)JOptionPane.showInputDialog(null, "Choose Fish", "Cooking Trainer", JOptionPane.PLAIN_MESSAGE, null, fishOptions, fishOptions[0]);
    	}
    	
    	if(fishType.equals("Shrimp & Anchovies")){
    		addTaskShrimpCooking();
        } else if(fishType.equals("Trout & Salmon")){
        	addTaskTroutCooking();
        } else if(fishType.equals("Tuna")){
        	addTaskTunaCooking();
        } else if(fishType.equals("Lobster")){
        	addTaskLobsterCooking();
        } else if(fishType.equals("Swordfish")){
        	addTaskSwordfishCooking();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public String getAutoMode() {
    	JSONParser parser = new JSONParser();
    	String automode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4CookingTrainer_config.json"));

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
    
    public String getFishType() {
    	JSONParser parser = new JSONParser();
    	String fishType = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4CookingTrainer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            fishType = (String) jsonObject.get("fishType");
            return fishType;
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
            Object obj = parser.parse(new FileReader(path+"\\ED4CookingTrainer_config.json"));

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
        obj.put("fishType", fishType);
        obj.put("bankmode", bankMode);

        File path = getStorageDirectory();
        try (FileWriter file = new FileWriter(path+"\\ED4CookingTrainer_config.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.print(obj);
    }
    
    @Override
    public void start(){
    	startExp = ctx.skills.experience(Constants.SKILLS_COOKING);
    	startLevel = ctx.skills.level(Constants.SKILLS_COOKING);
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
    	
        /*ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }*/
  
        taskList.add(new Bank(this, ctx, fishIds));
		taskList.add(new Walk(this, ctx, fishIds));
		taskList.add(new Cook(this, ctx, fishIds));
		//taskList.add(new Drop(ctx));
        taskList.add(new LevelUp(ctx));

        int fishID = fishIDList.get(fishIDList.size() - 1);
        String fishIcon = fishID + ".gif";
        String[] listenString = {"You successfully", "You manage", "You roast"};
        hidePaint = false;
        new DrawPaint("ED4CookingTrainer", fishIcon, Constants.SKILLS_COOKING, "CKG", startLevel, startExp, startTime, expYield, "Fish/Hour: ", "Fish Cooked: ", listenString);
    }

    @Override
    public void poll() {
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
            	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            	System.out.println("Script has ended "+timeStamp);
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
    }
    
    public void updateStatus(String newStatus) {
    	if (!Status.equalsIgnoreCase(newStatus)) {
    		Status = newStatus;
    		System.out.println(""+newStatus+" - "+Time(getRuntime()));
    	};
    }
    
    public void messaged(MessageEvent event) {
		if (event.text().contains("Nothing interesting") ) {
			ctx.camera.angle(Random.nextInt(90, 359));
		}
		if (event.text().contains("I can't reach") ) {
			Tile bankTile = new Tile(3209, 3219, 2);
			bankTile.matrix(ctx).click();
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
