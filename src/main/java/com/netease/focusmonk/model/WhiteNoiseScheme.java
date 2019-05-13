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
    private Double speed;
}