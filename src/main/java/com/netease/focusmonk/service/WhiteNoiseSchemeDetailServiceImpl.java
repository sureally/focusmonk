package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.WhiteNoiseSchemeDetailMapper;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.WhiteNoiseElement;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName com.netease.focusmonk.service.WhiteNoiseSchemeDetailServiceImpl
 * @Desciption 白噪声方案中各个组成声音元素详情
 * @Author Shu WJ
 * @DateTime 2019-04-29 20:57
 * @Version 1.0
 **/
@Slf4j
@Service
public class WhiteNoiseSchemeDetailServiceImpl {

    @Autowired
    private WhiteNoiseSchemeDetailMapper whiteNoiseSchemeDetailMapper;

    @Autowired
    private WhiteNoiseElementServiceImpl whiteNoiseElementService;

    public void add(WhiteNoiseSchemeDetail wnsd) {
        whiteNoiseSchemeDetailMapper.insert(wnsd);
    }

    /**
     * 根据方案schemeId查询该方案中所有声音元素的详情
     * @param schemeId
     * @return
     */
    public List<WhiteNoiseSchemeDetail> selectBySchemeId(Integer schemeId) {
        return whiteNoiseSchemeDetailMapper.selectBySchemeId(schemeId);
    }

    /**
     * 根据方案schemeId查询该方案中所有声音元素的详情,并转换为DTO输出;
     * TODO: 优化方式，可以前端遍历实现，避免服务器查询的压力。
     * @param schemeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<WhiteNoiseSchemeDetailDTO> selectBySchemeId2DTO(Integer schemeId) throws Exception{
        List<WhiteNoiseSchemeDetail> details = whiteNoiseSchemeDetailMapper.selectBySchemeId(schemeId);
        List<WhiteNoiseSchemeDetailDTO> detailDTOs = new ArrayList<>();
        for (WhiteNoiseSchemeDetail detail : details) {
            WhiteNoiseSchemeDetailDTO detailDTO = new WhiteNoiseSchemeDetailDTO();
            detailDTO.setElementId(detail.getElementId());
            detailDTO.setId(detail.getId());
            detailDTO.setSchemeId(detail.getSchemeId());
            detailDTO.setVolume(detail.getVolume());
            WhiteNoiseElement whiteNoiseElement = whiteNoiseElementService.selectByElementId(detail.getElementId());
            if (whiteNoiseElement == null) {
                throw new ParamException("schemeId=" +
                        schemeId + "中的元素elementId=" + detail.getElementId() + "在 WhiteNoiseElement不存在" );
            }
            detailDTO.setElementName(whiteNoiseElement.getName());
            detailDTOs.add(detailDTO);
        }
        return detailDTOs;
    }

    /** 根据白噪声方案schemeId，进行声音配置详情的批量删除
     * @param schemeId
     */
    public void deleteBySchemeId(Integer schemeId) {
        // 因为是批量删除，不排除排除的声音元素配置中的元素实际在数据库中不存在的情景
        whiteNoiseSchemeDetailMapper.deleteBySchemeId(schemeId);
        log.info("删除白噪声方案详情配置: {}", "schemeId=" + schemeId);
    }


}
