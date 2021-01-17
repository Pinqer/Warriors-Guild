package pinqer.WarriorsGuild.tasks;


import org.powerbot.script.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import pinqer.WarriorsGuild.Utility.Constants;
import pinqer.WarriorsGuild.WarriorsGuild;

import java.util.concurrent.Callable;

public class Attack extends Task<ClientContext>{
    Npc currentNpc;

    public Attack(ClientContext ctx, WarriorsGuild main) {
        super(ctx, main);
    }

    @Override
    public boolean activate() {
        return (!ctx.players.local().interacting().name().contains("Armour") && Constants.MONSTERS_AREA.contains(ctx.players.local()) && !ctx.inventory.select().name(main.foodName).isEmpty());
    }

    @Override
    public void execute() {
        // if there's ground items, mithril or token
        GroundItems items = (GroundItems) ctx.groundItems.select(12).select((groundItem) -> (groundItem.name().contains("helm") || groundItem.name().contains("platebody") || groundItem.name().contains("platelegs")) || groundItem.name().contains("token") && Constants.MONSTERS_AREA.contains(groundItem)).nearest();
        if (!items.isEmpty() && ctx.inventory.select().size() < 28) {
            main.currentTask = "Picking up loot";
            GroundItem item = ctx.groundItems.select().select(n->(n.name().contains("helm") || n.name().contains("platebody") || n.name().contains("platelegs")) || n.name().contains("token")).nearest().poll();
            if (item.valid()) {
                if (item.inViewport()) {
                    if (item.interact("Take")) {
                        Condition.sleep(Random.nextInt(900,1400));
                        return;
                    }
                }
                else {
                    ctx.movement.step(item);
                    ctx.camera.turnTo(item);
                    Condition.wait(()->item.inViewport(),450,5);
                }
            }
        }

        // if inventory contains all of armour pieces
        if (!ctx.inventory.select().select(n->n.name().contains("helm")).isEmpty() && !ctx.inventory.select().select(n->n.name().contains("platebody")).isEmpty() && !ctx.inventory.select().select(n->n.name().contains("platelegs")).isEmpty() && items.isEmpty()) {
            main.currentTask = "Interacting with animator";
            GameObject animator = ctx.objects.select().name("Magical Animator").nearest().poll();
            if (animator.valid()) {
                if (animator.inViewport()) {
                    animator.interact("Animate");
                    if (ctx.game.crosshair() == Game.Crosshair.ACTION) {
                        // Wait until we have started interacting
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                return ctx.players.local().interacting().valid();
                            }
                        }, 450, 10);
                    }
                } else {
                    ctx.movement.step(animator);
                    ctx.camera.turnTo(animator);

                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return animator.inViewport();
                        }
                    },250, 10);
                }
            }
        }

        // Search for a valid NPC
        BasicQuery<Npc> npcQuery = ctx.npcs.select().select(n->n.name().contains("Armour") && n.animation() != 4166).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return npc.interacting().equals(ctx.players.local()) || (!npc.interacting().valid() && npc.healthPercent() > 0);
            }
        }).nearest().limit(4);

        // Create a temp variable for npc
        currentNpc = npcQuery.peek();
        // Check if one of them is attacking us
        for (Npc n : npcQuery) {
            if (n.interacting().equals(ctx.players.local())) {
                // change currentNpc to the attacking one
                currentNpc = n;
            }
        }

        if (currentNpc.valid()) {
            main.currentTask = "Attacking NPC";
            if (currentNpc.inViewport()) {
                currentNpc.interact("Attack");
                if (ctx.game.crosshair() == Game.Crosshair.ACTION) {
                    // Wait until we have started interacting
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return currentNpc.interacting().equals(ctx.players.local());
                        }
                    }, 450, 4);
                }
            }
        } else {
            ctx.movement.step(currentNpc);
            ctx.camera.turnTo(currentNpc);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return currentNpc.inViewport();
                }
            },250, 10);
        }
    }
}
