package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import pinqer.WarriorsGuild.Utility.Constants;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.List;

public class WalkToMonsters extends Task<ClientContext>{
    public WalkToMonsters(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return !Constants.MONSTERS_AREA.contains(ctx.players.local()) && !ctx.inventory.select().name(main.foodName).isEmpty();
    }

    @Override
    public void execute() {
        main.currentTask = "Walk to monsters";
        if (!Constants.MONSTERS_AREA.getCentralTile().matrix(ctx).reachable()) {
            // Handle the door obstacle
            GameObject obstacle = ctx.objects.select().id(24306).nearest().poll();
            if (obstacle.inViewport()) {
                if (ctx.bank.opened()) {
                    ctx.bank.close();
                    Condition.wait(()->!ctx.bank.opened(), 250, 8);
                }
                obstacle.interact("Open");
            } else {
                ctx.movement.step(obstacle);
                ctx.camera.turnTo(obstacle);
            }

            if (ctx.game.crosshair() == Game.Crosshair.ACTION) {
                Condition.wait(() -> ctx.players.local().animation() == -1 && !ctx.players.local().inMotion(), 1000, 4);
            }
        } else {
            ctx.movement.walkTo(Constants.MONSTERS_AREA.getCentralTile());
        }
    }
}
