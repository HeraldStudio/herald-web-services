package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.dao.CampusInfoDataAccess;
import mockit.Injectable;
import mockit.Tested;

public class CampusInfoResourceTest {

    @Tested
    private CampusInfoResource campusInfoResource;
    @Injectable
    private CampusInfoDataAccess campusInfoDataAccess;
}
