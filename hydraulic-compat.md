# Hydraulic FABRIC Polymer Compatibility Notes ( Alpha Version )

## Overview

This document summarizes experimental compatibility work for using Fabric Hydraulic with Polymer generated Java resource packs on a Fabric crossplay ( With GeyserMC ) server.

The goal of this work is limited: make Polymer mods with generated assets discoverable, convertible, diagnosable, and safer for Bedrock clients through Geyser. It does not provide complete Polymer support or full Java to Bedrock model parity.

The changes were validated on a public crossplay server with live Java and Bedrock players. Results should still be treated as alpha level compatibility work until broader mod and client testing is completed.

## Tested Environment

- Minecraft/Fabric server: Fabric 26.1.2
- Fabric API Version : 0.149.1+26.1.2
- Hydraulic: development build with Polymer compatibility changes
- Geyser/Floodgate: present on the test server
- Polymer: installed and generating a server resource pack
- Polymer generated pack path observed in live testing: `polymer/resource_pack.zip`
- Polymer config path: `config/polymer/resource-pack.json`
- Clients: Java and Bedrock clients on a live public server

## Implemented Compatibility Improvements

### Polymer Pack Discovery

- Hydraulic detects Polymer generated resource packs and feeds them into the existing PackConverter pipeline as synthetic Hydraulic packs.
- Discovery includes common generated pack names and folders, including `polymer-resourcepack.zip`.
- Hydraulic reads `config/polymer/resource-pack.json` and resolves `resource_pack_location` relative to the server root.
- `polymer/resource_pack.zip` is searched by default because Polymer 0.16.5+26.1.2 generated that path during live testing.
- Hydraulic performs a later rescan during Geyser resource-pack registration.
- If Polymer is installed but no generated pack is found during initial startup, Hydraulic schedules one short delayed discovery pass. This handles Polymer AutoHost generating the pack after Hydraulic's first scan without adding an infinite poller.

### Caching and Sanitization

- Discovered Polymer packs are hashed with SHA-256.
- The cache key includes the active Hydraulic/PackConverter build identifier where available.
- If the pack hash and converter version match a previous conversion, Hydraulic reuses the cached Bedrock pack.
- Generated packs are copied through a sanitized cache root before conversion.
- Unsafe ZIP entries are skipped before PackConverter runs, including empty names, root level entries where not useful, and path traversal entries.
- Sanitization diagnostics record source path, sanitized path, existence checks, read/write checks, directory listing count, ZIP source state, ZIP open state, and final status.
- Failed sanitized conversions do not mark the cache as valid.

### Texture Safety

- PNG assets with unreadable dimensions, zero dimensions, negative dimensions, or dimensions above 8192 pixels are skipped.
- A bad texture should not crash the overall Polymer pack conversion.
- Skipped texture details are included in logs and diagnostic reports where observable.

### Item Texture Resolution

- Generated `assets/*/models/item/*.json` models are inspected for simple item texture mappings.
- Hydraulic resolves `textures.layer0` first, then `textures.layer1`.
- Basic texture variable indirection is supported, including `#layer0` inherited through simple parent chains.
- Supported parent patterns include:
  - `minecraft:item/generated`
  - `minecraft:item/handheld`
  - Custom parents inside the same generated-pack namespace
- Parent traversal is bounded to avoid recursion loops.
- Texture references are resolved using Java resource-pack rules:
  - `namespace:item/foo` maps to `assets/namespace/textures/item/foo.png`
  - `item/foo` maps to `assets/<current-namespace>/textures/item/foo.png`
- Simple non vanilla item models prefer their resolved real texture.
- If a non vanilla Polymer item model cannot be resolved, Hydraulic registers `hydraulic:polymer_placeholder` to avoid invisible items where possible.

### Placed Block Fallbacks

- Generated `assets/*/models/block/*.json` models are inspected for simple visible block fallbacks.
- Simple cube like parents can resolve a Bedrock terrain texture fallback, including:
  - `minecraft:block/cube`
  - `minecraft:block/cube_all`
  - `minecraft:block/cube_column`
  - `minecraft:block/cube_bottom_top`
  - Orientable cube variants
- Unsupported decorative or furniture block models use a visible full block fallback.
- If no texture can be resolved, Hydraulic uses `hydraulic:polymer_placeholder_block`.
- If a Java block lacks a normal mod `blockstates/*.json` entry but the generated Polymer pack has a matching generated block or item model, Hydraulic keeps that block in the custom-block registration path and uses a visible full cube fallback.
- This is intentionally an anti-invisible-block fallback, not accurate geometry conversion.

### PolyDecorations Handling

- When a generated Polymer pack is available, Hydraulic skips direct resource pack conversion for the `polydecorations` mod jar.
- The generated Polymer pack is preferred as the authoritative client asset source.
- This avoids duplicate or broken direct conversion through PackConverter's map icon texture stage while still allowing generated `assets/polydecorations` item models, block models, and textures to be processed.

### PackConverter Guards

- A null safe replacement for PackConverter's `MapIconsTransformer` skips deliberate empty map-icon grid slots instead of passing `null` into `UnsafeKey`.
- Missing `force_translucent` texture fields are treated as `false`, matching optional-field behavior and reducing PolyDecorations model deserialization noise.

## Diagnostics & Reporting

### Conversion Logging

Hydraulic logs Polymer conversion details including:

