package eagledude4.ED4RunecraftingTrainer;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4RunecraftingTrainer.MConstants;
import eagledude4.ED4RunecraftingTrainer.tasks.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

@Script.Manifest(name="ED4 Runecrafting Trainer", description="Trains Runecrafting at various locations (OSRS)", properties="client=4; author=Eagledude4; topic=999;")

public class Main extends PollingScript<ClientContext> implements MouseListener, MessageListener, PaintListener {
	//private final AntibanTimer antiban = new AntibanTimer(ctx);
	
	Toolkit toolkit;

	public static String Status = "Initializing";
    public boolean hidePaint = false;
    
    List<Task> taskList = new ArrayList<Task>();
    
    public long startTime;
    
    public Image bg;
    public Image cursor;
    public Image runeIcon;
    
    public int startExp;
    public int startLevel;
    public int runeID = 0;
    public int runesPerEssence;
    public int runesCrafted = 0;
    public double expYield;
    
    public void craftAirRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft air runes "+timeStamp);
    	
		runeID = 556;
		expYield = 5;
		
		/*if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}*/
		
		taskList.add(new Craft(this, ctx, MConstants.AIR_ALTAR_TILE, runeID));
    	taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new Walk(this, ctx, MConstants.AIR_RUIN_TILE, MConstants.AIR_ALTAR_TILE, MConstants.AIR_RUIN_PATH, runeID));
    }
    
    public void craftMindRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft mind runes "+timeStamp);
    	
		runeID = 558;
		expYield = 5.5;
		
		/*if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}*/
		
		taskList.add(new Craft(this, ctx, MConstants.MIND_ALTAR_TILE, runeID));
    	taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new Walk(this, ctx, MConstants.MIND_RUIN_TILE, MConstants.MIND_ALTAR_TILE, MConstants.MIND_RUIN_PATH, runeID));
    }
    
    public void craftWaterRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft water runes "+timeStamp);
    	
		runeID = 555;
		expYield = 6;
		
		/*if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}*/
		
		taskList.add(new Craft(this, ctx, MConstants.WATER_ALTAR_TILE, runeID));
    	taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new WalkLumbridge(this, ctx, MConstants.STAIRS_BOTTOM_TILE, MConstants.STAIRS_TOP_TILE, MConstants.WATER_RUIN_TILE, MConstants.WATER_ALTAR_TILE, MConstants.WATER_RUIN_PATH, runeID));
    }
    
    public void craftEarthRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft earth runes "+timeStamp);
    	
		runeID = 557;
		expYield = 6.5;
		
		/*if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}*/
		
		taskList.add(new Craft(this, ctx, MConstants.EARTH_ALTAR_TILE, runeID));
		taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new Walk(this, ctx, MConstants.EARTH_RUIN_TILE, MConstants.EARTH_ALTAR_TILE, MConstants.EARTH_RUIN_PATH, runeID));
    }
    
    public void craftFireRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft fire runes "+timeStamp);
    	
		runeID = 554;
		expYield = 7;
		
		if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}
		
		taskList.add(new Craft(this, ctx, MConstants.FIRE_ALTAR_TILE, runeID));
		taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new Walk(this, ctx, MConstants.FIRE_RUIN_TILE, MConstants.FIRE_ALTAR_TILE, MConstants.FIRE_RUIN_PATH, runeID));
    }
    
    public void craftBodyRunes() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will craft body runes "+timeStamp);
    	
		runeID = 559;
		expYield = 7.5;
		
		/*if (startLevel >= 14 && startLevel < 35) {
			runesPerEssence = 1;
		} else if (startLevel >= 35 && startLevel < 70) {
			runesPerEssence = 2;
		} else if (startLevel >= 70) {
			runesPerEssence = 3;
		}*/
		
		taskList.add(new Craft(this, ctx, MConstants.BODY_ALTAR_TILE, runeID));
		taskList.add(new Bank(this, ctx, runeID));
    	taskList.add(new Walk(this, ctx, MConstants.BODY_RUIN_TILE, MConstants.BODY_ALTAR_TILE, MConstants.BODY_RUIN_PATH, runeID));
    }
  
    public void chooseMode() {
    	String modeOptions[] = {"Auto", "Manual"};
    	String autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "RUNECRAFTING Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	
    	if(autoMode.equals("Auto")){
	        if (startLevel >= 1 && startLevel < 2) {
	        	craftAirRunes();
	        } else if (startLevel >= 2 && startLevel < 5) {
	        	craftMindRunes();
	    	} else if (startLevel >= 5 && startLevel < 9) {
	    		craftWaterRunes();
	    	} else if (startLevel >= 9 && startLevel < 14) {
	    		craftEarthRunes();
	    	} else if (startLevel >= 14 && startLevel < 20) {
	    		craftFireRunes();
	    	} else if (startLevel > 20) {
	    		craftBodyRunes();
	        }
        } else if(autoMode.equals("Manual")){
        	chooseRuneType();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void chooseRuneType() {
    	String runeOptions[] = {"Air", "Mind", "Water", "Earth", "Fire", "Body"};
    	String runeType = ""+(String)JOptionPane.showInputDialog(null, "Choose rune", "RUNECRAFTING Trainer", JOptionPane.PLAIN_MESSAGE, null, runeOptions, runeOptions[0]);
    	
    	if(runeType.equals("Air")){
    		startLevel = 1;
    		craftAirRunes();
        } else if(runeType.equals("Mind")){
        	startLevel = 2;
    		craftMindRunes();
        } else if(runeType.equals("Water")){
        	startLevel = 5;
    		craftWaterRunes();
        } else if(runeType.equals("Earth")){
        	startLevel = 9;
    		craftEarthRunes();
        } else if(runeType.equals("Fire")){
        	startLevel = 14;
    		craftFireRunes();
        } else if(runeType.equals("Body")){
        	startLevel = 20;
    		craftBodyRunes();
        } else {
        	ctx.controller.stop();
        }
    }
    
    @Override
    public void start(){
    	startExp = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING);
    	startLevel = ctx.skills.level(Constants.SKILLS_RUNECRAFTING);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	//pickaxeMode = ""+(String)JOptionPane.showInputDialog(null, "Use Inventory pickaxe?", "RUNECRAFTING Trainer", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);
    	
    	/*if(pickaxeMode.equals("Yes")){
        	usingInvPickaxe = true;
        } else {
        	usingInvPickaxe = false;
        }*/
    	
    	chooseMode();
    	
    	if (ctx.game.tab() != Tab.INVENTORY) {
    		ctx.game.tab(Tab.INVENTORY);
    	}
        
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        /*ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }*/
        
        try {
			runeIcon = ImageIO.read(new File("src/img/icons/"+runeID+".gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.controller.stop();
		}
        
        try {
			bg = ImageIO.read(new File("src/img/bg.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.controller.stop();
		}
		
		try {
			cursor = ImageIO.read(new File("src/img/cursor.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.controller.stop();
		}
		
		 hidePaint = false;
    }

    @Override
    public void poll() {
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
            	toolkit = Toolkit.getDefaultToolkit();
            	toolkit.beep();
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }

        if (!ctx.game.loggedIn()) {
        	ctx.controller.stop();
        }
        
        if (ctx.menu.items()[0].toString().contains("Use")) {
        	Item selectedItem = ctx.inventory.selectedItem();
        	selectedItem.click();
        }
    }
    
    public void updateStatus(String newStatus) {
    	Status = newStatus;
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println(""+newStatus+" - "+timeStamp);
    }
    
    public void messaged(MessageEvent event) {
		// TODO
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
   
   /*public boolean hidePaint() {
	   Component messageBox = ctx.widgets.component(162, 0);
		int x = messageBox.screenPoint().x;
		int y = messageBox.screenPoint().y;
		int height = messageBox.height();
		int width = messageBox.width();
		Rectangle paintArea = new Rectangle(x, y, width, height);
		Point pointer = ctx.input.getLocation();
		
		if (paintArea.contains(pointer)) {
			return true;
		}
		
		return false;
   }*/
   
   public void updateRunesCrafted(int num) {
	   if (runesCrafted == 0) {
		   runesCrafted = num;
	   } else {
		   runesCrafted = runesCrafted + num;
	   }
   }
   
   @Override
   public void repaint(Graphics graphics) {
	   if (!hidePaint) {
		    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		    long runtime = this.getTotalRuntime();
			long seconds = (runtime / 1000) % 60;
			long minutes = (runtime / (1000*60) % 60);
			long hours = (runtime / (1000 * 60 * 60)) % 24;
			
			int currentLevel = ctx.skills.realLevel(Constants.SKILLS_RUNECRAFTING);
			int currentExp = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING);
			int expGained = currentExp-startExp;
			int expToNextLevel = ctx.skills.experienceAt(currentLevel + 1) - ctx.skills.experience(Constants.SKILLS_RUNECRAFTING);
			int expPerHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - startTime));
			String timeLeft = getTimeToNextLevel(expToNextLevel,expPerHour);
			
			Graphics2D g = (Graphics2D)graphics;
			
			Component messageBox = ctx.widgets.component(162, 0);
			int x = messageBox.screenPoint().x;
			int y = messageBox.screenPoint().y;
			//int height = messageBox.height();
			int width = messageBox.width();
			
			g.drawImage(bg, x, y, null);
			g.drawImage(runeIcon, width - 100, y - 5, null);
			g.drawImage(cursor, ctx.input.getLocation().x, ctx.input.getLocation().y, null);
			
			int ypos = y + 15;
			int ygap = 20;
			//g.drawString("Running: "+String.format("%02d:%02d:%02d", hours, minutes, seconds)+" - "+Status, 10, ypos);
			g.drawString("Running: "+timeStamp+" - "+Status, 10, ypos);
			ypos = ypos + 40;
			g.drawString("Exp/Hour "+expPerHour, 10, ypos);
			int runePerHour = (int) (expPerHour / expYield);
 			g.drawString("Runes/Hour "+runePerHour * runesPerEssence, 200, ypos);
			ypos = ypos + ygap;
			g.drawString("Current RNC level "+((int)(currentLevel)), 10, ypos);
			ypos = ypos + ygap;
			g.drawString("RNC levels gained "+((int)(currentLevel - startLevel)), 10, ypos);
			ypos = ypos + ygap;
			g.drawString("EXP to next RNC level "+((int)(expToNextLevel)), 10, ypos);
			g.drawString("Time to next RNC level "+timeLeft, 200, ypos);
			ypos = ypos + ygap;
			g.drawString("Runes Crafted "+runesCrafted, 10, ypos);
	   }
    }
   
    @Override
	public void mouseClicked(MouseEvent Event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent Event) {
		Component messageBox = ctx.widgets.component(162, 0);
		int x = messageBox.screenPoint().x;
		int y = messageBox.screenPoint().y;
		int height = messageBox.height();
		int width = messageBox.width();
		Rectangle paintArea = new Rectangle(x, y, width, height);
		Point pointer = Event.getPoint();
		
		if (paintArea.contains(pointer)) {
			hidePaint = true;
			
			Condition.wait(new Callable<Boolean>(){
	            @Override
	            public Boolean call() throws Exception {
	                return !paintArea.contains(pointer);
	            }
	        }, 200, 10);
			
			hidePaint = false;
		}
	}
	
	@Override
	public void mouseExited(MouseEvent Event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent Event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent Event) {
		// TODO Auto-generated method stub
		
	}
}
