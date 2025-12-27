package org.example.hamrogharsewa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(String msg, T data) {
        return new ApiResponseDto<>(true, msg, data);
    }

    public static <T> ApiResponseDto<T> success(String msg) {
        return new ApiResponseDto<>(true, msg, null);
    }

    public static <T> ApiResponseDto<T> error(String msg) {
        return new ApiResponseDto<>(false, msg, null);
    }
}
