package abstractions;

import java.util.List;

public abstract class DSLProcessor {
    public abstract String render(String DSL);
    public abstract List<Role> parseRoles(String DSL);
}
