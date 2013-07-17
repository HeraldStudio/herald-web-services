package cn.edu.seu.herald.ws.resource.aspect;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
final class ResourceAspectUtils {

    public static String linkPath(String... path) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String p : path) {
            pathBuilder.append(p);
        }
        return pathBuilder.toString();
    }
}
