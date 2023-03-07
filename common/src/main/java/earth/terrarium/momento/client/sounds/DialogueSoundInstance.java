package earth.terrarium.momento.client.sounds;

import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.srt.SrtBlock;
import earth.terrarium.momento.mixins.ChannelAccessor;
import earth.terrarium.momento.mixins.ChannelHandleAccessor;
import earth.terrarium.momento.mixins.SoundEngineAccessor;
import earth.terrarium.momento.mixins.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.openal.AL11;

import java.util.ArrayList;
import java.util.List;

public class DialogueSoundInstance extends EntityBoundSoundInstance {

    private final Player player;
    private final List<SrtBlock> shownBlocks;
    private final List<SrtBlock> queuedBlocks;

    public DialogueSoundInstance(Player player, Dialogue dialogue) {
        super(dialogue.sound(), SoundSource.VOICE, dialogue.volume(), 1.0f, player, System.currentTimeMillis());
        this.player = player;
        this.shownBlocks = new ArrayList<>();
        this.queuedBlocks = new ArrayList<>(dialogue.srt().blocks());
    }

    @Override
    public void tick() {
        super.tick();
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        SoundEngine engine = ((SoundManagerAccessor) manager).getEngine();
        ChannelHandleAccessor channel = (ChannelHandleAccessor) ((SoundEngineAccessor) engine).getChannels().get(this);
        if (channel != null) {
            ChannelAccessor accessor = (ChannelAccessor) channel.getChannel();
            if (accessor != null) {
                float time = AL11.alGetSourcef(accessor.getSource(), AL11.AL_SEC_OFFSET);
                int millis = (int) (time * 1000);
                showBlocks(millis);
                hideBlocks(millis);

                //TODO Switch to proper display
                shownBlocks.forEach(block -> System.out.println(block.text()));
            }
        }
    }

    private void showBlocks(int millis) {
        var iterator = queuedBlocks.iterator();
        while (iterator.hasNext()) {
            SrtBlock block = iterator.next();
            if (block.time().start() <= millis) {
                iterator.remove();
                shownBlocks.add(block);
            } else {
                break;
            }
        }
    }

    private void hideBlocks(int millis) {
        var iterator = shownBlocks.iterator();
        while (iterator.hasNext()) {
            SrtBlock block = iterator.next();
            if (block.time().end() <= millis) {
                iterator.remove();
            } else {
                break;
            }
        }
    }
}
