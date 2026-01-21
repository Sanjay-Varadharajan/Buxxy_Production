package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.geolocation;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoConfig {

    @Bean
    public DatabaseReader databaseReader() throws IOException{
            InputStream database=new ClassPathResource("geoip/GeoLite2-City.mmdb").getInputStream();
            return new DatabaseReader.Builder(database).build();
    }

}
