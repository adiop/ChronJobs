package com.warn.chron.chronjobs.job.dunandbradstreet;

import com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.scheduler.DunAndBradstreet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DunAndBradstreetTest {

    @Autowired
    DunAndBradstreet dunAndBradstreet;

    @Test
    public void executeTest() {
        dunAndBradstreet.execute();
    }
}
