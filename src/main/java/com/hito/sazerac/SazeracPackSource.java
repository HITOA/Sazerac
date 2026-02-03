package com.hito.sazerac;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.packs.repository.PackSource;

import java.util.function.UnaryOperator;

public interface SazeracPackSource extends PackSource {
    PackSource SAZERAC = PackSource.create(decorateWithSazerac(), true);

    static UnaryOperator<Component> decorateWithSazerac() {
        return c -> c.copy().append(" (sazerac)").withStyle(ChatFormatting.AQUA);
    }
}
