/*
 * Copyright (c) 2011-2019, hubin (jobob@qq.com). <p> Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at <p>
 * https://www.apache.org/licenses/LICENSE-2.0 <p> Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.example.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.mysql.jdbc.Driver;

/**
 * 代码生成器 示例
 *
 * @author K神
 * @since 2017/12/29
 */
class CodeGeneratorWithTemplateTest {

    @Test
    void generateCode() {
        String packageName = "com.example.demo";
        generateByTables(packageName, "todo");

    }

    private void generateByTables(String packageName, String... tableNames) {
        String projectPath = System.getProperty("user.dir") + "/target/codeGenerator";
        File fProjectPath = new File(projectPath);
        if (fProjectPath.exists()) {
            FileSystemUtils.deleteRecursively(fProjectPath);
        }
        fProjectPath.mkdirs();

        String dbUrl = "jdbc:mysql://localhost:3306/plain_a?useUnicode=true&useSSL=false&characterEncoding=utf8";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL).setUrl(dbUrl).setUsername("root").setPassword("abcDEF123___")
            .setDriverName(Driver.class.getName());
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true).setEntityLombokModel(false).setNaming(NamingStrategy.underline_to_camel)
            .setColumnNaming(NamingStrategy.underline_to_camel).setEntityTableFieldAnnotationEnable(true)
            .setFieldPrefix(null)// test_id ->
                                 // id, test_type
                                 // -> type
            .setInclude(tableNames);// 修改替换成你需要的表名，多个表名传数组

        GlobalConfig config = new GlobalConfig();
        config.setOutputDir(projectPath + "/src/main/java").setFileOverride(true).setOpen(true).setEnableCache(false)
            .setAuthor("yeqiang").setSwagger2(true).setActiveRecord(false).setBaseResultMap(true)
            // 对应MySQL LocalDateTime
            .setDateType(DateType.TIME_PACK).setBaseColumnList(true).setEntityName("%s").setMapperName("%sMapper")
            .setServiceName("%sService").setServiceImplName("%sServiceImpl").setControllerName("%sController");

        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setXml("templates/mapper.xml");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setController("templates/controller.java");

        InjectionConfig injectionConfig = new InjectionConfig() {
            // 自定义属性注入:abc
            // 在.vm/ftl模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 freemarker
        String templatePath = "templates/mapper.xml.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
                    + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);

        new AutoGenerator().setGlobalConfig(config).setDataSource(dataSourceConfig).setStrategy(strategyConfig)
            // 配置自定义模板
            .setTemplate(templateConfig)
            // 配置自定义属性注入
            .setCfg(injectionConfig)
            .setPackageInfo(new PackageConfig().setParent(packageName).setController("controller").setEntity("entity"))
            .setTemplateEngine(new FreemarkerTemplateEngine()).execute();
    }
}
