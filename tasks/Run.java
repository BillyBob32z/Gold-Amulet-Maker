package goldamuletmaker.tasks;


import goldamuletmaker.util.Resources;
import goldamuletmaker.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Run extends Task<ClientContext> {

    public Run(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return ctx.movement.energyLevel() > 30 && !ctx.movement.running();

    }

    @Override
    public void execute() {
        Resources.status = "Turning Run On";
        ctx.movement.running(true);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.movement.running();
            }
        }, 250, 5);
    }
}
