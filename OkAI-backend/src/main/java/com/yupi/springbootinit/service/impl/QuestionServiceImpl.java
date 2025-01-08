package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.Question;
import com.yupi.springbootinit.service.QuestionService;
import com.yupi.springbootinit.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 16247
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-01-08 14:20:49
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




