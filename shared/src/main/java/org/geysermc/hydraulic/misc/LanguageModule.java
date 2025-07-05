package org.geysermc.hydraulic.misc;

import com.google.auto.service.AutoService;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.hydraulic.pack.PackModule;
import team.unnamed.creative.lang.Language;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(PackModule.class)
public class LanguageModule extends PackModule<LanguageModule> {
    // Each pack is threaded, multiple may access this at the same time.
    public static final Map<String, Map<String, String>> TRANSLATIONS = new ConcurrentHashMap<>();

    public LanguageModule() {
        this.postProcess((context) -> {
            Collection<Language> languages = context.javaResourcePack().languages();

            for (Language languageData : languages) {
                TRANSLATIONS.putIfAbsent(correctBedrockKey(languageData.key().value()), new ConcurrentHashMap<>());
                TRANSLATIONS.get(correctBedrockKey(languageData.key().value())).putAll(languageData.translations());
            }
        });
    }

    private String correctBedrockKey(String key) {
        if (key.equals("no_no")) {
            return "nb_no";
        }

        return key;
    }
}
