package goldamuletmaker.tasks;

import goldamuletmaker.Smelter;
import goldamuletmaker.util.Task;
import goldamuletmaker.util.Resources;
import goldamuletmaker.util.Variables;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.Random;

import java.util.concurrent.Callable;


public class Smelt extends Task<ClientContext> {

    Smelter script;

    private int furnaceID = Variables.furnaceID;
    private int barID = Variables.barID;
    private int mouldID = Variables.mouldID;
    private int amuletID = Variables.amuletID;

    public Smelt(ClientContext ctx) {
        super(ctx);
    }

    final GameObject FURNACE = ctx.objects.select().id(furnaceID).nearest().poll();
    Component INVENTORY = ctx.widgets.component(548, 65);
    Component inventoryButton = ctx.widgets.component(548, 47);
    Component GOLDAMULET = ctx.widgets.component(446, 32);
    Component enterAmount = ctx.widgets.component(162, 32);
    Component clickContinue = ctx.widgets.component(233, 2);


    public boolean activate() {
        return Resources.furnaceArea.contains(ctx.players.local())
                && ctx.inventory.select().id(barID).count() > 0
                && ctx.players.local().animation() == -1
                && ctx.inventory.select().id(mouldID).count() == 1
                && !ctx.players.local().inMotion();
    }

    public void execute() {
        if (ctx.inventory.select().id(barID).count() < 27) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() != -1;
                }
            }, 100, 20);
            if (ctx.players.local().animation() != -1) {
                return;
            }
        }

        if (Resources.furnaceArea.contains(ctx.players.local()) && !GOLDAMULET.visible() && ctx.players.local().animation() != 899 && !ctx.inventory.selectedItem().valid()) {
            Item BAR = ctx.inventory.select().id(barID).first().poll();
            Resources.status = "Clicking on Gold bar";
            BAR.interact("Use", "Gold bar");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.selectedItem().valid();
                }
            }, 250, 8);
        } else {
            if (ctx.inventory.selectedItem().valid() && !GOLDAMULET.visible()) {
                Resources.status = "Using Furnace!";
                FURNACE.interact("Use", FURNACE.name());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return GOLDAMULET.visible();
                    }
                }, 250, 5);
            } else {
                if (GOLDAMULET.visible() && !enterAmount.visible() && ctx.players.local().animation() == -1) {
                    Resources.status = "Using Furnace!";
                    GOLDAMULET.interact("Make-X", "Gold amulet (u)");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return enterAmount.visible();
                        }
                    }, 250, 8);
                } else {
                    if (enterAmount.visible()) {
                        Resources.status = "Making Amulets";
                        ctx.input.sendln(String.valueOf(Random.nextInt(28, 100)));
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.players.local().animation() != -1;
                            }
                        }, 250, 10);
                    } else {
                        if (clickContinue.visible()) {
                            clickContinue.click();
                        }
                    }
                }
            }
        }
    }
}