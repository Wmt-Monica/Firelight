package cn.dreamjun;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Classname GeneratorCodes
 * @Description TODO
 * @Date 2022/9/9 8:47
 * @Created by 翊
 */
public class GeneratorCodes {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D:\\study_project\\firelight_blog_system\\u-center\\src\\main\\java");
        gc.setAuthor("翊");
        gc.setOpen(false);//生成以后是否打开文件夹
        gc.setSwagger2(true);//实体属性 Swagger2 注解
        gc.setFileOverride(true);//true表示覆盖原来的
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://106.12.108.72:3306/firelight_user_center?characterEncoding=utf-8&useSSL=false&useUnicode=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("mengmengmeng0501");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("uc");
        pc.setParent("cn.dreamjun");
        pc.setController("api");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setEntity("pojo");
        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        //多个表的时候，写多个就可以
        strategy.setInclude("uc_role","uc_permission","uc_app_model","uc_user_role","uc_role_permission");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }

}
