package com.hito.sazerac;

import net.minecraft.server.packs.PackType;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.nio.file.Path;
import java.nio.file.Paths;


@Mod(Sazerac.MODID)
public class Sazerac
{
    public static final String MODID = "sazerac";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Sazerac(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, SazeracConfig.CONFIG_SPEC);
        modEventBus.addListener(Sazerac::OnPackFinderEvent);
    }

    private static void OnPackFinderEvent(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES && SazeracConfig.CONFIG.resourcepackEnabled.isTrue()) {
            Path resourcepackPath = Paths.get(FMLPaths.GAMEDIR.get().toString(), SazeracConfig.CONFIG.resourcepackRootDirectory.get());
            event.addRepositorySource(new SazeracRepositorySource(resourcepackPath, event.getPackType(), SazeracConfig.CONFIG.resourcepackRequired.get()));
        } else if (event.getPackType() == PackType.SERVER_DATA && SazeracConfig.CONFIG.datapackEnabled.isTrue()) {
            Path datapackPath = Paths.get(FMLPaths.GAMEDIR.get().toString(), SazeracConfig.CONFIG.datapackRootDirectory.get());
            event.addRepositorySource(new SazeracRepositorySource(datapackPath, event.getPackType(), SazeracConfig.CONFIG.datapackRequired.get()));
        }
    }
}
