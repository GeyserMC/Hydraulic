package org.geysermc.hydraulic.pack;

import org.geysermc.pack.converter.util.LogListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class PackLogListener implements LogListener {
    private final Logger logger;

    public PackLogListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(@NotNull String s) {
        this.logger.debug(s);
    }

    @Override
    public void info(@NotNull String s) {
        this.logger.info(s);
    }

    @Override
    public void warn(@NotNull String s) {
        this.logger.warn(s);
    }

    @Override
    public void error(@NotNull String s) {
        this.logger.error(s);
    }

    @Override
    public void error(@NotNull String s, @Nullable Throwable throwable) {
        this.logger.error(s, throwable);
    }
}
