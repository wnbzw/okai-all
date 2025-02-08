package com.zw.okai.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目选项列表
     */
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private String result;
        private int score;
        private String value;
        private String key;
    }

    public static String getValue(List<Option> options, String key){
        for(Option option : options){
            if(option.getKey().equals(key)){
                return option.getValue();
            }
        }
        return key;
    }
    public static int getScore(List<Option> options, String key){
        for(Option option : options){
            if(option.getKey().equals(key)){
                return option.getScore();
            }
        }
        return 0;
    }
}