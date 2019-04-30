package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.WhiteNoiseSchemeMapper;
import com.netease.focusmonk.model.WhiteNoiseScheme;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName com.netease.focusmonk.service.WhiteNoiseSchemeServiceImpl
 * @Desciption 白噪声方案
 * @Author Shu WJ
 * @DateTime 2019-04-29 17:26
 * @Version 1.0
 **/
@Slf4j
@Service
public class WhiteNoiseSchemeServiceImpl {
    @Autowired
    private WhiteNoiseSchemeMapper whiteNoiseSchemeMapper;

    @Autowired
    private WhiteNoiseSchemeDetailServiceImpl whiteNoiseSchemeDetailService;

    /**
     * 新增一个方案
     * @param wns
     * @param volumes
     * @param elements
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int addOneScheme(WhiteNoiseScheme wns, int[] volumes, int[] elements) throws InterruptedException {
        whiteNoiseSchemeMapper.insert(wns);
        int newSchemeId = wns.getId();
        insertSchemeDetails(newSchemeId, volumes, elements);
        log.info("新增了一个方案," + wns.toString());
        return newSchemeId;
    }

    /**
     * 删除一个方案
     * @param schemeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneScheme(int schemeId) {
        whiteNoiseSchemeDetailService.deleteBySchemeId(schemeId);
        whiteNoiseSchemeMapper.deleteByPrimaryKey(schemeId);
        log.info("删除了一个方案, schemeId=" + schemeId);
    }

    /**
     * 更新一个方案
     * @param wns
     * @param volumes
     * @param elements
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneScheme(WhiteNoiseScheme wns, int[] volumes, int[] elements) throws IllegalAccessException{
        whiteNoiseSchemeMapper.updateByPrimaryKeySelective(wns);
        int schemeId = wns.getId();
        insertSchemeDetails(schemeId, volumes, elements);
        whiteNoiseSchemeDetailService.deleteBySchemeId(schemeId);
        log.info("更新了一个白噪声方案, " + wns.toString());
    }


    /**
     * 通过白噪声方案Id获取该方案的所有信息。
     * @param schemeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getOneScheme(int schemeId) {
        return getOneSchemeDetail(schemeId);
    }

    /**
     * 根据用户Id获取该用户所有白噪声方案及其信息详情。
     * @param userId
     * @return
     */
    public List<List<Object>> selectAllSchemeByUserId(int userId) {
        List<WhiteNoiseScheme> schemes = whiteNoiseSchemeMapper.selectByUserId(userId);
        List<List<Object>> schemeDetails = new ArrayList<>();
        for (WhiteNoiseScheme scheme : schemes) {
            int schemeId = scheme.getId();
            List<Object> schemeDetail = getOneSchemeDetail(schemeId);
            schemeDetails.add(schemeDetail);
        }
        return schemeDetails;
    }


    /**
     * 获取一个方案的所有信息
     * @param schemeId
     * @return
     */
    private List<Object> getOneSchemeDetail(int schemeId) {
        List<Object> schemeDetail = new ArrayList<>();
        WhiteNoiseScheme wns = whiteNoiseSchemeMapper.selectByPrimaryKey(schemeId);
        schemeDetail.add(wns);
        List<WhiteNoiseSchemeDetail> wnsds = whiteNoiseSchemeDetailService.selectBySchemeId(schemeId);
        schemeDetail.addAll(wnsds);
        return schemeDetail;
    }

    /**
     * 插入一个方案中的所有声音元素的详情
     * @param schemeId
     * @param volumes
     * @param elements
     */
    private void insertSchemeDetails(int schemeId, int[] volumes, int[] elements) {
        if (!Objects.equals(volumes.length, elements.length)) {
            log.error("白噪声新增方案元素详情输入错误, 声音volumes与elements长度不匹配");
            throw new IllegalArgumentException("声音volumes与elements长度不匹配");
        }

        for (int i = 0; i < elements.length; i++) {
            WhiteNoiseSchemeDetail wnds = new WhiteNoiseSchemeDetail();
            wnds.setElementId(elements[i]);
            wnds.setVolume(volumes[i]);
            wnds.setSchemeId(schemeId);
            whiteNoiseSchemeDetailService.add(wnds);
            log.info("新增一个白噪声元素详情, " + wnds.toString());
        }
    }

}
