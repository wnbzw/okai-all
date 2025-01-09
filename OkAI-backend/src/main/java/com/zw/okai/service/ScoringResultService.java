package com.zw.okai.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zw.okai.model.dto.scoringResult.ScoringResultQueryRequest;
import com.zw.okai.model.entity.ScoringResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.okai.model.vo.ScoringResultVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 16247
* @description 针对表【scoring_result(评分结果)】的数据库操作Service
* @createDate 2025-01-08 14:20:49
*/
public interface ScoringResultService extends IService<ScoringResult> {

    void validScoringResult(ScoringResult scoringResult, boolean b);

    ScoringResultVO getScoringResultVO(ScoringResult scoringResult, HttpServletRequest request);

    Wrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest);

    Page<ScoringResultVO> getScoringResultVOPage(Page<ScoringResult> scoringResultPage, HttpServletRequest request);
}
