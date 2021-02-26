package DSLGuided.requestsx;

import abstractions.DSLProcessor;
import abstractions.Role;
import com.avs.ParseDSL;

import java.util.List;

public class requests extends DSLProcessor {
    public ParseDSL parser = new ParseDSL();
    public String mock(){
        return "hi";
    }

    public String parseroleDSL(String DSL ){
        return parser.parseRole(DSL).toString();
    };

    public String parseroles(String DSL ){
        return parser.parseRoles(DSL).toString();
    };


    @Override
    public String render(String DSL) {
        return "hi";
    }

    @Override
    public List<Role> parseRoles(String DSL) {
        return parser.parseRoles(DSL);
    }
}
