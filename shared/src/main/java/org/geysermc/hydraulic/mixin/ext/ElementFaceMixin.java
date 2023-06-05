package org.geysermc.hydraulic.mixin.ext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import team.unnamed.creative.model.ElementFace;

@Mixin(value = ElementFace.class, remap = false)
public class ElementFaceMixin {

    /**
     * Overwrites the validateUVComponentInRange method
     * to not throw an exception when the UV component is out of range.
     * <p>
     * Many mods use UV components out of range, and some data
     * is handled specifically inside the mod's client jar.
     * <p>
     * Since we don't care about the range, we can just ignore it
     * if it's invalid, rather than just erroring and not converting the
     * rest of the mod's data.
     *
     * @author Redned
     * @reason unneeded on Bedrock
     */
    @Overwrite(remap = false)
    private void validateUVComponentInRange(float value, String name) {
    }
}
