package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.*;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.List;
import java.util.concurrent.Callable;

public class Bank extends Task<ClientContext>{
    public Bank(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return (ctx.bank.opened() && ctx.inventory.select().name(main.foodName).isEmpty());
    }

    @Override
    public void execute() {
        main.currentTask = "Withdrawing food";
        if (ctx.bank.select().name(main.foodName).isEmpty()) { // if no food we log out
            ctx.game.logout();
            ctx.controller.stop();
        }
        if (ctx.inventory.select().name(main.foodName).isEmpty()) {
            ctx.bank.withdraw(main.foodName, main.foodAmount);
        }
    }
}
