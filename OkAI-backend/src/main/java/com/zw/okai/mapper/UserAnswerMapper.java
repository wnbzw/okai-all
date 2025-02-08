package com.zw.okai.mapper;

import com.zw.okai.model.dto.userAnswer.AppAnswerCountDTO;
import com.zw.okai.model.dto.userAnswer.AppAnswerResultCountDTO;
import com.zw.okai.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 16247
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2025-01-08 14:21:32
* @Entity com.yupi.okai.model.entity.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    @Select(" select appId, count(userId) as answerCount from user_answer"+
        " group by appId order by answerCount desc  ")
    List<AppAnswerCountDTO> getAppAnswerCount();


    @Select(" select resultName, count(resultName) as resultCount from user_answer"+
        " where appId = #{appId} group by resultName order by resultCount desc ")
    List<AppAnswerResultCountDTO> getAppAnswerResultCount(Long appId);
}




