package pinqer.WarriorsGuild;

import com.sun.tools.javac.Main;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import pinqer.WarriorsGuild.Utility.GUI;
import pinqer.WarriorsGuild.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TransformAttribute;
import java.util.ArrayList;
import java.util.List;


@Script.Manifest(
        name = "Warriors Guild",
        description = "Kills Animated Armor in Warriors Guild",
        version = "0.0.1"
)
public class WarriorsGuild extends PollingScript<ClientContext> implements PaintListener {

    public int startXp;
    public boolean startScript = false;
    public String foodName = "Tuna";
    public int foodAmount = 12;
    public int eatAtHealth = 15;
    public String currentTask = "Starting...";
    private final Font helveticaFont = new Font("Helvetica", 0, 12);
    private final Font helveticaFontBig = new Font("Helvetica", 0, 13);

    public List<Task> taskList = new ArrayList<Task>();

    // Vars for paint
    public long sAttackXp, sStrengthXp, sDefenceXp, sHitpointsXp, sRangeXp, sMagicXp;
    public long cAttackXp, cStrengthXp, cDefenceXp, cHitpointsXp, cRangeXp, cMagicXp;

    @Override
    public void start() {
        // Start the GUI
        SwingUtilities.invokeLater((() -> {
            GUI gui = new GUI(ctx, this );
        }));

        // Initialize starting xp
        sAttackXp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        sStrengthXp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        sDefenceXp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        sHitpointsXp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        sRangeXp = ctx.skills.experience(Constants.SKILLS_RANGE);
        sMagicXp = ctx.skills.experience(Constants.SKILLS_MAGIC);
    }

    @Override
    public void stop() {

    }

    @Override
    public void poll() {
        if (startScript) {
            for (Task t : taskList) {
                if (t.activate()) {
                    t.execute();
                    break;
                }
            }
        }
    }

    @Override
    public void repaint(Graphics g) {
        cAttackXp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        cStrengthXp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        cDefenceXp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        cRangeXp = ctx.skills.experience(Constants.SKILLS_RANGE);
        cHitpointsXp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        cMagicXp = ctx.skills.experience(Constants.SKILLS_MAGIC);
        long combatXpGained = (cAttackXp - sAttackXp) + (cStrengthXp - sStrengthXp) + (cDefenceXp - sDefenceXp)
                + (cRangeXp - sRangeXp) + (cHitpointsXp - sHitpointsXp) + (cMagicXp - sMagicXp);

        // Paint window
        g.setColor(new Color(0, 0, 0,255));
        g.drawRect(240,352,250,100);
        g.setColor(new Color(33, 33, 33, 230));
        g.fillRect(240,352,250,100);

        // Info text
        g.setColor(Color.ORANGE);
        g.setFont(helveticaFontBig);
        g.drawString("Pinq's Token Gatherer",302,372);
        g.setColor(Color.WHITE);
        g.setFont(helveticaFont);
        g.drawString("Combat exp. gained: " + combatXpGained + " (" + getPerHour(combatXpGained, this.getRuntime()) +"/pH)", 250, 397);
        g.drawString("Time running: " + formatTime((int)this.getRuntime()/1000), 250, 417);
        g.drawString("Current task: " + currentTask, 250, 437);
    }

    private long getPerHour(long in, long time) {
        return (int) ((in) * 3600000D / time);
    }

    private String formatTime(long time) {
        return String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));

    }
}
