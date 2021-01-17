package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.List;
import java.util.concurrent.Callable;

public class Heal extends Task<ClientContext>{

    public Heal(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().healthPercent() <= main.eatAtHealth && !ctx.inventory.select().name(main.foodName).isEmpty());
    }

    @Override
    public void execute() {
        main.currentTask = "Eating food";
        ctx.game.tab(Game.Tab.INVENTORY , true);
        Item food = ctx.inventory.select().name(main.foodName).poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return food.interact("Eat");
            }
        }, 450, 3);
    }
}
