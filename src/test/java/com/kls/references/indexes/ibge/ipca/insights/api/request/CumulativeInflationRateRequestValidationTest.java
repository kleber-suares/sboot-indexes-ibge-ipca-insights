package com.kls.references.indexes.ibge.ipca.insights.api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CumulativeInflationRateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateSuccessfullyWhenAllFieldsArePresent() {
        var req = new CumulativeInflationRateRequest(
            2020, 1,
            2021, 12
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailWhenStartYearIsNull() {
        var req = new CumulativeInflationRateRequest(
            null, 1,
            2021, 12
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations)
            .hasSize(1)
            .anyMatch(v -> v.getPropertyPath().toString().equals("startYear"));
    }

    @Test
    void shouldFailWhenStartMonthIsNull() {
        var req = new CumulativeInflationRateRequest(
            2020, null,
            2021, 12
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations)
            .hasSize(1)
            .anyMatch(v -> v.getPropertyPath().toString().equals("startMonth"));
    }

    @Test
    void shouldFailWhenEndYearIsNull() {
        var req = new CumulativeInflationRateRequest(
            2020, 1,
            null, 12
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations)
            .hasSize(1)
            .anyMatch(v -> v.getPropertyPath().toString().equals("endYear"));
    }

    @Test
    void shouldFailWhenEndMonthIsNull() {
        var req = new CumulativeInflationRateRequest(
            2020, 1,
            2021, null
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations)
            .hasSize(1)
            .anyMatch(v -> v.getPropertyPath().toString().equals("endMonth"));
    }

    @Test
    void shouldFailWhenMultipleFieldsAreNull() {
        var req = new CumulativeInflationRateRequest(
            null, null,
            null, null
        );

        Set<ConstraintViolation<CumulativeInflationRateRequest>> violations =
            validator.validate(req);

        assertThat(violations).hasSize(4);
    }
}
