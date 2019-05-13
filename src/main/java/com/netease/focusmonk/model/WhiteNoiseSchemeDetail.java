package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WhiteNoiseSchemeDetail {
    private Integer id;

    private Integer schemeId;

    private Integer elementId;

    private Double volume;
}