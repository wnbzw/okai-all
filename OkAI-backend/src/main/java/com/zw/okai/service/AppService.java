package com.zw.okai.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.okai.model.dto.app.AppQueryRequest;
import com.zw.okai.model.entity.App;
import com.zw.okai.model.vo.AppVO;


import javax.servlet.http.HttpServletRequest;

/**
 * 应用服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
public interface AppService extends IService<App> {


    void validApp(App app, boolean b);

    AppVO getAppVO(App app, HttpServletRequest request);

    Wrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest);

    Page<AppVO> getAppVOPage(Page<App> appPage, HttpServletRequest request);
}
