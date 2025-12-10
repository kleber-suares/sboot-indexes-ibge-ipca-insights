package com.kls.references.indexes.ibge.ipca.insights.infrastructure.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FourDecimalBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');

        DecimalFormat formatter = new DecimalFormat("0.0000", symbols);

        if (value != null) {
            BigDecimal roundedValue = value.setScale(4, RoundingMode.HALF_UP);
            gen.writeString(formatter.format(roundedValue));
        } else {
            gen.writeNull();
        }

    }
}
