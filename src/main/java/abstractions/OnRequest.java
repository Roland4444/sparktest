package abstractions;

import java.io.IOException;

public interface OnRequest {
    public void action(RequestMessage message) throws IOException;
}
