package com.example.destopinion.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.destopinion.config.R;
import com.example.destopinion.entity.Liked;
import com.example.destopinion.service.LikedService;
import com.example.destopinion.util.HashUtils;
import com.example.destopinion.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/common")
@RestController
public class CommonController {

    @Value("${opinion-service.path}")
    private String basePath;

    @Autowired
    private LikedService likedService;

    private static final String ALGORITHM = "SHA-256";

    private final KeyPair keyPair;

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
    @GetMapping("/download")
    public R<ResponseEntity<byte[]>> download(@RequestParam("hash") String hash, @RequestParam("signature") String signature, @RequestParam("image") String image) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        byte[] hashBytes = hash.getBytes();
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        byte[] imageBytes = Base64.getDecoder().decode(image);

        // 验证数字签名
        //验证hash
        boolean isValid = RSAUtils.verify(hashBytes, signatureBytes, keyPair.getPublic());
        if (!isValid) {
            throw new SignatureException("数字签名验证失败");
        }

        // 返回图片
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return R.success(new ResponseEntity<>(imageBytes, headers, HttpStatus.OK));
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
        }
        likedService.save(liked);
    }
}
