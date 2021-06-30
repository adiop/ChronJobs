package com.warn.chron.chronjobs.job.upc;

import com.warn.chron.chronjobs.job.jde.utility.upc.entitites.PartNumber;
import com.warn.chron.chronjobs.job.jde.utility.upc.scheduler.AssignUPCNumbers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AssignUPCNumbersTest {
    @Autowired
    AssignUPCNumbers assignUPCNumbers;

    @Test
    public void getPartNumbersWithNoUPCTest() {
        List<PartNumber> partNumberList = assignUPCNumbers.getPartNumbersWithNoUPC();
        System.out.println("*****************************************");
        System.out.println("****************" + partNumberList.size());
        System.out.println("*****************************************");
    }

    @Test
    public void updateItemMasterWithNewUPCCodes () {
        assignUPCNumbers.updateItemMasterWithNewUPCCodes();
    }

    @Test
    public void dropf4101WkTable () {
        assignUPCNumbers.dropf4101WkTable();
    }

    @Test
    public void executeTest() {
        assignUPCNumbers.execute();
    }
}
