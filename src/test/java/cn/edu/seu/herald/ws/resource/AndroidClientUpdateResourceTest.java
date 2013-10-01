package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.update.Update;
import cn.edu.seu.herald.ws.dao.AndroidClientUpdateDataAccess;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@RunWith(JMockit.class)
public class AndroidClientUpdateResourceTest {

    @Tested
    private AndroidClientUpdateResource androidClientUpdateResource;
    @Mocked
    private AndroidClientUpdateDataAccess androidClientUpdateDataAccess;

    @Test
    public void testGetUpdateInfo() throws Exception {
        new Expectations() {{
            androidClientUpdateDataAccess.getUpdateInfo();
            result = new Update();
        }};
        androidClientUpdateResource.getUpdateInfo();
    }
}
