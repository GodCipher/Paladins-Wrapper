package me.skiincraft.api.paladins.objects;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;

public class HirezAPIUtils {

    private static final String ENDPOINT = "https://api.paladins.com/paladinsapi.svc";

    public static boolean checkResponse(String body) {
        if (body.contains("Invalid Developer Id") ||
                body.contains("Invalid session id") ||
                body.contains("Exception while validating developer access.") ||
                body.contains("Error while comparing Server and Client timestamp")) {
            return false;
        }
        return !body.contains("Exception - Timestamp");
    }

    private static String complete(String... strings) {
        return Arrays.stream(strings).map(str -> str.replace(" ", "_")
                .replace("/", ""))
                .collect(Collectors.joining("/"));
    }

    public static String makeUrl(int devId, String authkey, String method, String[] args) {
        String url = String.format(ENDPOINT + "/%s%s/%s", method.toLowerCase(), "Json",
                complete(String.valueOf(devId),
                        getSignature(devId, method.toLowerCase(), authkey),
                        getTimeStamp()) + ((args == null || args.length == 0) ? "" : "/"));

        if (args == null) {
            return url;
        }
        return url + String.join("/", args);
    }

    public static String makeUrl(int devId, String authkey, String method, String sessionId, String[] args) {
        String url = String.format(ENDPOINT + "/%s%s/%s", method.toLowerCase(), "Json",
                complete(String.valueOf(devId),
                        getSignature(devId, method.toLowerCase(), authkey),
                        sessionId,
                        getTimeStamp()) + ((args == null || args.length == 0) ? "" : "/"));

        if (args == null) {
            return url;
        }
        return url + String.join("/", args);
    }

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return sdf.format(new Date());
    }

    public static String getSignature(int devId, String method, String authkey) {
        try {
            String signature = devId + method + authkey + getTimeStamp();
            MessageDigest digestor = MessageDigest.getInstance("MD5");
            digestor.update(signature.getBytes());
            byte[] bytes = digestor.digest();

            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b & 0xff));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
