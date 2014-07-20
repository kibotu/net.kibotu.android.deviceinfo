package net.kibotu.android.deviceinfo;


import org.jetbrains.annotations.NotNull;

/**
 * <p>Static logging facet for concrete device specific logger.
 * Depending on Logging level it shows only higher prioritized logs.
 * For instance: if the Logging Level is set to info, no debug messages will be shown, etc.
 * </p>
 * <p>Initialize with <code>Logger.init()</code>
 * </p>
 */
final public class Logger {

    public static boolean hasThrowableHook() {
        return ThrowableHook.throwableHook != null;
    }

    public abstract static class ThrowableHook {
        private static ThrowableHook throwableHook;
        abstract void handleException(@NotNull final Throwable e);

    }

    public static void setThrowableHook(@NotNull final ThrowableHook throwableHook) {
        ThrowableHook.throwableHook = throwableHook;
    }

    // region Variables.

    /**
     * Separator between tags.
     */
    public static final String SEPARATOR = ".";
    /**
     * Singleton.
     */
    private static Logger shared;
    /**
     * Logging level.
     */
    @NotNull
    private static Level logLevel;
    /**
     * Concrete Logger.
     */
    private static ILogger logger;
    /**
     * Application Tag.
     */
    private static String tag;

    /**
     * Constructor.
     *
     * @param logger - Concrete Logger.
     * @param tag    - Application tag, gets added to the beginning.
     * @param level  - Logging level.
     */
    private Logger(@NotNull final ILogger logger, @NotNull final String tag, @NotNull Level level) {
        Logger.logger = logger;
        Logger.tag = tag + SEPARATOR;
        Logger.logLevel = level;

//        String[] cmd = new String[] { "logcat", "-f", "/sdcard/testlog", "-v", "time", "ActivityManager:W", "myapp:D" };
//        try {
//            Runtime.getRuntime().exec(cmd);
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
    }

    // endregion

    // region Logger API

    /**
     * Initializing logger.
     *
     * @param logger - Concrete logger.
     */
    synchronized public static void init(@NotNull final ILogger logger) {
        if (shared == null) shared = new Logger(logger, logger.getClass().getSimpleName(), Level.DEBUG);
    }

    /**
     * Initializing logger.
     *
     * @param logger - Concrete Logger.
     * @param tag    - Application tag, gets added to the beginning.
     * @param level  - Logging level.
     */
    synchronized public static void init(@NotNull final ILogger logger, @NotNull final String tag, @NotNull Level level) {
        if (shared == null) shared = new Logger(logger, tag, level);
    }

    /**
     * Nullifies the logger instance.
     */
    synchronized public static void release() {
        shared = null;
        Logger.logger = null;
        Logger.tag = null;
        Logger.logLevel = null;
    }

