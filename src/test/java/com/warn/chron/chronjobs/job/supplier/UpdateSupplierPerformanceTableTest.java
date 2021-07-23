package com.warn.chron.chronjobs.job.supplier;

import com.warn.chron.chronjobs.job.supplier.performance.scheduler.UpdateSupplierPerformanceTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UpdateSupplierPerformanceTableTest {

    @Autowired
    UpdateSupplierPerformanceTable updateSupplierPerformanceTable;

    @Test
    public void executeTest() {
        updateSupplierPerformanceTable.execute();
    }
}
