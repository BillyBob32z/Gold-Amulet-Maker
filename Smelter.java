package goldamuletmaker;

import goldamuletmaker.util.Paint;
import goldamuletmaker.util.Task;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import goldamuletmaker.tasks.*;
import goldamuletmaker.tasks.Smelt;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="Al-kharid Gold Amulet Maker", description="Makes gold Amulets in Al-kharid" )
public class Smelter extends PollingScript<ClientContext>  implements PaintListener {
    private List<Task> taskList = new ArrayList<Task>();
    private Paint paint = new Paint(ctx, this);

    @Override
    public void start() {
        taskList.addAll(Arrays.asList(new Run(ctx), new TraverseToBank(ctx), new Banking(ctx), new TraverseToFurnace(ctx), new Smelt(ctx)));
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
    }
    @Override
    public void stop() {
        System.out.println("Thanks for using Al-kharid Gold Amulet Maker!");
    }

    @Override
   public void repaint(Graphics graphics) {
       paint.repaint(graphics);
  }
}

