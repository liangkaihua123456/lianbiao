package com.rjwm5.rjwm5.controller;

import com.rjwm5.rjwm5.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
//     1.获取文件名
        String originalFilename = file.getOriginalFilename();
//        2.从文件名中获取文件的后缀
        int i = originalFilename.lastIndexOf(".");
        String houzhuiString = originalFilename.substring(i);
//        3.通过随机函数，生成文件名，进行拼接新的文件名
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid+houzhuiString;
//        4.指定要转存的文件夹
        File file1 = new File(basePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
//        5.判断要转存的文件夹是否存在，若不存在则生成

//        6.将文件转存到指定的文件夹的位置，并修改为全新的文件名
        try {
            file.transferTo(new File(basePath+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        注意：此处必须返回文件名称对于前端，用于文件下载使用
        return R.success(newFileName);
    }

//        this.imageUrl = `/common/download?name=${response.data}`
   @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
//        1.获取(文件)输入流
       FileInputStream fileInputStream = null;
       try {
            fileInputStream = new FileInputStream(new File(basePath + name));
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
//       2.获取输出流
       ServletOutputStream outputStream =null;
       try {
            outputStream = response.getOutputStream();
       } catch (IOException e) {
           e.printStackTrace();
       }
//       3.创建字节数组
       byte[] bytes = new byte[1024];
//       4.写while循环，将文件系统中的文件写入到字节数组中，再从字节数组中获取文件
       int len=0;
       while ((len=fileInputStream.read(bytes))!=-1){
//           将输入流中读取的文件写到输出流中
           outputStream.write(bytes,0,len);
//           刷新
           outputStream.flush();
       }
//       5.设置下载的文件的类型
       response.setContentType("image/jpeg");
//       6.关闭输入流
       fileInputStream.close();
//       7.关闭输出流
       outputStream.close();

   }
}
