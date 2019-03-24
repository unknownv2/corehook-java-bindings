package corehook;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoreHookTest {
    private CoreHook corehook;

    @BeforeEach
    void setUp() {
        corehook = new CoreHook();
    }

    @Test
    void findFunctionAddress() {
        assertNotEquals(Pointer.NULL, corehook.FindFunction("kernel32.dll", "CreateFileW"));
    }
}
