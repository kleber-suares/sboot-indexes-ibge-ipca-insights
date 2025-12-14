package com.kls.references.indexes.ibge.ipca.insights.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberCheckerTest {

    // -------------------------
    // isNumeric
    // -------------------------

    @Test
    @DisplayName("isNumeric should return true for valid integer numbers")
    void shouldReturnTrue_When_IsNumericWithInteger() {
        assertThat(NumberChecker.isNumeric("10")).isTrue();
        assertThat(NumberChecker.isNumeric("-5")).isTrue();
    }

    @Test
    @DisplayName("isNumeric should return true for valid decimal numbers")
    void shouldReturnTrue_When_IsNumericWithDecimal() {
        assertThat(NumberChecker.isNumeric("10.5")).isTrue();
        assertThat(NumberChecker.isNumeric("-3.14")).isTrue();
    }

    @Test
    @DisplayName("isNumeric should return false for non-numeric values")
    void shouldReturnFalse_When_IsNumericWithInvalidValue() {
        assertThat(NumberChecker.isNumeric("abc")).isFalse();
        assertThat(NumberChecker.isNumeric("10a")).isFalse();
    }

    @Test
    @DisplayName("isNumeric should return false for null or blank values")
    void shouldReturnFalse_When_IsNumericWithBlankOrNull() {
        assertThat(NumberChecker.isNumeric(null)).isFalse();
        assertThat(NumberChecker.isNumeric("")).isFalse();
        assertThat(NumberChecker.isNumeric("   ")).isFalse();
    }

    // -------------------------
    // isInteger
    // -------------------------

    @Test
    @DisplayName("isInteger should return true for valid integer values")
    void shouldReturnTrue_When_IsIntegerWithValidValue() {
        assertThat(NumberChecker.isInteger("0")).isTrue();
        assertThat(NumberChecker.isInteger("123")).isTrue();
        assertThat(NumberChecker.isInteger("-42")).isTrue();
    }

    @Test
    @DisplayName("isInteger should return false for decimal numbers")
    void shouldReturnFalse_When_IsIntegerWithDecimal() {
        assertThat(NumberChecker.isInteger("10.5")).isFalse();
    }

    @Test
    @DisplayName("isInteger should return false for non-numeric values")
    void shouldReturnFalse_When_IsIntegerWithInvalidValue() {
        assertThat(NumberChecker.isInteger("abc")).isFalse();
    }

    @Test
    @DisplayName("isInteger should return false for null or blank values")
    void shouldReturnFalse_When_IsIntegerWithBlankOrNull() {
        assertThat(NumberChecker.isInteger(null)).isFalse();
        assertThat(NumberChecker.isInteger("")).isFalse();
        assertThat(NumberChecker.isInteger("   ")).isFalse();
    }

    // -------------------------
    // isDouble
    // -------------------------

    @Test
    @DisplayName("isDouble should return true for valid double values")
    void shouldReturnTrue_When_IsDoubleWithValidValue() {
        assertThat(NumberChecker.isDouble("10")).isTrue();
        assertThat(NumberChecker.isDouble("10.25")).isTrue();
        assertThat(NumberChecker.isDouble("-0.99")).isTrue();
    }

    @Test
    @DisplayName("isDouble should return false for invalid values")
    void shouldReturnFalse_When_IsDoubleWithInvalidValue() {
        assertThat(NumberChecker.isDouble("abc")).isFalse();
        assertThat(NumberChecker.isDouble("10,5")).isFalse(); // comma instead of dot
    }

    @Test
    @DisplayName("isDouble should return false for null or blank values")
    void shouldReturnFalse_When_IsDoubleWithBlankOrNull() {
        assertThat(NumberChecker.isDouble(null)).isFalse();
        assertThat(NumberChecker.isDouble("")).isFalse();
        assertThat(NumberChecker.isDouble("   ")).isFalse();
    }

    // -------------------------
    // isBigDecimal
    // -------------------------

    @Test
    @DisplayName("isBigDecimal should return true for valid BigDecimal values")
    void shouldReturnTrue_When_IsBigDecimalWithValidValue() {
        assertThat(NumberChecker.isBigDecimal("10")).isTrue();
        assertThat(NumberChecker.isBigDecimal("10.123456")).isTrue();
        assertThat(NumberChecker.isBigDecimal("-999999.999")).isTrue();
    }

    @Test
    @DisplayName("isBigDecimal should return false for invalid values")
    void shouldReturnFalse_When_IsBigDecimalWithInvalidValue() {
        assertThat(NumberChecker.isBigDecimal("abc")).isFalse();
        assertThat(NumberChecker.isBigDecimal("10,5")).isFalse();
    }

    @Test
    @DisplayName("isBigDecimal should return false for null or blank values")
    void shouldReturnFalse_When_IsBigDecimalWithBlankOrNull() {
        assertThat(NumberChecker.isBigDecimal(null)).isFalse();
        assertThat(NumberChecker.isBigDecimal("")).isFalse();
        assertThat(NumberChecker.isBigDecimal("   ")).isFalse();
    }

}

