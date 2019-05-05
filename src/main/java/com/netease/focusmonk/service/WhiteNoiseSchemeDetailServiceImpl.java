package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.WhiteNoiseSchemeDetailMapper;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /** 根据白噪声方案schemeId，进行批量删除
     * @param schemeId
     */
    public void deleteBySchemeId(Integer schemeId) {
        whiteNoiseSchemeDetailMapper.deleteBySchemeId(schemeId);
        log.info("删除白噪声方案详情配置: {}", "schemeId=" + schemeId);
    }


}
