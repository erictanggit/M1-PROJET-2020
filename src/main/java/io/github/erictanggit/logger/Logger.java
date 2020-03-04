package io.github.erictanggit.logger;

public class Logger {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Log");

	// printing methods:
	public static void trace(Class<?> classe, Object message) {
		log.trace("[ " + classe + " ] : [TRACE] " + message);
	}

	public static void debug(Class<?> classe, Object message) {
		log.debug("[ " + classe + " ] : [DEBUG] " + message);
	}

	public static void info(Class<?> classe, Object message) {
		log.info("[ " + classe + " ] : [INFO] " + message);
	}

	public static void warn(Class<?> classe, Object message) {
		log.warn("[ " + classe + " ] : [WARN] " + message);
	}

	public static void error(Class<?> classe, Object message) {
		log.error("[ " + classe + " ] : [ERROR] " + message);
	}

	public static void fatal(Class<?> classe, Object message) {
		log.fatal("[ " + classe + " ] : [FATAL] " + message);
	}
}
