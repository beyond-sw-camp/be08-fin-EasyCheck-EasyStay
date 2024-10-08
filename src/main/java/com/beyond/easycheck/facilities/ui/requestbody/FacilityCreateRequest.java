package com.beyond.easycheck.facilities.ui.requestbody;

import com.beyond.easycheck.facilities.infrastructure.entity.AvailableStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
public class FacilityCreateRequest {

    @NotNull
    private Long accommodationId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    private AvailableStatus availableStatus;
}
