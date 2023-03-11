package earth.terrarium.momento.common.items;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.managers.DialogueManager;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        ResourceLocation id = getID(itemStack);
        if (level != null && id != null) {
            Dialogue dialogue1 = DialogueManager.get(id);
            if (dialogue1 != null) {
                list.add(Component.translatable(dialogue1.name()).withStyle(ChatFormatting.GRAY));
            }
        }
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
