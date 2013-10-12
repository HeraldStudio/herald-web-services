package cn.edu.seu.herald.ws.resource;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CsvBuilderTest {

    @Test
    public void testCsvBuilder() throws Exception {
        CsvBuilder csvBuilder = new CsvBuilder();

        List<String> list = Arrays.asList("a", "b", "c");
        String csv = csvBuilder.getCsv(list);
        Assert.assertEquals("a,b,c", csv);
    }
}
