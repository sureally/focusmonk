package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.WhiteNoiseElementMapper;
import com.netease.focusmonk.model.WhiteNoiseElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName com.netease.focusmonk.service.WhiteNoiseElementServiceImpl
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-04-29 15:19
 * @Version 1.0
 **/
@Service
public class WhiteNoiseElementServiceImpl {

    @Autowired
    private WhiteNoiseElementMapper whiteNoiseElementMapper;

    /**
     * 返回数据库中所有白噪声元素
     * @return
     */
    public List<WhiteNoiseElement> listAll() {
        return whiteNoiseElementMapper.listAll();
    }
}
