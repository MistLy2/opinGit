package com.example.destopinion.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.destopinion.config.CustomException;
import com.example.destopinion.config.R;
import com.example.destopinion.conn.Tess4jClient;
import com.example.destopinion.entity.Liked;
import com.example.destopinion.entity.Opinion;
import com.example.destopinion.entity.Reson;
import com.example.destopinion.service.LikedService;
import com.example.destopinion.service.OpinionService;
import com.example.destopinion.util.HashUtils;
import com.example.destopinion.util.RSAUtils;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/common")
@RestController
public class CommonController {

//    @Value("${opinion-service.path}")
//    private String basePath;

    @Autowired
    private LikedService likedService;

    private static final String ALGORITHM = "SHA-256";

    private final KeyPair keyPair;

    @Autowired
    private OpinionService opinionService;

    public CommonController() throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyPair = RSAUtils.generateKeyPair();//拿出来密钥对
    }

    @PostMapping("/upload")
    public R<Map<String,Object>> upload(MultipartFile file) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        byte[] fileBytes = file.getBytes();
        String hash = HashUtils.sha256(fileBytes.toString());
        byte[] signature = RSAUtils.encrypt(hash.getBytes(), keyPair.getPublic());//rsa加密

//        //这里的file是一个临时文件，需要转存到指定位置
//        String originalFilename = file.getOriginalFilename();
//        // System.out.println(originalFilename);
//        String s = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        String fileName= UUID.randomUUID().toString()+s;
//        //System.out.println(fileName);
//        //这里还要创建目录
//        File dir=new File(basePath);
//
//        if(!dir.exists()){
//            dir.mkdirs();//没有就创建
//        }
//        try {
//            file.transferTo(new File(basePath+fileName));
//            //System.out.println(basePath+fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Map<String, Object> result = new HashMap<>();
        result.put("hash", hash);
        result.put("signature", Base64.getEncoder().encodeToString(signature));
        result.put("image", Base64.getEncoder().encodeToString(fileBytes));

        //返回hash ， 签名 ， image
        return R.success(result);//这里要返回文件名称
    }

    @GetMapping("/getPublic")
    public R<String> getPublic(){
        PublicKey aPublic = keyPair.getPublic();

        byte[] publicKey = Base64.getEncoder().encode(aPublic.getEncoded());
        return R.success(new String(publicKey));
    }

    //文件下载，将文件图片发送到前端进行展示
//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response) {
//        try {
//            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
//
//            ServletOutputStream outputStream=response.getOutputStream();
//
//            //设置文件写回格式
//            response.setContentType("image/jpeg");
//            //然后写回页面
//            int len=0;
//            byte[] bytes=new byte[1024];
//            while((len=fileInputStream.read(bytes))!=-1){
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
//
//            //写完之后关闭流
//            fileInputStream.close();
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //数据库模糊匹配查找舆论消息
    @GetMapping("/search/{text}")
    public R<List<Opinion>> search(@PathVariable String text){
        //数据库中进行模糊匹配
        if(text == null){
            return R.error("查询条件不能为空");
        }
        LambdaQueryWrapper<Opinion> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(Opinion::getTitle,text);
        List<Opinion> list = opinionService.list(wrapper);

        return R.success(list);
    }
    //图片文字提取分析并查询数据库返回结果
    @PostMapping("/analyse")
    public R<List<Opinion>> analyse(@RequestParam("file") MultipartFile image){
        String result=null;

        byte[] bytes;
        try {
            bytes = image.getBytes();
            System.out.println("进来了");
        } catch (IllegalArgumentException | IOException e) {
            return R.error("Invalid file");
        }

        //从byte[]转换为butteredImage
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BufferedImage imageFile = null;

        try {
            imageFile = ImageIO.read(in);
        } catch (IOException e) {
            return R.error("Failed to read image file");
        }
        try {
            result = Tess4jClient.doOCR(imageFile);
        } catch (TesseractException e) {
            return R.error("Failed to perform OCR on image");
        }
        //查询后返回
        //System.out.println(result);
        //对这个result进行处理
        String regex = "[^\u4e00-\u9fa5]";
        result = result.replaceAll(regex,"");
        return search(result.substring(0,2));
    }

    @PostMapping("/download")
    public R<Reson> download(String hash,String signature, MultipartFile image) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        byte[] hashBytes = hash.getBytes();
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        //byte[] imageBytes = Base64.getDecoder().decode(image);

        // 验证数字签名
        //验证hash
        boolean isValid = RSAUtils.verify(hashBytes, signatureBytes, keyPair.getPublic());
        if (!isValid) {
            return R.error("数字签名验证失败");
        }

        R<List<Opinion>> r = analyse(image);

        // 返回图片
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return R.success(new Reson(new ResponseEntity<>(image.getBytes(), headers, HttpStatus.OK),r.getData()));
    }

    //kafka监听点赞信息，存入数据库
    @KafkaListener(topics = "likes")
    public void likes(List<String> list){
        Long userId = Long.parseLong(list.get(0));
        Long opinionId = Long.parseLong(list.get(1));
        int status = Integer.parseInt(list.get(2));

        Liked liked = new Liked();
        liked.setUserId(userId);
        liked.setOpinionId(opinionId);
        liked.setStatus(status);//点赞或者取消点赞

        LambdaQueryWrapper<Liked> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Liked::getUserId,userId)
                        .eq(Liked::getOpinionId,opinionId);
        Liked one = likedService.getOne(wrapper);
        if(one != null){
            likedService.updateById(one);
            return;
        }
        likedService.save(liked);
    }
}
