package eagledude4.ED4SmithingTrainer;

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
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4SmithingTrainer.MConstants;
import eagledude4.ED4SmithingTrainer.tasks.Walk;
import eagledude4.ED4SmithingTrainer.tasks.Bank;
import eagledude4.ED4SmithingTrainer.tasks.Smith;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

@Script.Manifest(name="ED4 Smithing Trainer", description="Trains Smithing at Varrock (OSRS)", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> implements MouseListener, MessageListener, PaintListener {
	Toolkit toolkit;
	
	public String Status = "Initializing";
    
	public boolean hidePaint = true;
    
	public List<Task> taskList = new ArrayList<Task>();
    
	public int startExp = 0;
    public int startLevel = 0;
    public int currentLevel = 0;
    public int barID = 0;
    public int barsMade = 0;
    public double expYield;
    public long startTime;
    
    public Image bg;
    public Image cursor;
    public Image barIcon;
    
    public void smithBronze() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will smith bronze bars "+timeStamp);
		
    	expYield = 12.5;
		barID = 2349;
   
    	taskList.add(new Smith(this, ctx, MConstants.ANVIL_TILE, barID));
    }
    
    public void smithIron() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will smith iron bars "+timeStamp);
		
    	expYield = 25.0;
		barID = 2351;
   
    	taskList.add(new Smith(this, ctx, MConstants.ANVIL_TILE, barID));
    }
    
    public void smithSteel() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will smith Steel bars "+timeStamp);
		
    	expYield = 37.5;
		barID = 2353;
   
    	taskList.add(new Smith(this, ctx, MConstants.ANVIL_TILE, barID));
    }
    
    public void smithMithril() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will smith Mithril bars "+timeStamp);
		
    	expYield = 50;
		barID = 2359;
   
    	taskList.add(new Smith(this, ctx, MConstants.ANVIL_TILE, barID));
    }
    
    public void smithAdamant() {
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("Script will smith Adamant bars "+timeStamp);
		
    	expYield = 62.5;
		barID = 2361;
   
    	taskList.add(new Smith(this, ctx, MConstants.ANVIL_TILE, barID));
    }
    
    public void chooseMode() {
    	String modeOptions[] = {"Auto", "Manual"};
    	String autoMode = ""+(String)JOptionPane.showInputDialog(null, "Mode Type", "Mining Trainer", JOptionPane.PLAIN_MESSAGE, null, modeOptions, modeOptions[0]);
    	
    	if(autoMode.equals("Auto")){
	        if (startLevel >= 1 && startLevel < 15) {
	        	smithBronze();
	        } else if (startLevel >= 15 && startLevel < 30) {
	        	smithIron();
	    	} else if (startLevel >= 30 && startLevel < 50) {
	    		smithSteel();
	    	} else if (startLevel >= 50 && startLevel < 70) {
	    		smithMithril();
	    	} else if (startLevel >= 70 && startLevel < 85) {
	    		smithAdamant();
	    	}
        } else if(autoMode.equals("Manual")){
        	chooseBarType();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void chooseBarType() {
    	String barOptions[] = {"Bronze", "Iron", "Steel", "Mithril"};
    	String barChoice = ""+(String)JOptionPane.showInputDialog(null, "Choose a Bar to Smith", "Smithing Trainer", JOptionPane.PLAIN_MESSAGE, null, barOptions, barOptions[2]);
    	
    	if(barChoice.equals("Bronze")){
    		smithBronze();
        } else if(barChoice.equals("Iron")){
        	smithIron();
        } else if(barChoice.equals("Steel")){
        	smithSteel();
        } else if(barChoice.equals("Mithril")){
        	smithMithril();
        } else {
        	ctx.controller.stop();
        }
    }
    
    public void openDonatePage() {
    	try {
			Desktop.getDesktop().browse(new URL("https://www.powerbot.org/").toURI());
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
    	
    	chooseMode();
    	
    	taskList.add(new Walk(ctx, MConstants.ANVIL_TILE, barID));
    	taskList.add(new Bank(this, ctx, barID));

        ctx.game.tab(Tab.INVENTORY);
        
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        if (ctx.camera.pitch() < 90) {
        	ctx.camera.pitch(Random.nextInt(90, 99));
        }
        
        try {
			bg = ImageIO.read(new File("src/img/bg.png")); //credits to Nomivore
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.controller.stop();
		}
		
        try {
			barIcon = ImageIO.read(new File("src/img/icons/"+barID+".gif"));
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
    	
        if (!ctx.game.loggedIn()) {
        	ctx.controller.stop();
        }
        
        Component newLevel = ctx.widgets.component(233, 0);
		Component continueButton = ctx.widgets.component(233, 2);
    	if (newLevel.visible()) {
    		continueButton.interact("Continue");
    	};
    }
    
    public void updateStatus(String newStatus) {
    	Status = newStatus;
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println(""+newStatus+" - "+timeStamp);
    }
    
    public void messaged(MessageEvent event) {
		if (event.text().contains("You hammer")) {
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
    
    //Method which gets time till next level.
    public String getTimeToNextLevel(final int expLeft, final int xpPerHour) {
        //If not earning exp then return
        if (xpPerHour < 1) {
     	   return "No EXP gained yet.";
        }

        //If gaining exp then measure approximately it will take to level.
        return Time((long)(expLeft * 3600000D / xpPerHour));
    }
    
    @Override
    public void repaint(Graphics graphics) {
 	   if (!hidePaint) {
 		    long runtime = this.getTotalRuntime();
 			long seconds = (runtime / 1000) % 60;
 			long minutes = (runtime / (1000*60) % 60);
 			long hours = (runtime / (1000 * 60 * 60)) % 24;
 			
 			currentLevel = ctx.skills.realLevel(Constants.SKILLS_SMITHING);
 			int currentExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
 			int expGained = currentExp-startExp;
 			int expToNextLevel = ctx.skills.experienceAt(currentLevel + 1) - ctx.skills.experience(Constants.SKILLS_SMITHING);
 			int expPerHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - startTime));
 			String timeLeft = getTimeToNextLevel(expToNextLevel,expPerHour);
 			
 			Graphics2D g = (Graphics2D)graphics;
 			
 			Component messageBox = ctx.widgets.component(162, 0);
 			int x = messageBox.screenPoint().x;
 			int y = messageBox.screenPoint().y;
 			//int height = messageBox.height();
 			int width = messageBox.width();
 			
 			g.drawImage(bg, x, y, null);
 			g.drawImage(barIcon, width - 100, y - 5, null);
 			g.drawImage(cursor, ctx.input.getLocation().x, ctx.input.getLocation().y, null);
 			
 			int ypos = y + 15;
 			int ygap = 20;
 			g.drawString("Running: "+String.format("%02d:%02d:%02d", hours, minutes, seconds)+" - "+Status, 10, ypos);
 			ypos = ypos + 40;
 			g.drawString("Exp/Hour "+expPerHour, 10, ypos);
 			int barsPerHour = (int) (expPerHour / expYield);
 			g.drawString("Bars/Hour "+barsPerHour, 200, ypos);
 			ypos = ypos + ygap;
 			g.drawString("Current SMT level "+((int)(currentLevel)), 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("SMT levels gained "+((int)(currentLevel - startLevel)), 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("EXP to next SMT level "+((int)(expToNextLevel)), 10, ypos);
 			g.drawString("Time to next SMT level "+timeLeft, 200, ypos);
 			ypos = ypos + ygap;
 			g.drawString("Bars made "+barsMade, 10, ypos);
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