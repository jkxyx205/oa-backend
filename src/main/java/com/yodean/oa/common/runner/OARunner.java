package com.yodean.oa.common.runner;

import com.yodean.oa.common.config.Global;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/22.
 */
@Component
public class OARunner implements CommandLineRunner {

    @Resource
    private Global global;

    @Override
    public void run(String... strings) throws Exception {

    }

}
