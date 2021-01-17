package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import pinqer.WarriorsGuild.Utility.Constants;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.List;

public class OpenBank extends Task<ClientContext>{
    public OpenBank(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return Constants.BANK_AREA.contains(ctx.players.local()) && !ctx.bank.opened() && ctx.inventory.select().name(main.foodName).isEmpty();
    }

    @Override
    public void execute() {
        main.currentTask = "Open bank";
        if (ctx.bank.inViewport()) {
            if (ctx.bank.open())
                Condition.wait(()-> ctx.bank.opened(), 1000, 2);
        } else {
            ctx.movement.step(ctx.bank.nearest().tile());
            ctx.camera.turnTo(ctx.bank.nearest());
        }
    }
}
