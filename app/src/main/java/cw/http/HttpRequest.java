package cw.http;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

@Data
public class HttpRequest {
    private Method method;
    private String path;
    private String protocol;
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body;
    public enum Method{
        GET, POST
    }

    public static HttpRequest of (String text){
        HttpRequest request = new HttpRequest();

        String[] lines = text.replace("\r", "").split("\n");

        String startLine = lines[0];
        String[] startLineParts = startLine.split(" ");
        request.setMethod(Method.valueOf(startLineParts[0]));
        request.setPath(startLineParts[1]);
        request.setProtocol(startLineParts[2]);

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if(line.equals("")){
                // read body
                StringJoiner body = new StringJoiner("\n");
                for (int j = i + 1; j < lines.length; j++) {
                    body.add(lines[j]);
                }
                request.setBody(body.toString());
            }else{
                // read header
                String[] keyValue = line.split(": ");
                request.getHeaders().put(
                    keyValue[0].strip(),
                    keyValue[1].strip()
                );
            }
        }
        return request;
    }
}
