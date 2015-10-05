package com.ucl.hottopic.test.editor;

import com.ucl.hottopic.Application;
import com.ucl.hottopic.editor.DoubleSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.net.idn.StringPrep;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-5
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DoubleSerializerTest {
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer ds = new DoubleSerializer();
        double[] testDouble = new double[]{111.1, 111.11115, 0.1, 0.11114, 111};
        String[] testResult = new String[]{"111.1", "111.1112", "0.1", "0.1111", "111"};
        for(int i=0; i<testDouble.length; i++) {
            double d = testDouble[i];
            Assert.assertEquals(testResult[i], ds.format(d,5));
        }
    }
}
