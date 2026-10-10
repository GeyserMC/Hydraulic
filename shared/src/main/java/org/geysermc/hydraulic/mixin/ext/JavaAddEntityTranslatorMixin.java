package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.geyser.entity.GeyserEntityType;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.entity.JavaAddEntityTranslator;
import org.geysermc.hydraulic.entity.CustomEntityTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: Temporary class for custom entity types until we have a proper registry system in place. This is a stopgap
//       solution to allow for custom entities to be registered and used in the game.
@Mixin(value = JavaAddEntityTranslator.class, remap = false)
public class JavaAddEntityTranslatorMixin {

    @Redirect(
        method = "translate(Lorg/geysermc/geyser/session/GeyserSession;Lorg/geysermc/mcprotocollib/protocol/packet/ingame/clientbound/entity/ClientboundAddEntityPacket;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/geyser/entity/GeyserEntityType;of(Lorg/geysermc/mcprotocollib/protocol/data/game/entity/type/EntityType;)Lorg/geysermc/geyser/entity/GeyserEntityType;"
        )
    )
    private GeyserEntityType hydraulic$resolveCustomType(EntityType vanilla, GeyserSession session, ClientboundAddEntityPacket packet) {
        int javaTypeId = CustomEntityTypes.javaTypeId(packet);
        if (!CustomEntityTypes.isCustom(javaTypeId)) {
            return GeyserEntityType.of(vanilla);
        }

        GeyserEntityType custom = CustomEntityTypes.byJavaId(javaTypeId);
        // Falling back to the placeholder lets the translator report it as unregistered rather
        // than silently spawning the wrong entity.
        return custom != null ? custom : GeyserEntityType.of(vanilla);
    }
}
