package rk;

import io.spring.guides.gs_producing_web_service.MyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ReplEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final JavaRepl repl;

    @Autowired
    public ReplEndpoint(JavaRepl repl) {
        this.repl = repl;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "MyRequest")
    @ResponsePayload
    public MyRequest getCountry(@RequestPayload MyRequest request) throws Exception {
        String execute = repl.execute(request.getBody());
        MyRequest myRequest = new MyRequest();
        myRequest.setBody(execute);
        return myRequest;
    }
}
