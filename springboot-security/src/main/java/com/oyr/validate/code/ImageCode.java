package com.oyr.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * Created by Administrator on 2018/12/25.
 */
public class ImageCode {

    private String code;

    private LocalDateTime expireTime;

    private BufferedImage image;

    public ImageCode() {
    }

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isExpried() {

    }
}
