package eagledude4.ED4AlKharidSmelter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;

import eagledude4.ED4AlKharidSmelter.Task;
import eagledude4.ED4AlKharidSmelter.Main;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class Bank extends Task {
	Main main;
	
	int oreIds[];
	int barID;
    
	public Bank(Main main, ClientContext ctx, int[] oreIds, int barID) {
        super(ctx);
        this.oreIds = oreIds;
        this.barID = barID;
        this.main = main;
    }
	
	public void Deposit() {
		main.updateStatus("Depositing Bars");
		
		if(ctx.bank.depositInventory()){
            final int inventCount = ctx.inventory.select().count();
            Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != inventCount;
                }
            }, 250, 20);
        }
	}
	
	public void Withdraw() {
		main.updateStatus("Withdrawing Ore");
		
		for(Item ore: ctx.bank.select().id(oreIds)) {
			if (ore.stackSize() > 0) {
				 int x = ore.centerPoint().x - 11 + Random.nextInt(0, 25);
		         int y = ore.centerPoint().y - 12 + Random.nextInt(0, 25);
		         ctx.input.click(new Point(x, y), false);
		         if (!ctx.menu.click(Menu.filter("Withdraw-All"))) { //this will click without moving the mouse at all if 'Use' is the option (top left text)
		         	ore.interact("Withdraw-All"); //if not then interact() as normal, just incase
		         }
		         Condition.sleep(Random.nextInt(300,600));
			} else {
				JOptionPane.showMessageDialog(null, "Ran out of ore");
				ctx.controller.stop();
			}
		}
		
    	ctx.bank.close();
	}
	
	public void WithdrawBronze() {
		for(Item ore: ctx.bank.select().id(oreIds)) {
			if (ore.stackSize() >= 14) {
				ctx.bank.withdraw(ore.id(), 14);
		        Condition.sleep(Random.nextInt(300,600));
			} else {
				JOptionPane.showMessageDialog(null, "Ran out of ore");
				ctx.controller.stop();
			}
		}
		
    	ctx.bank.close();
	}
	
	public void WithdrawSteel() {
		for(Item ore: ctx.bank.select().id(oreIds)) {
			if (ore.id() == 440) {
				if (ctx.inventory.select().id(440).count() == 0) {
					if (ore.stackSize() >= 9) {
						ctx.bank.withdraw(ore.id(), 9);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of iron ore");
						ctx.controller.stop();
					}
				}
			} else if (ore.id() == 453) {
				if (ctx.inventory.select().id(453).count() == 0) {
					if (ore.stackSize() >= 18) {
						ctx.bank.withdraw(ore.id(), 18);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of coal");
						ctx.controller.stop();
					}
				}
			}
		}
		
    	ctx.bank.close();
	}
	
	public void WithdrawMithril() {
		for(Item ore: ctx.bank.select().id(oreIds)) {
			if (ore.id() == 447) {
				if (ctx.inventory.select().id(447).count() == 0) {
					if (ore.stackSize() >= 5) {
						ctx.bank.withdraw(ore.id(), 5);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of mithril ore");
						ctx.controller.stop();
					}
				}
			} else if (ore.id() == 453) {
				if (ctx.inventory.select().id(453).count() == 0) {
					if (ore.stackSize() >= 20) {
						ctx.bank.withdraw(ore.id(), 20);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of coal");
						ctx.controller.stop();
					}
				}
			}
		}
		
    	ctx.bank.close();
	}
	
	public void WithdrawAdamantite() {
		for(Item ore: ctx.bank.select().id(oreIds)) {
			if (ore.id() == 449) {
				if (ctx.inventory.select().id(447).count() == 0) {
					if (ore.stackSize() >= 4) {
						ctx.bank.withdraw(ore.id(), 4);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of mithril ore");
						ctx.controller.stop();
					}
				}
			} else if (ore.id() == 453) {
				if (ctx.inventory.select().id(453).count() == 0) {
					if (ore.stackSize() >= 24) {
						ctx.bank.withdraw(ore.id(), 24);
				        Condition.sleep(Random.nextInt(300,600));
					} else {
						JOptionPane.showMessageDialog(null, "Ran out of coal");
						ctx.controller.stop();
					}
				}
			}
		}
		
    	ctx.bank.close();
	}

    @Override
    public boolean activate() {
    	boolean needToWithdraw = ctx.inventory.select().id(barID).count() == 0;
    	boolean needToDeposit = false;
		
		if (main.oreIds.length == 1) {
			needToDeposit = (ctx.inventory.select().id(oreIds).count() == 0) && 
							(ctx.inventory.select().count() > 0);
		} else {
			needToDeposit = (ctx.inventory.select().id(oreIds[0]).count() == 0) && 
							(ctx.inventory.select().id(oreIds[1]).count() == 0) && 
							(ctx.inventory.select().count() > 0);
		}
		
        return (needToWithdraw || needToDeposit) && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
    	boolean needToWithdraw = ctx.inventory.select().id(barID).count() == 0;
    	boolean needToDeposit = false;
		
		if (main.oreIds.length == 1) {
			needToDeposit = (ctx.inventory.select().id(oreIds).count() == 0) && 
							(ctx.inventory.select().count() > 0);
		} else {
			needToDeposit = (ctx.inventory.select().id(oreIds[0]).count() == 0) && 
							(ctx.inventory.select().id(oreIds[1]).count() == 0) && 
							(ctx.inventory.select().count() > 0);
		}
	
        if(ctx.bank.opened()){
        	if (ctx.bank.currentTab() != 6) {
    			ctx.bank.currentTab(6);
    		}
        	
        	if (needToDeposit) {
        		Deposit();
        	} else if (needToWithdraw) {
        		if (barID == 2349) {
        			WithdrawBronze();
        		} else if (barID == 2353) {
        			WithdrawSteel();
        		} else if (barID == 2359) {
        			WithdrawMithril();
        		} else if (barID == 2361) {
        			WithdrawAdamantite();
        		} else {
        			Withdraw();
        		}
        	}
        } else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}