package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.dao.AndroidClientUpdateDataAccess;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

public class AndroidClientUpdateResourceTest {

    @Tested
    private AndroidClientUpdateResource androidClientUpdateResource;
    @Injectable
    private AndroidClientUpdateDataAccess androidClientUpdateDataAccess;

    @Test
    public void testGetUpdateInfo() throws Exception {
        new Expectations() {
            {
                androidClientUpdateDataAccess.getUpdateInfo();
                times = 1;
            }
        };
        androidClientUpdateResource.getUpdateInfo();
    }
}
