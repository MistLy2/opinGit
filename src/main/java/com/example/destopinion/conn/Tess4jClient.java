package com.example.destopinion.conn;

import lombok.Getter;
import lombok.Setter;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Getter
@Setter
@Component
@EnableRedisRepositories
@ConfigurationProperties(prefix = "tess4j")
public class Tess4jClient {

    static{
        System.setProperty("TESSDATA_PREFIX", "src/main/resources/tessData");
    }
    private static String dataPath = "src/main/resources/tessData";
    private static String language = "chi_sim";
//usr/share/tesseract-ocr/tessdata
    // 入参：图片流
    public static String doOCR(BufferedImage image) throws TesseractException {
        //创建Tesseract对象


        ITesseract tesseract = new Tesseract();
        System.out.println(tesseract);
        //设置中文字体库路径
        tesseract.setDatapath(dataPath);
        //中文识别
        tesseract.setLanguage(language);
        //System.out.println(dataPath);
        //System.out.println(image);
        //执行ocr识别
        String result = tesseract.doOCR(image);
        //System.out.println(result);
        //替换回车和tal键  使结果为一行
        result = result.replaceAll("\\r|\\n", "-").replaceAll(" ", "");
        return result;
    }
}

