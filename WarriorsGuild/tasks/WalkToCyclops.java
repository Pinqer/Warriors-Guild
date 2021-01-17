package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.rt4.ClientContext;
import pinqer.WarriorsGuild.WarriorsGuild;

public class WalkToCyclops extends Task<ClientContext>{
    public WalkToCyclops(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return !ctx.inventory.select().select(n->n.name().contains("token")).isEmpty();
    }

    @Override
    public void execute() {

    }
}