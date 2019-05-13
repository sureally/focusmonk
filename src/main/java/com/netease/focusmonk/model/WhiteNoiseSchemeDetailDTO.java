package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName com.netease.focusmonk.model.WhiteNoiseSchemeDetailDTO
 * @Desciption 返回前端中的WhiteNoiseSchemeDetail，增加了元素name，但是这个增加了服务器的压力。
 * @Author Shu WJ
 * @DateTime 2019-05-13 14:56
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WhiteNoiseSchemeDetailDTO {
    private Integer id;

    private Integer schemeId;

    private Integer elementId;

    private String elementName;

    private Double volume;
}
