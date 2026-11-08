package org.geysermc.hydraulic.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.ServerWatchdog;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Stops the watchdog from killing the server while pack conversion runs before the first tick.
 */
@Mixin(ServerWatchdog.class)
public class ServerWatchdogMixin {
    @Shadow
    @Final
    private DedicatedServer server;

    @ModifyExpressionValue(
            method = "run",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;getNextTickTime()J")
    )
    private long hydraulic$ignoreBoot(long nextTickTime) {
        if (this.server.getTickCount() == 0) {
            return Util.getNanos();
        }

        return nextTickTime;
    }
}
