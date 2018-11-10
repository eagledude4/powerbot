package eagledude4.ED4GiantSlayer;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4GiantSlayer.Task;
import eagledude4.ED4GiantSlayer.tasks.Bank;
import eagledude4.ED4GiantSlayer.tasks.Fight;
import eagledude4.ED4GiantSlayer.tasks.Heal;
import eagledude4.ED4GiantSlayer.tasks.BuryBones;
import eagledude4.ED4GiantSlayer.tasks.LootItems;
import eagledude4.ED4GiantSlayer.tasks.MonitorHealth;
import eagledude4.ED4GiantSlayer.tasks.Walk;
import eagledude4.ED4Utils.DrawPaint;
import eagledude4.ED4Utils.Utils;
import eagledude4.ED4Utils.JSON;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

@Script.Manifest(name="ED4 Giant Slayer", description="Trains Combat by killig Hill/Moss Giants (OSRS)", properties="client=4; author=Eagledude4; topic=1348615;")

public class Main extends PollingScript<ClientContext> {
	private final Utils utils = new Utils(ctx);
	private final JSON JSON = new JSON(ctx);
	Toolkit toolkit;
	
	public boolean hidePaint = true;
	
	List<Task> taskList = new ArrayList<Task>();
    
	public int startExp;
    public int startLevel;
    public int GiantsToKill;
    
    public static String Status = "Initializing";
	public String combatOptions[] = {"Attack", "Strength", "Defense"};
	public String giantOptions[] = {"Hill Giants", "Moss Giants"};
	
    public String fightMode;
    public String giantType;
    
    public int chosenSkill;
    public String skillString;
    
    public long startTime;
    
    
   
    public void chooseGiantType() {
    	String fileName = "\\ED4GiantSlayer_config.json";
    	String savedGiantType = JSON.getJSONObj(fileName,"gianttype",getStorageDirectory());
    	
    	if (savedGiantType != "") {
    		giantType = ""+(String)JOptionPane.showInputDialog(null, "Giant Type", "Giant Slayer", JOptionPane.PLAIN_MESSAGE, null, giantOptions, savedGiantType);
    	} else {
    		giantType= ""+(String)JOptionPane.showInputDialog(null, "Giant Type", "Giant Slayer", JOptionPane.PLAIN_MESSAGE, null, giantOptions, giantOptions[0]);
    	}
    	
    	System.out.println("Giant Type "+giantType);
    }
    
    public void chooseFightMode() {
    	String fileName = "\\ED4GiantSlayer_config.json";
    	String savedFightMode = JSON.getJSONObj(fileName,"fightmode",getStorageDirectory());
    	
    	if (savedFightMode != "") {
    		fightMode = ""+(String)JOptionPane.showInputDialog(null, "Combat Type", "Giant Slayer", JOptionPane.PLAIN_MESSAGE, null, combatOptions, savedFightMode);
    	} else {
    		fightMode = ""+(String)JOptionPane.showInputDialog(null, "Combat Type", "Giant Slayer", JOptionPane.PLAIN_MESSAGE, null, combatOptions, combatOptions[0]);
    	}
    	
    	System.out.println("Fight Mode "+fightMode);
   
    	
    	if(fightMode.equals("Attack")){
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Attack "+timeStamp);
        	
        	startExp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        	startLevel = ctx.skills.level(Constants.SKILLS_ATTACK);
        	chosenSkill = Constants.SKILLS_ATTACK;
        	skillString = "ATT";
        	//GiantsToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
        	Component component = ctx.widgets.widget(593).component(3);
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
        	//GiantsToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
        	Component component = ctx.widgets.widget(593).component(7);
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
        	//GiantsToKill = (ctx.skills.experienceAt(startLevel + 1) - startExp) / 88;
        	
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
    	
    	String levelGoal = ""+(String)JOptionPane.showInputDialog(null, "Level Goal", "Giant Slayer", JTextField.CENTER);
    	if (!levelGoal.equals("")) {
    		utils.setGoal(fightMode,levelGoal);
    	}
    }
    
    @Override
    public void start(){
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	chooseGiantType();
    	chooseFightMode();
    	
    	String[][] objects = {{"gianttype",giantType},{"fightmode",fightMode}};
    	JSON.writeFile("\\ED4GiantSlayer_config.json",getStorageDirectory(),objects);
    	
    	ctx.game.tab(Tab.INVENTORY);
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.game.tab() == Tab.INVENTORY;
            }
        }, 1000, 5);
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        //ctx.camera.pitch(Random.nextInt(90, 100));
        
        if (giantType.equals("Hill Giants")) {
        	taskList.add(new Walk(this, ctx, MConstants.HILLGIANT_AREA_TILE, MConstants.EDGEVILLE_BANK_TILE, MConstants.TO_HILLGIANTS, MConstants.HILLGIANT_IDS));
        } else if (giantType.equals("Moss Giants")) {
        	taskList.add(new Walk(this, ctx, MConstants.MOSSGIANT_AREA_TILE, MConstants.VARROCK_BANK_TILE, MConstants.TO_MOSSGIANTS, MConstants.MOSSGIANT_IDS));
        } else {
            ctx.controller.stop();
        }
       
        taskList.add(new Heal(this, ctx));

        if (giantType.equals("Hill Giants")) {
        	 taskList.add(new LootItems(this, ctx));
        	 taskList.add(new BuryBones(this, ctx));
        	 taskList.add(new Fight(this, ctx, MConstants.HILLGIANT_AREA_TILE, MConstants.HILLGIANT_IDS));
        } else if (giantType.equals("Moss Giants")) {
        	 taskList.add(new Fight(this, ctx, MConstants.MOSSGIANT_AREA_TILE, MConstants.MOSSGIANT_IDS));
        } else {
            ctx.controller.stop();
        }
        
        taskList.add(new MonitorHealth(ctx, ctx.combat.health()));
        taskList.add(new Bank(this, ctx));
        
        hidePaint = false;
		String[] listenString = {""};
		new DrawPaint("ED4GiantSlayer", "Hill_giant.png", chosenSkill, skillString, startLevel, startExp, startTime, 0, "", "", listenString);
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
}
