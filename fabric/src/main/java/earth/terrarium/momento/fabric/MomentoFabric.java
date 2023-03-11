package earth.terrarium.momento.fabric;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.commands.Commands;
import earth.terrarium.momento.common.items.PlayerItem;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MomentoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Momento.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> Commands.register(dispatcher));

        FabricItemGroupBuilder.create(new ResourceLocation(Momento.MOD_ID, "itemgroup"))
                .icon(() -> new ItemStack(Momento.PLAYER.get()))
                .appendItems(list -> {
                    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                        for (Dialogue dialogue : DialogueManager.getDialogues()) {
                            list.add(PlayerItem.create(dialogue.id(), dialogue.icon()));
                        }
                    }
                })
                .build();
    }
}