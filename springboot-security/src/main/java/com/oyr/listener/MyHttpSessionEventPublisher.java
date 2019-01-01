package com.oyr.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;

/**
 * session控制并发需要的监听器
 * Created by Administrator on 2018/12/27.
 */
@WebListener
public class MyHttpSessionEventPublisher extends HttpSessionEventPublisher {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        logger.info("===== sessionCreated =====");
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("===== sessionDestroyed =====");
        super.sessionDestroyed(event);
    }
}


