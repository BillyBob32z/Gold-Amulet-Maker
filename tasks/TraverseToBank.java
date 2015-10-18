package goldamuletmaker.tasks;

import goldamuletmaker.util.Resources;
import goldamuletmaker.util.Task;
import goldamuletmaker.util.Variables;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;


public class TraverseToBank extends Task<ClientContext> {

    public TraverseToBank(ClientContext ctx){
        super(ctx);
    }

    private int barID = Variables.barID;
    private int mouldID = Variables.mouldID;
    private int closedDoor = Variables.closedDOOR;

    Component INVENTORY = ctx.widgets.component(548, 65);
    Component inventoryButton = ctx.widgets.component(548, 47);

    @Override
    public boolean activate(){
        return ctx.inventory.select().id(barID).count() == 0
                && !Resources.bankArea.contains(ctx.players.local())
                && ctx.players.local().animation() == -1
                && INVENTORY.visible()
                || ctx.inventory.select().id(mouldID).count() == 0
                && !Resources.bankArea.contains(ctx.players.local())
                && ctx.players.local().animation() == -1
                && INVENTORY.visible();

    }
    @Override
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

        GameObject DOOR = ctx.objects.select().id(closedDoor).name("Door").nearest().poll();

        if (DOOR.inViewport() && Resources.furnaceArea.contains(ctx.players.local()) && Resources.doorArea.contains(DOOR) && ctx.players.local().animation() != 899){
            Resources.status = "Opening Closed Door";
            DOOR.interact("Open", "Door");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !Resources.furnaceArea.contains(DOOR);
                }
            }, 250, 5);
        } else {
            if (!DOOR.inViewport() && Resources.furnaceArea.contains(DOOR) && Resources.furnaceArea.contains(ctx.players.local())) {
                Resources.status = "Turning to Closed Door";
                ctx.camera.turnTo(DOOR);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return DOOR.inViewport();
                    }
                }, 250, 5);
            }
            if (!Resources.bankArea.contains(ctx.players.local())){
                Resources.status = "Going to Bank!";
                ctx.movement.newTilePath(Resources.bankPath).traverse();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return Math.abs(ctx.players.local().tile().x() - ctx.movement.destination().x()) < 5 && Math.abs(ctx.players.local().tile().y() - ctx.movement.destination().y()) < 5;
                    }
                }, 250, 5);
            }
        }
    }
}
