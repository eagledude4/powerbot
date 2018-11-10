package eagledude4.ED4Utils;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import eagledude4.ED4Utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class DrawPaint extends PollingScript<ClientContext> implements MouseListener, MessageListener, PaintListener {
	private final Utils utils = new Utils(ctx);
	
	private String customString1;
	private String customString2;
	private String skillString;
	private String[] listenString;
	
    private boolean hidePaint = false;
    
    private long startTime;
    private int startLevel;
    private int startExp;
    private double expYield;
    
    private Image bg;
    private Image cursor;
    private Image icon;
    
    private int skill;
    private int customInt2 = 0;
   
    
    @Override
    public void poll() {
    }
    
    public DrawPaint(String className, String iconURL, String iconIMG, int skill, String skillString, int startLevel, int startExp, long startTime, double expYield, String customString1, String customString2, String[] listenString) {
    	ctx.dispatcher.add(this);
    
    	//this.className = className;
    	this.skill = skill;
    	this.skillString = skillString;
    	this.startLevel = startLevel;
    	this.startExp = startExp;
    	this.startTime = startTime;
    	this.expYield = expYield;
    	
    	this.customString1 = customString1;
    	this.customString2 = customString2;
    	this.listenString = listenString;
    	
    	//this.bg = utils.getImageLocal("\\img\\bg.png");
        //this.cursor = utils.getImageLocal("\\img\\cursor.png");
        //this.icon = utils.getImageLocal("\\img\\icons\\"+iconName);
        
        this.bg = utils.getImage("http://i.imgur.com/4Yh5mER.png", "bg.png");
        this.cursor = utils.getImage("http://i.imgur.com/SmrJgUD.png", "cursor.png");
        this.icon = utils.getImage(iconURL, iconIMG);
    }
    
    @Override
    public void repaint(Graphics graphics) {
	    int currentLevel = ctx.skills.realLevel(skill);
		int currentExp = ctx.skills.experience(skill);
		int expGained = currentExp-startExp;
		int expToNextLevel = ctx.skills.experienceAt(currentLevel + 1) - ctx.skills.experience(skill);
		int expPerHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - startTime));
		
		String timeLeft = utils.getTimeToNextLevel(expToNextLevel,expPerHour);
		
		long runtime = this.getTotalRuntime();
		long seconds = (runtime / 1000) % 60;
		long minutes = (runtime / (1000*60) % 60);
		long hours = (runtime / (1000 * 60 * 60)) % 24;
		
	   if (!hidePaint) {
		    Graphics2D g = (Graphics2D)graphics;
			
			Component messageBox = ctx.widgets.component(162, 0);
			int x = messageBox.screenPoint().x;
			int y = messageBox.screenPoint().y;
			int width = messageBox.width();
			
			g.drawImage(bg, x, y, null);
			g.drawImage(icon, width - 100, y, null);
			g.drawImage(cursor, ctx.input.getLocation().x, ctx.input.getLocation().y, null);
			
			int ypos = y + 15;
			int ygap = 20;
			g.drawString("Running: "+String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, ypos);
			ypos = ypos + ygap;
			g.drawString("", 10, ypos);
			ypos = ypos + ygap;
			g.drawString("Exp/Hour: "+expPerHour, 10, ypos);
			int customInt1 = (int) (expPerHour / expYield);
			if (!customString1.equals("")) {
				g.drawString(customString1+customInt1, 200, ypos);
			}
 			ypos = ypos + ygap;
 			if (!customString2.equals("")) {
 				g.drawString(customString2+customInt2, 200, ypos);
 			}
			g.drawString("Current "+skillString+" level "+((int)(currentLevel)), 10, ypos);
			ypos = ypos + ygap;
			g.drawString(skillString+" levels gained "+((int)(currentLevel - startLevel)), 10, ypos);
			ypos = ypos + ygap;
			g.drawString("EXP to next "+skillString+" level "+((int)(expToNextLevel)), 10, ypos);
			g.drawString("Time to next "+skillString+" level "+timeLeft, 200, ypos);
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

	@Override
    public void messaged(MessageEvent event) {
		for (String s : listenString) {
			if (!event.text().equals("") && event.text().contains(s)) {
				customInt2++;
			}
		}
    }
}
