package cn.com.pax.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RecordLog {
    public static Logger Logger() {
        File logbackFile = new File("./config/logback.xml");
        if(logbackFile.exists()) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(logbackFile);
            } catch (JoranException e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
            Logger logger = lc.getLogger(RecordLog.class);
            return logger;
        }
        throw new NullPointerException();
    }
}
