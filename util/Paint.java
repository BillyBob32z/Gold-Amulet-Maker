package goldamuletmaker.util;


import goldamuletmaker.Smelter;
import goldamuletmaker.util.Resources;
import goldamuletmaker.tasks.Smelt;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

public class Paint extends ClientAccessor {

    Smelter script;

    private Color lineColor = new Color(68,200,84, 255);
    private Color fillColor = new Color (21, 226, 114, 73);
    private Color mouseColor = new Color(44, 156, 203, 255);

    public Paint(ClientContext ctx, Smelter script){
        super(ctx);
        this.script = script;
    }

    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;



        final long runtime = (script.getRuntime());
        long second = (runtime / 1000) % 60;
        long minute = (runtime / (1000 * 60)) % 60;
        long hour = (runtime / (1000 * 60 * 60)) % 24;

        int amountHour = (int) ((Resources.amount * 3600000D) / runtime);

        g.setColor(lineColor);
        g.drawString("Runtime: " + String.format("%02dh %02dm %02ds", hour, minute, second), 20, 43);
        g.drawString("Amulets Made: " + Resources.amount + " (" + amountHour + ")", 20, 68);
        g.drawString(Resources.status, 20, 93);
        g.drawRect(15, 25, 160, 75);

        g.setColor(fillColor);
        g.fillRect(15, 25, 160, 75);

        g.setColor(mouseColor);
        Point mouseLoc = ctx.input.getLocation();

        g.drawLine(-3, mouseLoc.y, 768, mouseLoc.y);
        g.drawLine(mouseLoc.x, -3, mouseLoc.x, 506);

    }
}
