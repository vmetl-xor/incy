package com.vmetl.crawler;

import com.vmetl.incy.cache.RefsCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Initializer implements InitializingBean {


    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    Map<String,RefsCache> caches;

   @Override
    public void afterPropertiesSet() throws Exception {
        caches.forEach((key, value) -> LOG.info("initializing {}", key));
    }
}
