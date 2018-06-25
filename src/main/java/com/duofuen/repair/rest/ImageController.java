package com.duofuen.repair.rest;

import com.duofuen.repair.domain.Image;
import com.duofuen.repair.domain.ImageRepository;
import com.duofuen.repair.util.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Optional;

import static com.duofuen.repair.util.Const.Rest.ROOT_PATH;

@RestController
@RequestMapping(path = ROOT_PATH)
public class ImageController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping(value = "/viewImage")
    public void viewImage(Integer id, HttpServletResponse response) throws IOException {
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isPresent()) {
            response.setStatus(404);
            response.setContentType("text/html;charset=utf8");
            response.getWriter().append("No Image Found");
            return;
        }
        byte[] data = image.get().getImageContent();

        response.setContentType("image/" + image.get().getImageType());
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }

    @PostMapping(value = "/uploadImage")
    public BaseResponse<RbImage> uploadImage(@RequestBody Map<String, String> map) {
        LOGGER.info("==>restful method uploadImage called");

        BaseResponse<RbImage> baseResponse;

        String imgEncode = map.get(Const.Rest.IMG);
        if (imgEncode.isEmpty()) {
            baseResponse = BaseResponse.fail("img is empty");
        } else {
            LOGGER.info("image size {}", imgEncode.length());

            BASE64Decoder decoder = new BASE64Decoder();
            try {
                //判断图片的格式
                String data = "data:image";
                int sIndex = imgEncode.indexOf(data);
                String chanImgStr = imgEncode.substring(sIndex + data.length());
                int eIndex = chanImgStr.indexOf(";");
                String imageType = chanImgStr.substring(1, eIndex);
                // 取得实际编码
                String base = "base64,";
                int bIndex = imgEncode.indexOf(base);
                imgEncode = imgEncode.substring(bIndex + 7);

                //Base64解码
                byte[] imgByte = decoder.decodeBuffer(imgEncode);
                for (int i = 0; i < imgByte.length; ++i) {
                    if (imgByte[i] < 0) {//调整异常数据
                        imgByte[i] += 256;
                    }
                }
//                //生成jpeg图片
//                String imgFilePath = "test.gif";//新生成的图片
//                OutputStream out = new FileOutputStream(imgFilePath);
//                out.write(imgByte);
//                out.flush();
//                out.close();

                // save to db
                Image image = new Image();
                image.setImageType(imageType);
                image.setImageContent(imgByte);
                imageRepository.save(image);
            } catch (Exception ignored) {
            }

            RbImage rbImage = new RbImage();
            rbImage.setImageId(123);

            baseResponse = BaseResponse.success(rbImage);
        }

        return baseResponse;
    }
}