package syntax;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class GrammarRequest {
    public abstract Production getEntryPoint();

    public Map<String, Production> getProductions() {
        HashMap<String, Production> r = new HashMap<>();
        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (!(f.getType().equals(Production.class))) continue;
                r.put(f.getName(), (Production)f.get(this));
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
