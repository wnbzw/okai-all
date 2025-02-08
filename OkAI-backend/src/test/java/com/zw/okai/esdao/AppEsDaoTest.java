//package com.zw.okai.esdao;
//
//import com.zw.okai.model.dto.app.AppEsDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//public class AppEsDaoTest {
//
//    @Resource
//    private AppEsDao appEsDao;
//
//    @Test
//    public void testAdd() {
//        AppEsDTO appEsDTO = new AppEsDTO();
//        appEsDTO.setAppName("测试应用");
//        appEsDTO.setAppDesc("测试应用描述");
//        appEsDTO.setAppType(1);
//        appEsDTO.setScoringStrategy(1);
//        appEsDTO.setUserId(1L);
//        appEsDTO.setIsDelete(0);
//        appEsDTO.setId(1L);
//        appEsDTO.setCreateTime(new Date());
//        appEsDTO.setUpdateTime(new Date());
//        appEsDao.save(appEsDTO);
//        System.out.println(appEsDTO.getId());
//    }
//
//    @Test
//    public void testFindById() {
//        Optional<AppEsDTO> byId = appEsDao.findById(1L);
//        byId.ifPresent(System.out::println);
//    }
//
//    @Test
//    public void testFindByAppName() {
//        List<AppEsDTO> byAppName = appEsDao.findByAppName("测试");
//        byAppName.forEach(System.out::println);
//    }
//}
