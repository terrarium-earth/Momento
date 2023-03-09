package earth.terrarium.momento.forge;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.commands.Commands;
import earth.terrarium.momento.common.items.PlayerItem;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

@Mod(Momento.MOD_ID)
public class MomentoForge {
    public MomentoForge() {
        Momento.init();

        MinecraftForge.EVENT_BUS.addListener(MomentoForge::onRegisterCommand);

        new CreativeModeTab(Momento.MOD_ID + ".itemgroup") {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(Momento.PLAYER.get());
            }

            @Override
            public void fillItemList(@NotNull NonNullList<ItemStack> arg) {
                if (FMLLoader.getDist().isClient()) {
                    for (Dialogue dialogue : DialogueManager.getDialogues()) {
                        arg.add(PlayerItem.create(dialogue.id(), dialogue.icon()));
                    }
                }
            }
        };

        if (FMLLoader.getDist().isClient()) {
            MomentoForgeClient.setup();
        }
    }

    private static void onRegisterCommand(RegisterCommandsEvent event) {
        Commands.register(event.getDispatcher());
    }


}