- Source pack path
- Source pack size
- Converted pack size
- Model count
- Texture count
- Item model count
- Skipped asset count
- Warning count
- Unsupported model summary

### Markdown Reports

When Polymer is installed or a Polymer pack is processed, Hydraulic can write a Markdown report to:

```text
config/hydraulic/debug/polymer-report-<timestamp>.md
```

The report is intended to be shared instead of raw server logs.

Report content includes:

- Hydraulic version or build identifier where available
- PackConverter version or build identifier where available
- Minecraft, Fabric Loader, Geyser, Java, and OS information where accessible
- Polymer `resource_pack_location`
- Resolved Polymer output path
- Discovery phase results, such as `initial`, `resource-pack-event`, and `delayed`
- Found pack paths
- SHA-256 hash
- Source size
- Cache status
- Sanitized input state
- Converted pack path and size
- Model, texture, and item model counts
- Skipped asset and warning counts
- Unsupported model summary
- Top detailed warnings
- Compact fatal conversion error summary

### Debug Report Configuration

Configuration file:

```text
config/hydraulic/polymer-debug.properties
```

Supported options:

```properties
enablePolymerDebugReports=true
polymerDebugMaxWarnings=25
polymerDebugIncludeStacktrace=false
```

`polymerDebugIncludeStacktrace=false` keeps report output compact. Set it to `true` only for local debugging when a full stack trace is needed.

Reports avoid server properties, player data, tokens, UUIDs, and raw log dumps.

### Polymer-Specific Report Fields

Reports also include Polymer bridge details where Hydraulic can observe them:

- Whether direct conversion was skipped for Polymer-backed mods
- Namespace asset counts for `polydecorations`
- Item texture resolution attempts and successes
- Grouped placeholder fallback reasons
- Top placeholder fallback examples
- Generated block models inspected
- Simple block mappings resolved
- Decorative fallback count
- Placeholder block fallback count
- Invisible-placement prevention count
- Grouped block fallback reasons
- Nullable JSON defaults applied
- Null-key texture entries skipped

## Known Limitations

- Full Polymer support is not implemented.
- Full Java-to-Bedrock block geometry conversion is not implemented.
- Decorative and furniture blocks may render as simple cubes or visible placeholders.
- Placeholder fallback indicates that Hydraulic preserved visibility or join stability, not that the original model was converted.
- Complex item models may still use `hydraulic:polymer_placeholder`.
- Generated model overrides, dynamic predicates, custom model data equivalents, and model chains outside the generated pack are not fully mapped.
- Multipart blockstates and complex parent chains are summarized but not converted into accurate Bedrock models.
- Player-head and block-disguise visuals are not converted into equivalent Bedrock geometry.
- Dynamic Polymer virtual entities are not represented by this resource-pack bridge.

## Retest Required

- PolyDecorations item rendering: verify one simple decoration item on a Bedrock client after Polymer AutoHost writes `polymer/resource_pack.zip`.
- PolyDecorations placed blocks: verify that generated pack assets are preferred and that direct mod-pack conversion remains skipped.
- Polymer placed-block fallback: verify one simple cube-like generated block and one decorative/furniture block on a Bedrock client. Expected result is visible fallback, not accurate geometry.
- OMS Server Additions: Decorations & Furniture: verify one simple furniture item and block on a Bedrock client after Polymer AutoHost writes `polymer/resource_pack.zip`.
- Geyser custom item registration for Polymer carrier stacks: continue validating item mappings that use modern `minecraft:item_model` stack components and Polymer `$polymer:stack` metadata.

## Unsupported Cases

The following cases are currently outside the scope of this bridge:

- Accurate furniture geometry
- Multipart blockstate conversion
- Dynamic virtual entities
- Player-head-based visuals
- Block-disguise-based visuals
- Custom model data parity
- Complex Java model predicate handling
- Full generated model override support
- Complete Polymer runtime behavior emulation

## Notes for Maintainers

- The bridge treats Polymer generated packs as ordinary resource pack inputs and routes them through Hydraulic's existing PackConverter path where possible.
- Compatibility is intentionally conservative. The preferred failure mode is a logged fallback or skipped asset, not a server crash or malformed Bedrock pack registration.
- The generated Polymer pack should generally be considered more authoritative than direct mod jar assets for Polymer backed content.
- The PolyDecorations direct-conversion skip is intentionally narrow and should not be generalized without additional testing.
- The placed-block fallback is designed to prevent invisible blocks for Bedrock clients. It should not be described as model-accurate support.
- Diagnostic reports are part of the compatibility surface. They should remain safe to share and should not include secrets, player identifiers, raw server properties, or full console logs by default.
- Future work should target one asset class at a time, with small fixtures or live-server validation notes for each compatibility improvement.

## Confirmed Working Polymer Mods 

The following Polymer based mod were validated through testing in a real working server with real players equipped with GeyserMC and has been throughly tested with Java and Bedrock players

### Confirmed Functional 

- ServerBackpacks ( https://modrinth.com/mod/serverbacksnow ) Confirmed Functional, Java + Bedrock inventory / storage confirmed. Worn backpack visuals remained for Java only.

### Upcoming Mods Being Tested 

- Toms Mobs ( https://modrinth.com/mod/toms-mobs ) Not Confirmed
- PolyDecorations ( https://modrinth.com/mod/polydecorations ) Not fully working, Textures still missing and errors still showing up, currently working and testing on it.
