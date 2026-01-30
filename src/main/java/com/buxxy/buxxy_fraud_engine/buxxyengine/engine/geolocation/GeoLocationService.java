    package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.geolocation;

    import com.maxmind.geoip2.DatabaseReader;
    import com.maxmind.geoip2.model.CityResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;


    import java.net.InetAddress;
    import java.util.Map;

    @Service
    @RequiredArgsConstructor
    public class GeoLocationService {


        private final DatabaseReader dbReader;

        public String getCountry(String ip){
            try {
                InetAddress ipAddress=InetAddress.getByName(ip);
                CityResponse response = dbReader.city(ipAddress);
                return response.country().isoCode();
            }
            catch (Exception e){
                return "UNKNOWN";
            }
        }
        public String getCity(String ip){
            try {
                InetAddress inetAddress=InetAddress.getByName(ip);
                CityResponse response=dbReader.city(inetAddress);
                String city=response.city().name();
                return city != null ? city : "UNKNOWN";
            } catch (Exception e) {
                return "UNKNOWN";
            }
        }

        public double[] getLatitudeAndLongitude(String ip){
            try {
                InetAddress inetAddress = InetAddress.getByName(ip);
                CityResponse cityResponse = dbReader.city(inetAddress);
                if (cityResponse.location() != null) {
                    double latitude = cityResponse.location().latitude();
                    double longitude = cityResponse.location().longitude();
                    return new double[]{latitude, latitude};
                }
            }
                catch(Exception e){
                    System.err.print(e.getStackTrace());
                }
            return null;
        }
        }
