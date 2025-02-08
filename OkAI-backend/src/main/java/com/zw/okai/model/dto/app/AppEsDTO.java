package com.zw.okai.model.dto.app;

import com.zw.okai.model.entity.App;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * app ES 包装类
 **/
// todo 取消注释开启 ES（须先配置 ES）
@Document(indexName = "app")
@Data
public class AppEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * app 名称
     */
    private String appName;

    /**
     * app 描述
     */
    private String appDesc;

    /**
     * app 图标
     */
    private String appIcon;


    /**
     * app 类型
     */
    private Integer appType;

    /**
     * 评分策略
     */
    private Integer scoringStrategy;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param app
     * @return
     */
    public static AppEsDTO objToDto(App app) {
        if (app == null) {
            return null;
        }
        AppEsDTO appEsDTO = new AppEsDTO();
        BeanUtils.copyProperties(app, appEsDTO);

        return appEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param appEsDTO
     * @return
     */
    public static App dtoToObj(AppEsDTO appEsDTO) {
        if (appEsDTO == null) {
            return null;
        }
        App app = new App();
        BeanUtils.copyProperties(appEsDTO, app);
        return app;
    }
}
