package goldamuletmaker.tasks;

import goldamuletmaker.util.Resources;
import goldamuletmaker.util.Task;
import goldamuletmaker.util.Variables;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;


public class TraverseToFurnace extends Task<ClientContext> {

    public TraverseToFurnace(ClientContext ctx) {
        super(ctx);
    }

    private int amuletID = Variables.amuletID;
    private int barID =  Variables.barID;
    private int closedDoor = Variables.closedDOOR;
    private int mouldID = Variables.mouldID;

    Component INVENTORY = ctx.widgets.component(548, 65);
    Component inventoryButton = ctx.widgets.component(548, 47);


    @Override
    public boolean activate() {

        return ctx.inventory.select().id(amuletID).count() == 0
                && !Resources.furnaceArea.contains(ctx.players.local())
                && ctx.inventory.select().id(barID).count() != 0
                && ctx.inventory.select().id(mouldID).count() == 1;
    }

    @Override
    public void execute() {
        GameObject DOOR = ctx.objects.select().id(closedDoor).name("Door").nearest().poll();

        if (!Resources.furnaceArea.contains(ctx.players.local()) && !Resources.doorArea.contains(DOOR)) {
            Resources.status = "Going to Furnace";
            ctx.movement.newTilePath(Resources.thePath).traverse();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return Resources.furnaceArea.contains(ctx.players.local());
                }
            }, 100, 8);
        } else {
            if (!Resources.furnaceArea.contains(ctx.players.local()) && Resources.doorArea.contains(DOOR)) {
                ctx.movement.step(DOOR);
                if (!DOOR.inViewport()) {
                    ctx.camera.turnTo(DOOR);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return DOOR.inViewport();
                        }
                    }, 100, 25);
                } else {
                    if (DOOR.inViewport()) {
                        DOOR.interact("Open", DOOR.name());
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return !Resources.doorArea.contains(DOOR);
                            }
                        }, 100, 25);
                    }
                }
            }
        }
    }
}