package pinqer.WarriorsGuild.tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import pinqer.WarriorsGuild.WarriorsGuild;

public abstract class Task <C extends ClientContext> extends ClientAccessor<C> {

    protected WarriorsGuild main;

    public Task(C ctx, WarriorsGuild main) {
        super(ctx);
        this.main = main;
    }

    public abstract boolean activate();

    public abstract void execute();
}