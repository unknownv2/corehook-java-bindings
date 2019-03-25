package corehook;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.jna.platform.win32.Kernel32;

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

    // Win32 file access constants.
    static int GENERIC_ACCESS = -2147483648;
    static int EXCLUSIVE_ACCESS = 0;
    static int OPEN_EXISTING = 3;

    // Boolean for checking whether a detour callback was executed for 'CreateFile'.
    static boolean detouredCreateFile = false;

    @Test
    void createFunctionDetour_shouldCallbackCustomHandlerForCreateFile() {
        detouredCreateFile = false;
        Pointer createFileFunctionAddress = corehook.FindFunction("kernel32.dll", "CreateFileW");
        assertNotEquals(Pointer.NULL, createFileFunctionAddress);

        LocalHook hook = corehook.Create(createFileFunctionAddress, new CoreHookDetourCallback() {
            public WinNT.HANDLE createFile(Pointer fileName, int desiredAccess, int shareMode, WinBase.SECURITY_ATTRIBUTES securityAttributes, int creationDisposition, int flagsAndAttributes, WinNT.HANDLE templateFile) {
                detouredCreateFile = true;
                return Kernel32.INSTANCE.CreateFile( fileName.getWideString(0), desiredAccess, shareMode, securityAttributes, creationDisposition, flagsAndAttributes, templateFile);
            }
        }, this);

        // Enable the detour for all threads.
        hook.AccessControl.SetExclusiveAcl(new int[0]);
        // Call kernel32.dll!CreateFile, which should call the handle.
        WinNT.HANDLE handle = Kernel32.INSTANCE.CreateFile("file.txt", GENERIC_ACCESS, EXCLUSIVE_ACCESS, null, OPEN_EXISTING, 0, null);

        assertTrue(detouredCreateFile);
        // Close the handle if it is valid.
        if(!WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
            Kernel32.INSTANCE.CloseHandle(handle);
        }
    }
}
