package com.dnt.tds_java_task.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record ParkedCarRequest(@NotBlank(message = "Vehicle registration must not be blank")
        @Size(min = 1, max = 8, message = "Vehicle registration must be between 1 and 8 characters") String vehicleReg,
        @Min(value = 1, message = "Vehicle type must be at least 1") @Max(value = 3, message = "Vehicle type must be at most 3") int vehicleType,
        @Past(message = "Time parked must be in the past") LocalDateTime timeIn) {

    public ParkedCarRequest withTimeIn(LocalDateTime timeIn) {
        return new ParkedCarRequest(this.vehicleReg, this.vehicleType, timeIn);
    }
}
