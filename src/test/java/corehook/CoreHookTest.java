package corehook;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.jna.platform.win32.Kernel32;
import java.io.*;

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

    @Test
    void findPrivateFunctionAddress() {
        assertNotEquals(Pointer.NULL, corehook.FindFunction("kernel32.dll", "InternalFindAtom"));
    }
}
