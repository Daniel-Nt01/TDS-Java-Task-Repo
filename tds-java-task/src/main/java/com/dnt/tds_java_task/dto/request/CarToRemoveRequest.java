package com.dnt.tds_java_task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarToRemoveRequest(@NotBlank(message = "Vehicle registration must not be blank") 
                 @Size(min = 1, max = 8, message = "Vehicle registration must be between 1 and 8 characters") String vehicleReg) {

}
