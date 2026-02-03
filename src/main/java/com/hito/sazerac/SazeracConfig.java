package com.hito.sazerac;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class SazeracConfig {
    public static final SazeracConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.BooleanValue datapackEnabled;
    public final ModConfigSpec.ConfigValue<String> datapackRootDirectory;
    public final ModConfigSpec.ConfigValue<List<? extends String>> datapackRequired;

    public final ModConfigSpec.BooleanValue resourcepackEnabled;
    public final ModConfigSpec.ConfigValue<String> resourcepackRootDirectory;
    public final ModConfigSpec.ConfigValue<List<? extends String>> resourcepackRequired;

    private SazeracConfig(ModConfigSpec.Builder builder) {
        builder.push("datapack");

        datapackEnabled = builder.comment(" Enable datapack handling by Sazerac.")
                .comment(" This does NOT change Minecraft’s default datapack behavior.")
                .define("enable", true);

        datapackRootDirectory = builder.comment(" Root directory from which datapacks are loaded.")
                .comment(" Defaults to Minecraft's \"datapacks\" directory.")
                .define("root", "datapacks");

        datapackRequired = builder.comment(" List of datapacks that will be force-loaded by Sazerac,")
                .comment(" relative to the root directory.").comment("")
                .comment(" Datapacks are loaded in the order they appear in this list.").comment("")
                .comment(" Example:")
                .comment(" required = [ \"My Datapack\", \"My Datapack.zip\" ]")
                .defineListAllowEmpty("required",
                ArrayList::new, () -> "", o -> o instanceof String);

        builder.pop();

        builder.push("resourcepack");

        resourcepackEnabled = builder.comment(" Enable resource pack handling by Sazerac.")
                .comment(" This does NOT change Minecraft’s default resource pack behavior.")
                .define("enable", true);

        resourcepackRootDirectory = builder.comment(" Root directory from which resource packs are loaded.")
                .comment(" Defaults to Minecraft's \"resourcepacks\" directory.")
                .define("root", "resourcepacks");

        resourcepackRequired = builder.comment(" List of resource packs that will be force-loaded by Sazerac,")
                .comment(" relative to the root directory.")
                .comment("")
                .comment(" Resource packs are loaded in the order they appear in this list.")
                .comment("")
                .comment(" Example:")
                .comment(" required = [ \"My Resource Pack\", \"My Resource Pack.zip\" ]")
                .defineListAllowEmpty("required",
                    ArrayList::new, () -> "", o -> o instanceof String);

        builder.pop();
    }

    static {
        Pair<SazeracConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(SazeracConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
