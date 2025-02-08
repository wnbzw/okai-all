package com.zw.okai.controller;

import com.zw.okai.common.BaseResponse;
import com.zw.okai.common.ErrorCode;
import com.zw.okai.common.ResultUtils;
import com.zw.okai.exception.ThrowUtils;
import com.zw.okai.mapper.UserAnswerMapper;
import com.zw.okai.model.dto.userAnswer.AppAnswerCountDTO;
import com.zw.okai.model.dto.userAnswer.AppAnswerResultCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.getAppAnswerCount());
    }

    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.getAppAnswerResultCount(appId));
    }
}
