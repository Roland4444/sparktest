package abstractions;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public interface  OnRequest {
    public void action(RequestMessage message) throws IOException, ParseException;
}
