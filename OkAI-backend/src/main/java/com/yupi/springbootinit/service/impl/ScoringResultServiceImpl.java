package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.ScoringResult;
import com.yupi.springbootinit.service.ScoringResultService;
import com.yupi.springbootinit.mapper.ScoringResultMapper;
import org.springframework.stereotype.Service;

/**
* @author 16247
* @description 针对表【scoring_result(评分结果)】的数据库操作Service实现
* @createDate 2025-01-08 14:20:49
*/
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
    implements ScoringResultService{

}




