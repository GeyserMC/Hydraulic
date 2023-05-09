package org.geysermc.hydraulic.block;

import java.util.Map;

public record BlockState(Map<String, Variant> variants) {
}
