package com.oyr.controller;

import com.oyr.validate.code.ImageCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Administrator on 2018/12/25.
 */
@Controller
@RequestMapping("/validateCode/code")
public class ValidateCodeController {

    public final static String VALIDATE_CODE_KEY = "VALIDATE_CODE_KEY";
/*
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();*/

    @RequestMapping("")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
             1. 根据随机数生成图片
             2. 将随机数存到Session中
             3. 将生成的图片写到接口的响应中
         */
        /*ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
*/
        ImageCode imageCode = generate(new ServletWebRequest(request));
        request.getSession().setAttribute(VALIDATE_CODE_KEY, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 生成图形验证码
     * @param request
     * @return
     */
    private ImageCode generate(ServletWebRequest request) {
        int width = 64;
        int height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, sRand, 60);

    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
