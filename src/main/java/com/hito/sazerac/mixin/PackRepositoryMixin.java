package com.hito.sazerac.mixin;


import com.hito.sazerac.SazeracRepositorySource;
import net.minecraft.Util;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(value = PackRepository.class, priority = 2000)
public abstract class PackRepositoryMixin {
    @Shadow
    protected abstract Stream<Pack> getAvailablePacks(Collection<String> pIds);

    @Shadow
    @Final
    private Set<RepositorySource> sources;

    @Inject(method = "rebuildSelected", at = @At("RETURN"), cancellable = true)
    private void rebuildSelectedMixin(Collection<String> enabledNames, CallbackInfoReturnable<List<Pack>> cir) {
        List<Pack> sortedEnabledPacks = cir.getReturnValue().stream().collect(Util.toMutableList());

        Optional<RepositorySource> source = sources.stream().filter(s -> s instanceof SazeracRepositorySource).findFirst();
        if (source.isEmpty())
            return;

        SazeracRepositorySource sazeracRepositorySource = (SazeracRepositorySource)source.get();
        List<Pack> orderedRequiredPack = getAvailablePacks((Collection<String>)sazeracRepositorySource.GetRequired())
                .flatMap(Pack::streamSelfAndChildren).toList();
        List<Pack> optionalPacks = getAvailablePacks(sazeracRepositorySource.GetOptionals())
                .flatMap(Pack::streamSelfAndChildren).toList();
        sortedEnabledPacks.removeAll(orderedRequiredPack);
        sortedEnabledPacks.removeAll(optionalPacks);
        sortedEnabledPacks.addAll(sortedEnabledPacks.size(), orderedRequiredPack);
        cir.setReturnValue(sortedEnabledPacks);
    }
}
