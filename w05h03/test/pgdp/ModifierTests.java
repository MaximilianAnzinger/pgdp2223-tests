package pgdp;

import org.junit.jupiter.api.Test;
import pgdp.messenger.*;

import java.lang.reflect.Modifier;

import static pgdp.ReflectionHelper.*;

public class ModifierTests {
    @Test
    public void checkPinguTalk() {
        ReflectionHelper.assertMethodModifiers(Modifier.PUBLIC, PinguTalk.class, "addMember", String.class, User.class);
        ReflectionHelper.assertMethodModifiers(Modifier.PUBLIC, PinguTalk.class, "deleteMember", long.class);
        ReflectionHelper.assertMethodModifiers(Modifier.PUBLIC, PinguTalk.class, "createNewTopic", String.class);
        ReflectionHelper.assertMethodModifiers(Modifier.PUBLIC, PinguTalk.class, "deleteTopic", long.class);

        ReflectionHelper.assertFieldModifiers(Modifier.PRIVATE, UserArray.class, "users", User[].class);
        ReflectionHelper.assertFieldModifiers(Modifier.PRIVATE, PinguTalk.class, "userArray", UserArray.class);
        ReflectionHelper.assertFieldModifiers(Modifier.PRIVATE, PinguTalk.class, "topics", Topic[].class);
        ReflectionHelper.assertFieldModifiers(Modifier.PRIVATE | Modifier.STATIC, PinguTalk.class, "topicID", long.class);
        ReflectionHelper.assertFieldModifiers(Modifier.PRIVATE | Modifier.STATIC, PinguTalk.class, "userID", long.class);

        ReflectionHelper.assertConstructorModifiers(Modifier.PUBLIC, PinguTalk.class, int.class, int.class);
    }
}
