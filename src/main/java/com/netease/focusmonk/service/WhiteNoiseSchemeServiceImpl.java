package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.WhiteNoiseSchemeMapper;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.WhiteNoiseScheme;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;
import com.netease.focusmonk.model.WhiteNoiseSchemeDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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

    @Autowired
    private WhiteNoiseElementServiceImpl whiteNoiseElementService;

    /**
     * 新增一个方案
     * @param wns
     * @param volumes
     * @param elementIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int addOneScheme(WhiteNoiseScheme wns, double[] volumes, int[] elementIds) throws Exception {
        whiteNoiseSchemeMapper.insert(wns);
        int newSchemeId = wns.getId();
        insertSchemeDetails(newSchemeId, volumes, elementIds);
        log.info("新增白噪声方案: {}" + wns.toString());
        return newSchemeId;
    }

    /**
     * 删除一个方案
     * @param schemeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneScheme(int schemeId) {
        whiteNoiseSchemeDetailService.deleteBySchemeId(schemeId);
        if (whiteNoiseSchemeMapper.selectByPrimaryKey(schemeId) == null) {
            throw new ParamException("待删除白噪声方案不存在：schemeId=" + schemeId);
        }
        whiteNoiseSchemeMapper.deleteByPrimaryKey(schemeId);
        log.info("删除白噪声方案: {}", "schemeId=" + schemeId);
    }

    /**
     * 更新一个方案
     * @param wns
     * @param volumes
     * @param elementIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneScheme(WhiteNoiseScheme wns,
                                double[] volumes,
                                int[] elementIds) throws Exception {
        whiteNoiseSchemeMapper.updateByPrimaryKeySelective(wns);
        if (wns.getId() == null || wns.getId() <= 0) {
            throw new ParamException("更新白噪声方案，其schemeId不能为空或为非正整数");
        }
        // 删除该白噪声方案的所有声音详情
        whiteNoiseSchemeDetailService.deleteBySchemeId(wns.getId());
        // 插入该白噪声方案的需要更新的所有声音详情
        insertSchemeDetails(wns.getId(), volumes, elementIds);
        log.info("更新白噪音方案: {}", wns.toString());
    }


    /**
     * 通过白噪声方案Id获取该方案的所有信息。
     * @param schemeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getOneScheme(int schemeId) throws Exception {
        return getOneSchemeDetail(schemeId);
    }

    /**
     * 通过白噪声方案Id获取该方案的简单信息
     * @param schemeId
     * @return
     * @throws Exception
     */
    public WhiteNoiseScheme selectBySchemeId(int schemeId) throws Exception {
        return whiteNoiseSchemeMapper.selectByPrimaryKey(schemeId);
    }

    /**
     * 根据用户Id获取该用户所有白噪声方案及其信息详情。
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> selectAllSchemeByUserId(int userId) throws Exception {
        List<WhiteNoiseScheme> schemes = whiteNoiseSchemeMapper.selectByUserId(userId);
        if (schemes == null) {
            log.warn("warn info: userId=" + userId + " 没有任何白噪声方案");
            return null;
        }
        List<Map<String, Object>> schemeDetails = new ArrayList<>();
        // TODO: scheme 不可能为null吧
        for (WhiteNoiseScheme scheme : schemes) {
            int schemeId = scheme.getId();
            Map<String, Object> schemeDetail = getOneSchemeDetail(schemeId);
            schemeDetails.add(schemeDetail);
        }
        return schemeDetails;
    }


    /**
     * 获取一个方案的所有信息
     * @param schemeId
     * @return
     */
    private Map<String, Object> getOneSchemeDetail(int schemeId) throws Exception{
        Map<String, Object> schemeDetail = new HashMap<>();
        WhiteNoiseScheme wns = whiteNoiseSchemeMapper.selectByPrimaryKey(schemeId);
        if (wns == null) {
            throw new ParamException("schemeId有误，不存在该白噪声方案");
        }
        // 不返回userId，将所有userId置零
        wns.setUserId(0);
        schemeDetail.put("scheme", wns);

        List<WhiteNoiseSchemeDetailDTO> wnsds = whiteNoiseSchemeDetailService.selectBySchemeId2DTO(schemeId);

        if (wnsds != null) {
            schemeDetail.put("schemeDetail", wnsds);
        } else {
            log.warn("warn info: schemeId=" + schemeId + " 方案中没有任何声音元素详情配置");
        }
        return schemeDetail;
    }

    /**
     * 插入一个方案中的所有声音元素的详情
     * TODO: 未校验 volumes 中数值的大小，理论其应该在【0，1】之间
     * @param schemeId
     * @param volumes
     * @param elementIds
     */
    private void insertSchemeDetails(int schemeId, double[] volumes, int[] elementIds) {
        if (!Objects.equals(volumes.length, elementIds.length)) {
            throw new ParamException("声音volumes与elements长度不匹配");
        }

        Set<Integer> set = new HashSet<>();
        for (int i = elementIds.length - 1; i >= 0; i--) {

            // 判断elementIds中是否存在相同元素，如果存在则后者覆盖前者(抛弃前者)
            if (set.contains(elementIds[i])) {
                log.warn("新增白噪声方案schemeId=" + schemeId +
                        "中存在两个相同的elementId=" + elementIds[i] + "，后者已覆盖前者");
                continue;
            } else {
                set.add(elementIds[i]);
            }

            WhiteNoiseSchemeDetail wnds = new WhiteNoiseSchemeDetail();
            if (whiteNoiseElementService.selectByElementId(elementIds[i]) == null) {
                throw new ParamException("未在数据库中查询到声音元素elementId=" + elementIds[i]);
            }
            wnds.setElementId(elementIds[i]);
            wnds.setVolume(volumes[i]);
            wnds.setSchemeId(schemeId);
            whiteNoiseSchemeDetailService.add(wnds);
            log.info("新增白噪声方案的详情配置: {}", wnds.toString());
        }
    }

}
