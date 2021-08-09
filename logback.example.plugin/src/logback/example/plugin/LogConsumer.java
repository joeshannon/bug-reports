package logback.example.plugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.net.SocketReceiver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.StatusListenerConfigHelper;

public class LogConsumer implements IApplication {

	private static final Logger logger = LoggerFactory.getLogger(LogConsumer.class);

	private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

	@Override
	public Object start(IApplicationContext context) throws Exception {
		setupReceiver("localhost", 6750);
		for (int i = 0; i < 200; i++) {
			Thread.sleep(2000);
		}
		return null;
	}

	@Override
	public void stop() {
	}

	private void setupReceiver(String logServerHost, Integer logServerOutPort) {

		context.reset();
		StatusListenerConfigHelper.addOnConsoleListenerInstance(context, new OnConsoleStatusListener());
		context.setName("producer");
		context.start();

		SocketReceiver receiver = new SocketReceiver();
		receiver.setContext(context);
		receiver.setRemoteHost(logServerHost);
		receiver.setPort(logServerOutPort);
		receiver.setReconnectionDelay(1000);
		receiver.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern("%d %-5level %logger - %m%n%rEx");
		encoder.start();

		ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
		appender.setContext(context);
		appender.setEncoder(encoder);
		appender.start();

		ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.ALL);
		rootLogger.addAppender(appender);
		logger.info("Listening for events");
	}

}
