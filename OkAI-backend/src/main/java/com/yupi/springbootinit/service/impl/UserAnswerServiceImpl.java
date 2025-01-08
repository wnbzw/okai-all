package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.UserAnswer;
import com.yupi.springbootinit.service.UserAnswerService;
import com.yupi.springbootinit.mapper.UserAnswerMapper;
import org.springframework.stereotype.Service;

/**
* @author 16247
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service实现
* @createDate 2025-01-08 14:21:32
*/
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
    implements UserAnswerService{

}




