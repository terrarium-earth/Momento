package earth.terrarium.momento.forge;

import earth.terrarium.momento.Momento;
import net.minecraftforge.fml.common.Mod;

@Mod(Momento.MOD_ID)
public class MomentoForge {
    public MomentoForge() {
        Momento.init();
    }
}