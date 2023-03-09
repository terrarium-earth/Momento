package earth.terrarium.momento.common.items;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PlayerItem extends Item {

    public PlayerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(ResourceLocation id, PlayerIcon icon) {
        ItemStack stack = new ItemStack(Momento.PLAYER.get());
        stack.getOrCreateTag().putString("Dialogue", id.toString());
        stack.getOrCreateTag().putString("Type", icon.name().toLowerCase(Locale.ROOT));
        return stack;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ResourceLocation id = getID(player.getItemInHand(hand));
        if (id != null) {
            if (!level.isClientSide()) {
                DialoguePacket.play(player, id);
                player.getCooldowns().addCooldown(this, 100);
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    private static ResourceLocation getID(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag()) {
            return null;
        }
        String id = stack.getTag().getString("Dialogue");
        return id.isEmpty() ? null : ResourceLocation.tryParse(id);
    }

    public static PlayerIcon getIcon(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag()) {
            return PlayerIcon.BRASS_TAPE_RECORDER;
        }
        return PlayerIcon.fromValue(stack.getTag().getString("Type"));
    }
}
