package com.zw.okai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.okai.common.ErrorCode;
import com.zw.okai.constant.CommonConstant;
import com.zw.okai.exception.ThrowUtils;
import com.zw.okai.model.dto.question.QuestionQueryRequest;
import com.zw.okai.model.entity.App;
import com.zw.okai.model.entity.Question;
import com.zw.okai.model.entity.User;
import com.zw.okai.model.vo.QuestionVO;
import com.zw.okai.model.vo.UserVO;
import com.zw.okai.service.AppService;
import com.zw.okai.service.QuestionService;
import com.zw.okai.mapper.QuestionMapper;
import com.zw.okai.service.UserService;
import com.zw.okai.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 16247
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-01-08 14:20:49
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;


    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();

        Page<QuestionVO> questionVOPage=new Page<>(questionPage.getCurrent(), questionPage.getSize(),questionPage.getTotal());
        if(CollUtil.isEmpty(questionList)){
            return questionVOPage;
        }
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            return QuestionVO.objToVo(question);
        }).collect(Collectors.toList());

        //关联查询用户信息
        Set<Long> userIdList = questionVOList.stream().map(QuestionVO::getUserId).collect(Collectors.toSet());
        if(CollUtil.isNotEmpty(userIdList)){
            List<User> userList = userService.listByIds(userIdList);
            Map<Long, List<User>> collect =
                    userList.stream().collect(Collectors.groupingBy(User::getId));
            questionVOList.forEach(questionVO -> {
                User user = null;
                Long userId= questionVO.getUserId();
                if(collect.containsKey(userId)){
                    user = collect.get(userId).get(0);
                }
                questionVO.setUser(userService.getUserVO(user));
            });
        }
        questionVOPage.setRecords(questionVOList);

//        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
//            return getQuestionVO(question, request);
//        }).collect(Collectors.toList());
        return questionVOPage;
    }

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionQueryRequest.getId();
        String questionContent = questionQueryRequest.getQuestionContent();
        Long appId = questionQueryRequest.getAppId();
        Long userId = questionQueryRequest.getUserId();
        Long notId = questionQueryRequest.getNotId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "questionContent", questionContent);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        Long userId= question.getUserId();
        UserVO userVO = null;
        if(userId != null&& userId > 0){
            userVO = userService.getUserVO(userService.getById(userId));
        }
        questionVO.setUser(userVO);
        return questionVO;
    }

    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String questionContent = question.getQuestionContent();
        Long appId = question.getAppId();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(questionContent), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
            ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (appId != null) {
            App app = appService.getById(appId);
            ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        }
    }
}




