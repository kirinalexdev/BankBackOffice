package com.kirinalex.BankBackOffice.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ErrorsUtilTest {

    @ParameterizedTest
    @MethodSource("argumentsOfGenerateErrorMessage")
    void given_ErrorList_when_GenerateErrorMessage_Then_ReturnErrorMessage(List<FieldError> errors,
                                                                              String expectedResult) {
        var result  = ErrorsUtil.generateErrorMessage(errors);
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> argumentsOfGenerateErrorMessage() {
        var arg0 = arguments(
                new ArrayList<FieldError>(),
                "");

        var arg1 = arguments(List.of(
                new FieldError("", "field1","message1")),
                "field1 - message1; ");

        var arg2 = arguments(List.of(
                new FieldError("", "field1","message1"),
                new FieldError("", "field2","message2")),
                "field1 - message1; field2 - message2; ");

        return Stream.of(arg0, arg1, arg2);

    }
}