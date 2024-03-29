package com.wcy.wmall;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Generator
 * @Description 生产MBG代码
 * @Author wcy
 * @Date 2019-07-29 10:04
 * @Version 1.0
 **/
public class Generator {
    public static void main(String[] args) throws Exception{
        //MBG执行过程中的警告信息
        List<String> warnings = new ArrayList<String>();

        //生成代码重复的时候，覆盖原代码
        boolean overwrite = true;

        InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp =new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback  = new DefaultShellCallback(overwrite);
        //创建MBG
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback,warnings);
        //执行生成代码
        myBatisGenerator.generate(null);

        //输出警告信息
        for(String warning : warnings){
            System.out.println(warning);
        }


    }
}
