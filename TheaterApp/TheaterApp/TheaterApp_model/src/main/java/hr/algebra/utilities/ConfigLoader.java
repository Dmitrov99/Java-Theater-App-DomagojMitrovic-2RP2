package hr.algebra.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigLoader {
    private static final String PROPERTIES_FILE="db.properties";
    private static final Logger log=LoggerFactory.getLogger(ConfigLoader.class);


    private final Properties fileProperties;

    public ConfigLoader() {
        this.fileProperties = loadPropertiesFile();
    }

    private Properties loadPropertiesFile() {
        Properties props=new Properties();
        try (InputStream is=getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            props.load(is);
            log.info("File '{}' loaded successfully('{} keys')",PROPERTIES_FILE,props.size());
        } catch (IOException e) {//dodaj logger
            throw new RuntimeException(e);
        }

        return props;
    }
}
