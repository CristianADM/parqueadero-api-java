package com.parqueadero.app.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.parqueadero.app.dtos.responses.ErrorDetailResponse;
import com.parqueadero.app.dtos.responses.ErrorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UnauthorizedException extends RuntimeException{
private final transient ErrorResponse errorResponse;

    public UnauthorizedException(String error, String message) {
        List<ErrorDetailResponse> errorDetail = new ArrayList<>();
        errorDetail.add(new ErrorDetailResponse(error, message));
        this.errorResponse = new ErrorResponse(errorDetail);
    }
}
