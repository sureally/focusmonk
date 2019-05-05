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
     * @param elementIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int addOneScheme(WhiteNoiseScheme wns, int[] volumes, int[] elementIds) throws Exception {
        whiteNoiseSchemeMapper.insert(wns);
        int newSchemeId = wns.getId();
        insertSchemeDetails(newSchemeId, volumes, elementIds);
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
     * @param elementIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneScheme(WhiteNoiseScheme wns, int[] volumes, int[] elementIds) throws Exception {
        whiteNoiseSchemeMapper.updateByPrimaryKeySelective(wns);
        if (wns.getId() == null || wns.getId() <= 0) {
            throw new RuntimeException("更新白噪声方案，其schemeId不能为空或为非正整数");
        }
        insertSchemeDetails(wns.getId(), volumes, elementIds);
        whiteNoiseSchemeDetailService.deleteBySchemeId(wns.getId());
        log.info("更新了一个白噪声方案, " + wns.toString());
    }


    /**
     * 通过白噪声方案Id获取该方案的所有信息。
     * @param schemeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Object> getOneScheme(int schemeId) throws Exception {
        return getOneSchemeDetail(schemeId);
    }

    /**
     * 根据用户Id获取该用户所有白噪声方案及其信息详情。
     * @param userId
     * @return
     */
    public List<List<Object>> selectAllSchemeByUserId(int userId) throws Exception {
        List<WhiteNoiseScheme> schemes = whiteNoiseSchemeMapper.selectByUserId(userId);
        if (schemes == null) {
            log.warn("userId=" + userId + " 没有任何白噪声方案");
            return null;
        }
        List<List<Object>> schemeDetails = new ArrayList<>();
        // TODO: scheme 不可能为null吧
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
    private List<Object> getOneSchemeDetail(int schemeId) throws Exception{
        List<Object> schemeDetail = new ArrayList<>();
        WhiteNoiseScheme wns = whiteNoiseSchemeMapper.selectByPrimaryKey(schemeId);
        if (wns == null) {
            log.error("schemeId有误，不存在该白噪声方案");
            throw new RuntimeException("\"schemeId有误，不存在该白噪声方案\"");
        }
        schemeDetail.add(wns);
        List<WhiteNoiseSchemeDetail> wnsds = whiteNoiseSchemeDetailService.selectBySchemeId(schemeId);
        if (wnsds != null) {
            schemeDetail.addAll(wnsds);
        } else {
            log.warn("schemeId=" + schemeId + " 方案中没有任何声音元素");
        }
        return schemeDetail;
    }

    /**
     * 插入一个方案中的所有声音元素的详情
     * @param schemeId
     * @param volumes
     * @param elementIds
     */
    private void insertSchemeDetails(int schemeId, int[] volumes, int[] elementIds) {
        if (!Objects.equals(volumes.length, elementIds.length)) {
            log.error("白噪声新增方案元素详情输入错误, 声音volumes与elements长度不匹配");
            throw new IllegalArgumentException("声音volumes与elements长度不匹配");
        }

        for (int i = 0; i < elementIds.length; i++) {
            WhiteNoiseSchemeDetail wnds = new WhiteNoiseSchemeDetail();
            wnds.setElementId(elementIds[i]);
            wnds.setVolume(volumes[i]);
            wnds.setSchemeId(schemeId);
            whiteNoiseSchemeDetailService.add(wnds);
            log.info("新增一个白噪声元素详情, " + wnds.toString());
        }
    }

}