    /**
     * Representing Information-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void i(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.INFO)) logger.information(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Verbose-Logging level.
     *
     * @param loggingTag Additional logging tag.
     * @param message    - Actual logging message.
     */
    public static void v(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.VERBOSE)) logger.verbose(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Debug-Logging level.
     *
     * @param loggingTag Additional logging tag.
     * @param message    - Actual logging message.
     */
    public static void d(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.DEBUG)) logger.debug(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Warning-Logging level.
     *
     * @param loggingTag Additional logging tag.
     * @param message    - Actual logging message.
     */
    public static void w(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.WARNING)) logger.warning(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Error-Logging level.
     *
     * @param loggingTag Additional logging tag.
     * @param message    - Actual logging message.
     */
    public static void e(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.ERROR)) logger.error(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Fatal-Logging level.
     *
     * @param loggingTag Additional logging tag.
     * @param message    - Actual logging message.
     */
    public static void f(@NotNull final String loggingTag, @NotNull final String message) {
        if (allowLogging(Level.FATAL)) logger.fatal(tag + loggingTag + SEPARATOR, message);
    }

    /**
     * Representing Information-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void i(@NotNull final String message) {
        if (allowLogging(Level.ERROR)) logger.error(tag + SEPARATOR, message);
    }

    /**
     * Representing Verbose-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void v(@NotNull final String message) {
        if (allowLogging(Level.VERBOSE)) logger.verbose(tag, message);
    }

    /**
     * Representing Debug-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void d(@NotNull final String message) {
        if (allowLogging(Level.DEBUG)) logger.debug(tag, message);
    }

    /**
     * Representing Warning-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void w(@NotNull final String message) {
        if (allowLogging(Level.WARNING)) logger.warning(tag, message);
    }

    /**
     * Representing Error-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void e(@NotNull final String message) {
        if (allowLogging(Level.ERROR)) logger.error(tag, message);
    }

    /**
     * Representing Fatal-Logging level.
     *
     * @param message - Actual logging message.
     */
    public static void f(@NotNull final String message) {
        if (allowLogging(Level.FATAL)) logger.fatal(tag, message);
    }


    /**
     * Representing Information-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void i(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.ERROR)) logger.error(tag, message, e);
    }

    /**
     * Representing Verbose-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void v(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.VERBOSE)) logger.verbose(tag, message, e);
    }

    /**
     * Representing Debug-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void d(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.DEBUG)) logger.debug(tag, message, e);
    }

    /**
     * Representing Warning-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void w(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.WARNING)) logger.warning(tag, message, e);
    }

    /**
     * Representing Error-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void e(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.ERROR)) logger.error(tag, message, e);
    }

    /**
     * Logging an exception.
     *
     * @param e Throwable.
     */
    public static void e(final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.ERROR)) {
            final String msg = e.getMessage();
            logger.error(tag, msg == null ? "" : msg, e);
        }
    }

    /**
     * Representing Fatal-Logging level.
     *
     * @param message - Actual logging message.
     * @param e       Additional Throwable.
     */
    public static void f(@NotNull final String message, @NotNull final Throwable e) {
        ThrowableHook.throwableHook.handleException(e);
        if (allowLogging(Level.FATAL)) logger.fatal(tag, message, e);
    }

    /**
     * Gets Logging level.
     *
     * @return Currently set logging level.
     */
    @NotNull
    public static Level getLogLevel() {
        validateState();
        return logLevel;
    }

    /**
     * Sets new Logging level.
     *
     * @param logLevel new logging level.
     */
    public static void setLogLevel(@NotNull final Level logLevel) {
        validateState();
        Logger.logLevel = logLevel;
    }

    /**
     * Gets current application tag.
     *
     * @return current set prefix tag.
     */
    @NotNull
    public static String getTag() {
        validateState();
        return tag.substring(0, tag.length() - 1);
    }

    /**
     * Sets new prefix tag.
     *
     * @param tag - Added to the beginning of all logs.
     */
    public static void setTag(@NotNull final String tag) {
        validateState();
        Logger.tag = tag + SEPARATOR;
    }

    public static void toast(@NotNull String message) {
        validateState();
        logger.toast(message);
    }

    // endregion

    // region Helper methods.

    /**
     * Checks against logging level.
     *
     * @param level - Defined logging level.
     * @return true if logging is allowed.
     */
    private static boolean allowLogging(@NotNull final Level level) {
        validateState();
        return logLevel.compareTo(level) <= 0;
    }

    /**
     * Validates if logger has been initialized.
     */
    private static void validateState() {
        if (logger == null) throw new IllegalStateException("Please call Logger.init() first.");
    }

    // endregion

    // region interfaces

    /**
     * Represents the logging levels.
     *
     * @see <a href="http://developer.android.com/tools/debugging/debugging-log.html" target="_blank">Filtering Log Output</a>
     */
    public static enum Level {

        VERBOSE("V"), DEBUG("D"), INFO("I"), WARNING("W"), ERROR("E"), FATAL("F"), SILENT("");
        public final String TAG;

        private Level(@NotNull final String tag) {
            TAG = tag;
        }
    }

    /**
     * Logging interface for concrete device specific logger.
     */
    public static interface ILogger {

        // region with throwable

        /**
         * Debug Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void debug(final String tag, final String message, final Throwable e);

        /**
         * Debug Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void verbose(final String tag, final String message, final Throwable e);

        /**
         * Information Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void information(final String tag, final String message, final Throwable e);

        /**
         * Warning Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void warning(final String tag, final String message, final Throwable e);

        /**
         * Error Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void error(final String tag, final String message, final Throwable e);

        /**
         * Error Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void fatal(final String tag, final String message, final Throwable e);

        // endregion

        //region without throwable

        /**
         * Debug Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void debug(final String tag, final String message);

        /**
         * Debug Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void verbose(final String tag, final String message);

        /**
         * Information Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void information(final String tag, final String message);

        /**
         * Warning Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void warning(final String tag, final String message);

        /**
         * Error Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void error(final String tag, final String message);

        /**
         * Error Message.
         *
         * @param tag     - Application Tag.
         * @param message - Logging message.
         */
        void fatal(final String tag, final String message);

        // endregion

        /**
         * Toast message.
         *
         * @param message - Displayed message.
         */
        void toast(final String message);
    }

    // endregion
}

