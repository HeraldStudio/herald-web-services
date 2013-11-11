package cn.edu.seu.herald.ws.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class JsonpParser {

    @Autowired
    private Pattern jsVarNamePattern;

    public String jsonp(String callback, String json) {
        if (callback == null || !jsVarNamePattern.matcher(callback).matches()) {
            throw new IllegalArgumentException(
                    "illegal callback function name");
        }
        return String.format("%s(%s)", callback, json);
    }
}
