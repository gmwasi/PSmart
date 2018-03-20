package org.kenyahmis.psmartlibrary;

import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Test;
import org.kenyahmis.psmartlibrary.DAL.PSmartFile;

/**
 * Created by Muhoro on 3/20/2018.
 */

public class PSmartFileTest {

    @Test
    public void writeIsSuccessfull(){
        String expected = "Hello World - 123456[';'32{}09876";
        MockContext mockContext = new MockContext();
        PSmartFile file = new PSmartFile(mockContext, "test_file");
        file.write(expected);

        String readData = file.read();

        junit.framework.Assert.assertEquals(expected, readData);
    }
}
