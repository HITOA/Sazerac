package com.hito.sazerac;

import com.mojang.logging.LogUtils;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackDetector;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.DirectoryValidator;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SazeracRepositorySource implements RepositorySource {
    private final Path directory;
    private final PackType packType;
    private final List<? extends String> required;

    private final List<String> optionals;

    public SazeracRepositorySource(Path directory, PackType packType, List<? extends String> required) {
        this.directory = directory;
        this.packType = packType;
        this.required = required;
        this.optionals = new ArrayList<>();
    }

    public List<? extends String> GetRequired() { return required; }
    public List<String> GetOptionals() { return optionals; }

    @Override
    public void loadPacks(Consumer<Pack> pOnLoad) {
        SazeracPackDetector detector = new SazeracPackDetector(new DirectoryValidator(p -> true));

        try {
            FileUtil.createDirectoriesSafe(directory);
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
            for (Path path : stream) {
                List<ForbiddenSymlinkInfo> symlinkInfos = new ArrayList<>();
                Pack.ResourcesSupplier supplier = detector.detectPackResources(path, symlinkInfos);
                if (!symlinkInfos.isEmpty()) {
                    LogUtils.getLogger().warn("ignoring potential pack entry: {}", ContentValidationException.getMessage(path, symlinkInfos));
                } else if (supplier == null) {
                    LogUtils.getLogger().info("found non-pack entry '{}', ignoring", path);
                } else {
                    String packName = path.getFileName().toString();
                    boolean isRequired = required.contains(packName);
                    PackLocationInfo locationInfo = new PackLocationInfo(packName, Component.literal(packName), SazeracPackSource.SAZERAC, Optional.empty());
                    PackSelectionConfig selectionConfig = new PackSelectionConfig(isRequired, Pack.Position.TOP, false);
                    Pack pack = Pack.readMetaAndCreate(locationInfo, supplier, packType, selectionConfig);
                    if (!isRequired)
                        optionals.add(packName);
                    pOnLoad.accept(pack);
                }
            }
            stream.close();
        } catch (IOException e) {
            LogUtils.getLogger().error("unhandled IOException while loading packs, no packs will be loaded with sazerac");
            LogUtils.getLogger().error(e.toString());
        }
    }

    static class SazeracPackDetector extends PackDetector<Pack.ResourcesSupplier> {
        protected SazeracPackDetector(DirectoryValidator validator) { super(validator); }

        @Override
        protected @Nullable Pack.ResourcesSupplier createZipPack(Path pPath) throws IOException {
            FileSystem fs = pPath.getFileSystem();
            if (fs != FileSystems.getDefault() && !(fs instanceof LinkFileSystem)) {
                LogUtils.getLogger().info("cannot open pack archive at {}", pPath);
                return null;
            } else {
                return new FilePackResources.FileResourcesSupplier(pPath);
            }
        }

        @Override
        protected @Nullable Pack.ResourcesSupplier createDirectoryPack(Path pPath) throws IOException {
            return new PathPackResources.PathResourcesSupplier(pPath);
        }
    }
}
