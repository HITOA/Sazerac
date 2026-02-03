package com.hito.sazerac;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod(Sazerac.MODID)
public class Sazerac
{
    public static final String MODID = "sazerac";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Sazerac(IEventBus modEventBus, ModContainer modContainer)
    {
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
