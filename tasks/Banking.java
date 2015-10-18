package goldamuletmaker.tasks;


import goldamuletmaker.util.Resources;
import goldamuletmaker.util.Task;
import goldamuletmaker.util.Variables;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Game;

import java.util.concurrent.Callable;

public class Banking extends Task<ClientContext> {

    public Banking(ClientContext ctx) {
        super(ctx);
    }

    private int bankID = Variables.bankID;
    private int barID = Variables.barID;
    private int mouldID = Variables.mouldID;
    private int amuletID = Variables.amuletID;

    Component INVENTORY = ctx.widgets.component(548, 65);

    @Override
    public boolean activate() {
        return Resources.bankArea.contains(ctx.players.local())
                && ctx.inventory.select().id(barID).count() == 0
                && !ctx.players.local().inMotion()
                || ctx.inventory.select().id(mouldID).count() == 0
                && Resources.bankArea.contains(ctx.players.local())
                && !ctx.players.local().inMotion();
    }

    @Override
    public void execute() {
        final GameObject BANK = ctx.objects.select().id(bankID).nearest().poll();
        final Item MOULD = ctx.inventory.select().id(mouldID).poll();

        if(ctx.game.tab(Game.Tab.INVENTORY)){
            Resources.status = "Switching to Inventory Tab";
        }

        if (!BANK.inViewport()) {
            Resources.status = "Turning to Bank";
            ctx.camera.turnTo(BANK);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return BANK.inViewport();
                }
            }, 100, 25);
        } else {
            if (BANK.inViewport() && !ctx.bank.opened()) {
                Resources.status = "Opening Bank";
                BANK.interact("Bank", BANK.name());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 8);
            } else {
                int amuletMoulds = ctx.bank.select().id(amuletID).poll().stackSize();
                if (ctx.inventory.select().id(mouldID).count() == 0 && ctx.bank.opened() && amuletMoulds != 0) {
                    ctx.bank.withdraw(mouldID, 1);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.contains(MOULD);
                        }
                    }, 250, 5);
                }
                if (ctx.bank.opened() && ctx.inventory.select().id(amuletID).count() > 0) {
                    Resources.status = "Depositing Gold Amulets";
                    int count = Resources.amount;
                    int count2 = ctx.inventory.select().id(amuletID).count();
                    ctx.bank.deposit(amuletID, Bank.Amount.ALL);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(amuletID).count() == 0;
                        }
                    }, 250, 8);
                    int count3 = ctx.inventory.select().id(amuletID).count();
                    Resources.amount = (count + count2 - count3);
                } else {
                    int barsLeft = ctx.bank.select().id(barID).poll().stackSize();

                    if (ctx.bank.opened() && ctx.inventory.select().id(barID).count() == 0 && ctx.inventory.select().id(amuletID).count() == 0 && barsLeft > 0) {
                        Resources.status = "Withdrawing Gold Bars";
                        ctx.bank.withdraw(barID, 27);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().id(barID).count() > 0;
                            }
                        }, 250, 8);
                    } else {
                        if (ctx.bank.opened() && ctx.inventory.select().id(barID).count() > 0 && ctx.inventory.select().id(amuletID).count() == 0 && ctx.inventory.contains(MOULD)) {
                            ctx.bank.close();
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    Resources.status = "Closing Bank!";
                                    return !ctx.bank.opened();
                                }
                            }, 250, 8);
                        } else {
                            if (ctx.bank.opened() && ctx.inventory.select().id(mouldID).count() == 0 || ctx.bank.opened() && barsLeft == -1) {
                                if (ctx.inventory.select().id(mouldID).count() == 0 && amuletMoulds == -1 || ctx.inventory.select().id(barID).count() == 0 && barsLeft == -1) {
                                    Resources.status = "No Mould or Bars Left";
                                    System.out.println("No Mould or Bars Left");
                                    ctx.controller.stop();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}