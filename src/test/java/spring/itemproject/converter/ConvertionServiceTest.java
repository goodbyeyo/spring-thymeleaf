package spring.itemproject.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import spring.itemproject.typeconverter.converter.IntegerToStringConverter;
import spring.itemproject.typeconverter.converter.IpPortToStringConverter;
import spring.itemproject.typeconverter.converter.StringToIntegerConverter;
import spring.itemproject.typeconverter.converter.StringToIpPortConverter;
import spring.itemproject.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.*;

public class ConvertionServiceTest {

    @Test
    void conversionService() {

        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();

        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 사용
        Integer result = conversionService.convert("10", Integer.class);
        System.out.println("result = " + result);

        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
    }
}
