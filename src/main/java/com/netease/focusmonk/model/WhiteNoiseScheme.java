package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class WhiteNoiseScheme {
    private Integer id;

    @Min(1)
    private Integer userId;

    @NotBlank(message = "白噪声方案名不能为空")
    private String name;

    @NotNull(message = "变速值不能为空")
    @Min(value = 0, message = "变速值不能小雨0")
    @Max(value = 100, message = "变速值最大为100")
    private Integer speed;
}