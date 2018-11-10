package eagledude4.ED4CowArcher;

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
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Game.Tab;

import eagledude4.ED4CowArcher.tasks.GetArrows;
import eagledude4.ED4CowArcher.tasks.Fight;

import javax.imageio.ImageIO;
import javax.swing.*;
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

@Script.Manifest(name="ED4 Cow Archer", description="Trains Archery by killing cows at Lumbridge (OSRS)", properties="client=4; author=Eagledude4;")

public class Main extends PollingScript<ClientContext> implements MouseListener, MessageListener, PaintListener {
	Toolkit toolkit;
	
	public boolean hidePaint = false;
	
	List<Task> taskList = new ArrayList<Task>();
    
	public int startExp;
    public int startLevel;
    public int arrowID;
    
    public String Status = "Initializing";
    public String combatOptions[] = {"Attack", "Strength", "Defense"};
    public String userChoice;
    
    public long startTime;
    
    public Image bg;
    public Image cursor;
    public Image cowIcon;

    public void checkEquipment() {
    	if (ctx.game.tab() != Tab.EQUIPMENT) {
        	ctx.game.tab(Tab.EQUIPMENT);
    	}
    	
    	Item bow = ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND);
    	int bowID = bow.id();
    	if (bowID == 841) { //normal
    		arrowID = 884;
    	} else if (bowID == 843) { //oak
    		arrowID = 886;
    	} else if (bowID == 849) { //willow
    		arrowID = 888;
    	} else if (bowID == 853) { //maple
    		arrowID = 890;
    	}
    	
    	Condition.sleep(Random.nextInt(1000, 3000));
    	ctx.game.tab(Tab.INVENTORY);
    }
    
    @Override
    public void start(){
    	startExp = ctx.skills.experience(Constants.SKILLS_RANGE);
    	startLevel = ctx.skills.level(Constants.SKILLS_RANGE);
    	startTime = System.currentTimeMillis();
    	
    	hidePaint = true;
    	
    	userChoice = ""+(String)JOptionPane.showInputDialog(null, "Choose Combat Mode", "Cow Archer", JOptionPane.PLAIN_MESSAGE, null, combatOptions, combatOptions[0]);
        if(userChoice.equals("Attack")){
        	Component component = ctx.widgets.widget(593).component(4);
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Attack "+timeStamp);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("Accurate");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else if(userChoice.equals("Strength")){
        	Component component = ctx.widgets.widget(593).component(8);
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Strength "+timeStamp);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("rapid");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else if(userChoice.equals("Defense")){
        	Component component = ctx.widgets.widget(593).component(16);
        	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println("Training Defense "+timeStamp);
        	if (component.textureId() == 653) {
        		ctx.game.tab(Tab.ATTACK);
        		Condition.sleep(Random.nextInt(500, 1000));
        		component.interact("longrange");
        		Condition.sleep(Random.nextInt(500, 1000));
        	}
        } else {
            ctx.controller.stop();
        }
        
        hidePaint = false;
        
        //ctx.camera.angle(Random.nextInt(0, 359));
        //ctx.camera.pitch(Random.nextInt(90, 100));
        if (ctx.camera.pitch() > 0) {
        	ctx.camera.pitch(0);
        }
        
        checkEquipment();
        
        taskList.add(new Fight(this, ctx, MConstants.COW_IDS, arrowID));
        taskList.add(new GetArrows(this, ctx, arrowID));
        
        try {
			bg = ImageIO.read(new File("src/img/bg.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.controller.stop();
		}
        
        try {
			cowIcon = ImageIO.read(new File("src/img/Cow.png"));
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
    }
    
    public void updateStatus(String newStatus) {
    	Status = newStatus;
    	String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println(""+newStatus+" - "+timeStamp);
    }
    
    public void messaged(MessageEvent event) {
		if (event.text().contains("There is no ammo")) {
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			System.out.println("Executing Equip Arrows Task "+timeStamp);
			
	    	if (!ctx.inventory.select().id(arrowID).isEmpty()) {
		    	for (Item item : ctx.inventory.select().id(arrowID)) {
		    		if (ctx.controller.isStopping()) {
					    break;
					}
					item.click();
					break;
		    	}
	    	} else {
				System.out.println("Stopping script, no more arrows "+timeStamp);
	    		ctx.controller.stop();
	    	}
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
 			
 			int currentLevel = ctx.skills.realLevel(Constants.SKILLS_RANGE);
 			int currentExp = ctx.skills.experience(Constants.SKILLS_RANGE);
 			int expGained = currentExp-startExp;
 			int expToNextLevel = ctx.skills.experienceAt(currentLevel + 1) - ctx.skills.experience(Constants.SKILLS_RANGE);
 			int expPerHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - startTime));
 			String timeLeft = getTimeToNextLevel(expToNextLevel,expPerHour);
 			
 			Graphics2D g = (Graphics2D)graphics;
 			
 			Component messageBox = ctx.widgets.component(162, 0);
 			int x = messageBox.screenPoint().x;
 			int y = messageBox.screenPoint().y;
 			//int height = messageBox.height();
 			int width = messageBox.width();
 			
 			g.drawImage(bg, x, y, null);
 			g.drawImage(cowIcon, width - 100, y - 5, null);
 			g.drawImage(cursor, ctx.input.getLocation().x, ctx.input.getLocation().y, null);
 			
 			int ypos = y + 15;
 			int ygap = 20;
 			g.drawString("Running: "+String.format("%02d:%02d:%02d", hours, minutes, seconds)+" - "+Status, 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("Exp/Hour "+expPerHour, 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("Current RNG level "+((int)(currentLevel)), 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("RNG levels gained "+((int)(currentLevel - startLevel)), 10, ypos);
 			ypos = ypos + ygap;
 			g.drawString("EXP to next RNG level "+((int)(expToNextLevel)), 10, ypos);
 			g.drawString("Time to next RNG level "+timeLeft, 200, ypos);
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
 		}
 	}
 	
 	@Override
 	public void mouseExited(MouseEvent Event) {
 		Component messageBox = ctx.widgets.component(162, 0);
 		int x = messageBox.screenPoint().x;
 		int y = messageBox.screenPoint().y;
 		int height = messageBox.height();
 		int width = messageBox.width();
 		Rectangle paintArea = new Rectangle(x, y, width, height);
 		Point pointer = Event.getPoint();
 		
 		if (!paintArea.contains(pointer)) {
 			hidePaint = false;
 		}
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
