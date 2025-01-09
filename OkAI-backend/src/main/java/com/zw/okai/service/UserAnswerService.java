package com.zw.okai.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zw.okai.model.dto.userAnswer.UserAnswerQueryRequest;
import com.zw.okai.model.entity.UserAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.okai.model.vo.UserAnswerVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 16247
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service
* @createDate 2025-01-08 14:21:32
*/
public interface UserAnswerService extends IService<UserAnswer> {

    void validUserAnswer(UserAnswer userAnswer, boolean add);

    UserAnswerVO getUserAnswerVO(UserAnswer userAnswer, HttpServletRequest request);

    Wrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest);

    Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage, HttpServletRequest request);
}
