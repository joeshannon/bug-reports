package logback.example.plugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.net.server.ServerSocketAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.StatusListenerConfigHelper;

public class LogProducer implements IApplication {

	private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);

	private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

	@Override
	public Object start(IApplicationContext context) throws Exception {
		setupServer("localhost", 6750);
		for (int i = 0; i < 200; i++) {
			Thread.sleep(2000);
			logger.debug("A log message");
		}
		return null;
	}

	@Override
	public void stop() {
	}

	private void setupServer(String logServerHost, Integer logServerOutPort) {

		context.reset();
		StatusListenerConfigHelper.addOnConsoleListenerInstance(context, new OnConsoleStatusListener());
		context.setName("producer");
		context.start();

		ServerSocketAppender appender = new ServerSocketAppender();
		appender.setContext(context);
		// receiver.setRemoteHost(logServerHost);
		appender.setPort(logServerOutPort);
		appender.setAddress(logServerHost);
		appender.setName("server-socket");
		appender.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern("%d %-5level %logger - %m%n%rEx");
		encoder.start();

		ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<ILoggingEvent>();
		consoleAppender.setName("console");
		consoleAppender.setEncoder(encoder);
		consoleAppender.setContext(context);
		consoleAppender.start();

		ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.ALL);
		rootLogger.addAppender(appender);
		rootLogger.addAppender(consoleAppender);

		logger.info("Ready to send message to consumers");
	}

}
