package spring.itemproject.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import spring.itemproject.typeconverter.type.IpPort;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("converter source={}", source);
        // "127.0.0.1:8080"
        String[] split = source.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);

        return new IpPort(ip, port);
    }
}
