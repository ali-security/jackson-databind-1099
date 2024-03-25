package tools.jackson.failing;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.*;

import tools.jackson.databind.*;
import tools.jackson.databind.testutil.DatabindTestUtil;

// [databind#2572]: "empty" setter, POJO with no 0-arg constructor
class AsEmptyPOJONoDefaultCtor2572Test extends DatabindTestUtil {
    static class Outer {
        @JsonProperty("inner")
        private final Inner inner;

        @JsonCreator
        public Outer(@JsonProperty("inner") Inner inner) {
            this.inner = Objects.requireNonNull(inner, "inner");
        }
    }

    static class Inner {
        @JsonProperty("field")
        private final String field;

        @JsonCreator
        public Inner(@JsonProperty("field") String field) {
            this.field = field;
        }
    }

    @Test
    void jackson() throws Exception {
        ObjectMapper mapper = jsonMapperBuilder()
                .changeDefaultNullHandling(h -> h.withOverrides(JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY)))
                .build();
        final String json = mapper.writeValueAsString(new Outer(new Inner("inner")));
        mapper.readValue(json, Outer.class);
    }
}
