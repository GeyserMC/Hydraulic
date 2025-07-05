package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.hydraulic.misc.LanguageModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(value = MinecraftLocale.class, remap = false)
public class MinecraftLocaleMixin {
    @Inject(
            method = "getLocaleString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void injectGetLocalString(String messageText, String locale, CallbackInfoReturnable<String> cir) {
        // If the return result isn't the key, it was already translated, we shouldn't touch it
        if (!cir.getReturnValue().equals(messageText)) return;

        locale = locale.toLowerCase(Locale.ROOT); // Why does this happen?

        // a fallback in case we don't have the current locale
        if (!LanguageModule.TRANSLATIONS.containsKey(locale)) {
            locale = GeyserLocale.getDefaultLocale();
        }

        // if we *still* don't have the locale, consider the translation something we don't have
        if (!LanguageModule.TRANSLATIONS.containsKey(locale)) {
            return;
        }

        cir.setReturnValue(LanguageModule.TRANSLATIONS.get(locale).getOrDefault(messageText, messageText));
    }
}
