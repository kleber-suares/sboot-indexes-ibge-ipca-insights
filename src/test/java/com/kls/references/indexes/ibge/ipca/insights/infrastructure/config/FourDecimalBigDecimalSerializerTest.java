package com.kls.references.indexes.ibge.ipca.insights.infrastructure.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;


class FourDecimalBigDecimalSerializerTest {

    private final FourDecimalBigDecimalSerializer serializer =
        new FourDecimalBigDecimalSerializer();

    @Test
    void shouldSerializeWithFourDecimalPlaces() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(new BigDecimal("1.234567"), gen, provider);

        verify(gen).writeString("1.2346");
    }

    @Test
    void shouldSerializeNullAsJsonNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(null, gen, provider);

        verify(gen).writeNull();
    }
}
