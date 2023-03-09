package earth.terrarium.momento.client.sounds;

import com.mojang.blaze3d.audio.Channel;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import earth.terrarium.momento.common.srt.SrtBlock;
import earth.terrarium.momento.common.srt.SrtFile;
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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogueSoundInstance extends EntityBoundSoundInstance {

    private final Dialogue dialogue;
    private final List<SrtBlock> shownBlocks;
    private final List<SrtBlock> queuedBlocks;

    private boolean started = false;

    public DialogueSoundInstance(Player player, Dialogue dialogue, SrtFile file) {
        super(dialogue.sound(), SoundSource.VOICE, dialogue.volume(), 1.0f, player, System.currentTimeMillis());
       this.dialogue = dialogue;
        this.shownBlocks = Collections.synchronizedList(new ArrayList<>());
        this.queuedBlocks = Collections.synchronizedList(new ArrayList<>(file.blocks()));
    }

    @Override
    public void tick() {
        super.tick();
        ChannelAccessor accessor = getChannel();
        if (accessor != null) {
            float time = AL11.alGetSourcef(accessor.getSource(), AL11.AL_SEC_OFFSET);
            int millis = (int) (time * 1000);
            boolean changed = showBlocks(millis);
            if (hideBlocks(millis)) changed = true;

            if (changed) {
                DisplayRenderer.update(this);
            }

            if (accessor instanceof Channel channel && channel.stopped()) { // End of sound
                DialogueHandler.checkQueue();
                this.stop();
            }
        }
        started = true;
    }

    @Nullable
    private ChannelAccessor getChannel() {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        SoundEngine engine = ((SoundManagerAccessor) manager).getEngine();
        ChannelHandleAccessor channel = (ChannelHandleAccessor) ((SoundEngineAccessor) engine).getChannels().get(this);
        if (channel != null) {
            return (ChannelAccessor) channel.getChannel();
        }
        return null;
    }

    public boolean isNotPlaying() {
        return started && getChannel() == null;
    }

    private boolean showBlocks(int millis) {
        boolean changed = false;
        var iterator = queuedBlocks.iterator();
        while (iterator.hasNext()) {
            SrtBlock block = iterator.next();
            if (block.time().start() <= millis) {
                iterator.remove();
                shownBlocks.add(block);
                changed = true;
            } else {
                break;
            }
        }
        return changed;
    }

    private boolean hideBlocks(int millis) {
        boolean changed = false;
        var iterator = shownBlocks.iterator();
        while (iterator.hasNext()) {
            SrtBlock block = iterator.next();
            if (block.time().end() <= millis) {
                iterator.remove();
                changed = true;
            } else {
                break;
            }
        }
        return changed;
    }

    public List<SrtBlock> blocks() {
        return shownBlocks;
    }

    public Dialogue dialogue() {
        return this.dialogue;
    }

    public void stopInternal() {
        stop();
    }
}
