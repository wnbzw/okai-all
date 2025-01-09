package com.zw.okai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zw.okai.model.dto.question.QuestionQueryRequest;
import com.zw.okai.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.okai.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 16247
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-01-08 14:20:49
*/
public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean b);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
