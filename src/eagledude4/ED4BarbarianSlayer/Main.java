package eagledude4.ED4BarbarianSlayer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4BarbarianSlayer.Task;
import eagledude4.ED4BarbarianSlayer.tasks.Bank;
import eagledude4.ED4BarbarianSlayer.tasks.Fight;
import eagledude4.ED4BarbarianSlayer.tasks.Heal;
import eagledude4.ED4BarbarianSlayer.tasks.LootItems;
import eagledude4.ED4BarbarianSlayer.tasks.MonitorHealth;
import eagledude4.ED4BarbarianSlayer.tasks.Walk;
import eagledude4.ED4Utils.DrawPaint;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

@Script.Manifest(name="ED4 Barbarian Slayer", description="Trains Combat by killig Barbarians in Falador (OSRS)", properties="client=4; author=Eagledude4; topic=999;")

public class Main extends PollingScript<ClientContext> {
	Toolkit toolkit;
	
	public boolean hidePaint = true;
	
	List<Task> taskList = new ArrayList<Task>();
    
	public int startExp;
    public int startLevel;
    public int BarbariansToKill;
    
    public static String Status = "Initializing";
	public String combatOptions[] = {"Attack", "Strength", "Defense"};
    public String fightMode;
    
    public int chosenSkill;
    public String skillString;
    
    public long startTime;
    
    
    @SuppressWarnings("unchecked")
	public void writeFile() {
    	JSONObject obj = new JSONObject();
        obj.put("fightmode", fightMode);

        File path = getStorageDirectory();
        try (FileWriter file = new FileWriter(path+"\\ED4BarbarianSlayer_config.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //String string = obj.toJSONString();
        //System.out.print(string);
    }
    
    public void chooseFightMode() {
    	String savedFightMode = getFightMode();
    	
    	if (savedFightMode != "") {
    		fightMode = ""+(String)JOptionPane.showInputDialog(null, "Combat Type", "Barbarian Slayer", JOptionPane.PLAIN_MESSAGE, null, combatOptions, savedFightMode);
    	} else {
    		fightMode = ""+(String)JOptionPane.showInputDialog(null, "Combat Type", "Barbarian Slayer", JOptionPane.PLAIN_MESSAGE, null, combatOptions, combatOptions[0]);
    	}
    	
    	//System.out.println(fightMode);
    	
    	if(fightMode.equals("Attack")){
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Attack "+timeStamp);
        	
        	startExp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        	startLevel = ctx.skills.level(Constants.SKILLS_ATTACK);
        	chosenSkill = Constants.SKILLS_ATTACK;
        	skillString = "ATT";
        	//BarbariansToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
        	Component component = ctx.widgets.widget(593).component(4);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("Chop");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else if(fightMode.equals("Strength")){
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Strength "+timeStamp);
        	
        	startExp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        	startLevel = ctx.skills.level(Constants.SKILLS_STRENGTH);
        	chosenSkill = Constants.SKILLS_STRENGTH;
        	skillString = "STR";
        	//BarbariansToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
        	Component component = ctx.widgets.widget(593).component(8);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("Slash");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else if(fightMode.equals("Defense")){
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Defense "+timeStamp);
        	
        	startExp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        	startLevel = ctx.skills.level(Constants.SKILLS_DEFENSE);
        	chosenSkill = Constants.SKILLS_DEFENSE;
        	skillString = "DEF";
        	//BarbariansToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
        	Component component = ctx.widgets.widget(593).component(16);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("Block");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else {
            ctx.controller.stop();
        }
    }
    
    public String getFightMode() {
    	JSONParser parser = new JSONParser();
    	String fightMode = "";
        try {
        	File path = getStorageDirectory();
            Object obj = parser.parse(new FileReader(path+"\\ED4BarbarianSlayer_config.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            fightMode = (String) jsonObject.get("fightMode");
            return fightMode;
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
    
    @Override
    public void start(){
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	chooseFightMode();
    	writeFile();
    	
    	ctx.game.tab(Tab.INVENTORY);
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.game.tab() == Tab.INVENTORY;
            }
        }, 1000, 5);
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        //ctx.camera.pitch(Random.nextInt(90, 100));
        
        taskList.add(new Walk(this, ctx, MConstants.BARBARIAN_AREA_TILE, MConstants.BANK_TILE, MConstants.TO_BARBARIANS, MConstants.BARBARIAN_IDS));
        taskList.add(new Fight(this, ctx, MConstants.BARBARIAN_AREA_TILE, MConstants.TO_BARBARIANS, MConstants.BARBARIAN_IDS));
        taskList.add(new MonitorHealth(ctx, ctx.combat.health()));
        taskList.add(new Heal(this, ctx));
        taskList.add(new Bank(this, ctx));
        taskList.add(new LootItems(this, ctx));
        
        hidePaint = false;
		String[] listenString = {""};
		new DrawPaint("ED4BarbarianSlayer", "Barbarian.png", chosenSkill, skillString, startLevel, startExp, startTime, 0, "", "", listenString);
    }

    @Override
    public void poll() {
    	if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
    	
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
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
