package com.mobao360.system.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/11/15 10:57
 */
public class CodeGenerator {

    /** 数据库表名，下划线默认转驼峰*/
    private static final String Table_Name = "OTHER";//AUDIT_MERCHANT_ROUTE
    /** 数据库表名前缀,用于生成实体类时去掉对应前缀 */
    private static final String Table_Prefix = "";//BAS_
    /** 按模块中分层生成代码时，模块名*/
    private static final String Module_Name = "base";//audit base customer channel
    /** 注释中作者名字*/
    private static final String Author = "CSZ 991587100@qq.com";
    /** 生成代码的绝对路径，不包含“/src/main/java”*/
    private static final String Path = "E:/Projects/MoBaoNew/mobaopay_maxwell";
    /** 按模块中分层生成代码时，请配置项目包父路径 */
    private static final String Parent = "com.mobao360";




    /** 数据库连接信息 */
    private static final String Url = "jdbc:oracle:thin:@//192.168.20.1:1521/mobopay";
    private static final String DriverName = "oracle.jdbc.OracleDriver";
    private static final String Username = "MOBAO_PAYMENT";
    private static final String Password = "mobao360#2";


    /** 按层中分模块生成代码时，请修改下列路径 */
    private static final String Entity = "com.mobao360.entity.account.merchant";
    private static final String Mapper = "com.mobao360.mapper.account.merchant";
    private static final String MapperXml = "com.mobao360.mapper.account.merchant.xml";
    private static final String Service = "com.mobao360.service.account.merchant";
    private static final String ServiceImpl = "com.mobao360.service.account.merchant.impl";
    private static final String Controller = "com.mobao360.controller.account.merchant";


    public static void main(String[] args) {
        /**
         * 生成代码的模式
         * 0-模块中分层
         * 1-层中分模块
         */
        autoCode(0);
    }



    public static void autoCode(int way) {

        AutoGenerator mpg = new AutoGenerator();

        /**
         * 全局配置
         */
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(Path + "/src/main/java");
        gc.setAuthor(Author);
        gc.setOpen(false);
//        gc.setIdType(IdType.AUTO);
        gc.setIdType(IdType.INPUT);//oracle使用序列
        mpg.setGlobalConfig(gc);

        /**
         * 数据源配置
         */
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(Url);
        // dsc.setSchemaName("public");
        dsc.setDriverName(DriverName);
        dsc.setUsername(Username);
        dsc.setPassword(Password);
        mpg.setDataSource(dsc);

        /**
         * 包配置
         */
        PackageConfig pc = new PackageConfig();
        //方式一：按模块生成
        if(way == 0){
            pc.setModuleName(Module_Name);
            pc.setParent(Parent);
        }else {
            //方式二：按层生成
            pc.setParent(null);
            pc.setEntity(Entity);
            pc.setMapper(Mapper);// Dao接口包名
            pc.setXml(MapperXml);// XML包名
            pc.setService(Service);
            pc.setServiceImpl(ServiceImpl);
            pc.setController(Controller);
        }
        mpg.setPackageInfo(pc);

        /**
         * 自定义配置
         */
        // 需要可参见官网


        /**
         * 策略配置
         */
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        //自定义controller模板后，SuperController就不起作用了，可以在模板中直接写明父类信息
        //strategy.setSuperControllerClass("com.mobao360.controller.BaseController");
        strategy.setInclude(Table_Name);
        //strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(Table_Prefix);
        mpg.setStrategy(strategy);
        //需要配置模板引擎，省略不写就是配置velocity，需要在pom中引入对应的maven依赖
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
