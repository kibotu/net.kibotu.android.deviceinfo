package net.kibotu.android.deviceinfo;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Custom uncaught exception handler in order to send occurred exceptions to host server before crashing.
 */
final public class UncaughtExceptionHandlerDecorator implements UncaughtExceptionHandler {

    private static UncaughtExceptionHandler customHandler;
    private final UncaughtExceptionHandler originalHandler;

    private UncaughtExceptionHandlerDecorator(final UncaughtExceptionHandler originalHandler) {
        this.originalHandler = originalHandler;
    }

    /**
     * Sets new default {@link UncaughtExceptionHandler}.
     *
     * @param customHandler Custom handler. Note: Overrides current one.
     */
    public static void setHandler(final UncaughtExceptionHandler customHandler) {
        UncaughtExceptionHandlerDecorator.customHandler = customHandler;
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (currentHandler instanceof UncaughtExceptionHandlerDecorator) {
            currentHandler = ((UncaughtExceptionHandlerDecorator) currentHandler).originalHandler;
        }
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerDecorator(currentHandler));
    }

    /**
     * Removes the custom {@link UncaughtExceptionHandler}.
     */
    public static void release() {
        final UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (currentHandler instanceof UncaughtExceptionHandlerDecorator) {
            Thread.setDefaultUncaughtExceptionHandler(((UncaughtExceptionHandlerDecorator) currentHandler).originalHandler);
        }
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        Logger.e(e);
        customHandler.uncaughtException(t, e);
        originalHandler.uncaughtException(t, e);
    }
}
