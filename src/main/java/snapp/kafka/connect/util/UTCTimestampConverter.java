package snapp.kafka.connect.util;

import io.debezium.spi.converter.CustomConverter;
import io.debezium.spi.converter.RelationalColumn;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.kafka.connect.data.SchemaBuilder;

public class UTCTimestampConverter implements CustomConverter<SchemaBuilder, RelationalColumn> {

    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String LOCAL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private SimpleDateFormat utcFormatter, localFormatter;

    @Override
    public void configure(Properties props) {

        this.utcFormatter = new SimpleDateFormat(UTC_FORMAT);
        this.localFormatter = new SimpleDateFormat(LOCAL_FORMAT);

        this.utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.localFormatter.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void converterFor(RelationalColumn column, ConverterRegistration<SchemaBuilder> registration) {

        if ("TIMESTAMP".equalsIgnoreCase(column.typeName())) {
            registration.register(SchemaBuilder.string().optional(), value -> {
                
                String localTimestampStr = "";
                
                if (value == null){
                    if (column.isOptional()){
                        return null;
                    }
                    else if (column.hasDefaultValue()) {
                        return column.defaultValue();
                    }
                    else
                        return localTimestampStr;
                }

                try {
                    Date utcDate = this.utcFormatter.parse(value.toString());
                    localTimestampStr = this.localFormatter.format(utcDate);
                } catch (ParseException e) {
                    System.out.println("Exception :" + e);
                }

                return localTimestampStr;
            });
        }
    }
}