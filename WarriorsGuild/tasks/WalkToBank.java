package pinqer.WarriorsGuild.tasks;

import org.apache.commons.logging.Log;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Player;

import pinqer.WarriorsGuild.Utility.Constants;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.List;

public class WalkToBank extends Task<ClientContext>{
    public WalkToBank(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().name(main.foodName).isEmpty() && !Constants.BANK_AREA.contains(ctx.players.local()));
    }

    @Override
    public void execute() {
        GameObject bank = ctx.objects.select().name("Bank booth").nearest().poll();
        main.currentTask = "Walk to bank";
        Player player = ctx.players.local();
        if (!new Tile(2846,3543,0).matrix(ctx).reachable()) {
            // Handle the door obstacle
            GameObject obstacle = ctx.objects.select().id(24306).nearest().poll();
            if (obstacle.inViewport()) {
                obstacle.interact("Open");
            } else {
                ctx.movement.step(obstacle);
                ctx.camera.turnTo(obstacle);
            }

            if (ctx.game.crosshair() == Game.Crosshair.ACTION) {
                Condition.wait(() -> player.animation() == -1 && !player.inMotion(), 1000, 4);
            }
        } else {
            ctx.movement.step(new Tile(2846,3543,0));
        }
    }
}
