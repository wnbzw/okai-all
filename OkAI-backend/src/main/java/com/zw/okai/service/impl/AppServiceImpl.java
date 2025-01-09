package com.zw.okai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.okai.common.ErrorCode;
import com.zw.okai.constant.CommonConstant;
import com.zw.okai.exception.BusinessException;
import com.zw.okai.exception.ThrowUtils;
import com.zw.okai.mapper.AppMapper;
import com.zw.okai.model.dto.app.AppQueryRequest;
import com.zw.okai.model.entity.App;
import com.zw.okai.model.entity.User;
import com.zw.okai.model.enums.AppScoreStrategyEnum;
import com.zw.okai.model.enums.AppTypeEnum;
import com.zw.okai.model.enums.ReviewStatusEnum;
import com.zw.okai.model.vo.AppVO;
import com.zw.okai.model.vo.UserVO;
import com.zw.okai.service.AppService;
import com.zw.okai.service.UserService;
import com.zw.okai.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 应用服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Override
    public Page<AppVO> getAppVOPage(Page<App> appPage, HttpServletRequest request) {
        List<App> appList = appPage.getRecords();
        Page<AppVO> appVOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        if (CollUtil.isEmpty(appList)) {
            return appVOPage;
        }
        List<AppVO> appVOList = appList.stream().map(app -> {
            return AppVO.objToVo(app);
        }).collect(Collectors.toList());

        //关联查询用户信息
        Set<Long> userIdSet = appVOList.stream().map(AppVO::getUserId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(userIdSet)) {
            List<User> userList = userService.listByIds(userIdSet);
            //用户id和用户信息对其
            Map<Long, List<User>> userIdUserListMap = userList.stream()
                    .collect(Collectors.groupingBy(User::getId));
            appVOList.forEach(appVO -> {
                Long userId = appVO.getUserId();
                User user = null;
                if (userIdUserListMap.containsKey(userId)) {
                    user = userIdUserListMap.get(userId).get(0);
                }
                appVO.setUser(userService.getUserVO(user));
            });
        }
        appVOPage.setRecords(appVOList);
        return appVOPage;
    }

    @Override
    public Wrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest) {
        QueryWrapper queryWrapper = new QueryWrapper<App>();
        if (appQueryRequest == null) {
            return queryWrapper;
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String appDesc = appQueryRequest.getAppDesc();
        String appIcon = appQueryRequest.getAppIcon();
        Integer appType = appQueryRequest.getAppType();
        Integer scoringStrategy = appQueryRequest.getScoringStrategy();
        Integer reviewStatus = appQueryRequest.getReviewStatus();
        String reviewMessage = appQueryRequest.getReviewMessage();
        Long reviewerId = appQueryRequest.getReviewerId();
        Long userId = appQueryRequest.getUserId();
        Long notId = appQueryRequest.getNotId();
        String searchText = appQueryRequest.getSearchText();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.apply("appName like {0} or appDesc like {0}", "%" + searchText + "%");
        }
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(appName), "appName", appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), "appDesc", appDesc);
        queryWrapper.eq(StringUtils.isNotBlank(appIcon), "appIcon", appIcon);
        queryWrapper.eq(appType != null, "appType", appType);
        queryWrapper.eq(scoringStrategy != null, "scoringStrategy", scoringStrategy);
        queryWrapper.eq(reviewStatus != null, "reviewStatus", reviewStatus);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "reviewMessage", reviewMessage);
        queryWrapper.eq(reviewerId != null, "reviewerId", reviewerId);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.ne(notId != null, "id", notId);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public AppVO getAppVO(App app, HttpServletRequest request) {

        AppVO appVO = AppVO.objToVo(app);
        Long userId = app.getUserId();
        User user=null;
        if(userId!=null&& userId >0){
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        appVO.setUser(userVO);
        return appVO;
    }

    //校验,分为添加还是修改
    @Override
    public void validApp(App app, boolean add) {
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        String appName = app.getAppName();
        String appDesc = app.getAppDesc();
        String appIcon = app.getAppIcon();
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        Integer reviewStatus = app.getReviewStatus();

        if(add){
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(appName), ErrorCode.PARAMS_ERROR, "应用名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(appDesc), ErrorCode.PARAMS_ERROR, "应用描述不能为空");
            AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
            ThrowUtils.throwIf(appTypeEnum == null, ErrorCode.PARAMS_ERROR, "应用类别非法");
            AppScoreStrategyEnum scoringStrategyEnum = AppScoreStrategyEnum.getEnumByValue(scoringStrategy);
            ThrowUtils.throwIf(scoringStrategyEnum == null, ErrorCode.PARAMS_ERROR, "应用评分策略非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(appName)) {
            ThrowUtils.throwIf(appName.length() > 80, ErrorCode.PARAMS_ERROR, "应用名称要小于 80");
        }
        if (reviewStatus != null) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            ThrowUtils.throwIf(reviewStatusEnum == null, ErrorCode.PARAMS_ERROR, "审核状态非法");
        }
    }
}
