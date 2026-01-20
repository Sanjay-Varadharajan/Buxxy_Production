package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.geolocation;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final DatabaseReader dbReader;

    public GeoLocationService() throws IOException{
        File database=new File("src/main/resources/GeoLite2-City.mmdb");
        this.dbReader=new DatabaseReader.Builder(database).build();
    }

    public String getCounty(String ip){
        try {
            InetAddress ipAddress=InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            return response.country().isoCode();
        }
        catch (Exception e){
            return "UNKNOWN";
        }
    }

}
