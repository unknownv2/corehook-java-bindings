package corehook;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.jna.platform.win32.Kernel32;

import static org.junit.jupiter.api.Assertions.*;

public class CoreHookTest {
    private CoreHook corehook;

    @BeforeEach
    void setUp() { corehook = new CoreHook(); }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findFunctionAddress() {
        assertNotEquals(Pointer.NULL, corehook.findFunction("kernel32.dll", "CreateFileW"));
    }

    @Test
    void findPrivateFunctionAddress() {
        assertNotEquals(Pointer.NULL, corehook.findFunction("kernel32.dll", "InternalFindAtom"));
    }

    // Win32 file access constants.
    private static int GENERIC_ACCESS = -2147483648;
    private static int EXCLUSIVE_ACCESS = 0;
    private static int OPEN_EXISTING = 3;

    // Boolean for checking whether a detour callback was executed for 'CreateFile'.
    private static boolean detouredCreateFile = false;

    @Test
    void createFunctionDetour_shouldCallbackCustomHandlerForCreateFile() {
        detouredCreateFile = false;
        Pointer createFileFunctionAddress = corehook.findFunction("kernel32.dll", "CreateFileW");
        assertNotEquals(Pointer.NULL, createFileFunctionAddress);

        // The detour is enabled, so the callback should be executed.
        LocalHook hook = corehook.create(createFileFunctionAddress, new CoreHookDetourCallback() {
            public WinNT.HANDLE createFile(Pointer fileName, int desiredAccess, int shareMode, WinBase.SECURITY_ATTRIBUTES securityAttributes, int creationDisposition, int flagsAndAttributes, WinNT.HANDLE templateFile) {
                detouredCreateFile = true;
                return Kernel32.INSTANCE.CreateFile( fileName.getWideString(0), desiredAccess, shareMode, securityAttributes, creationDisposition, flagsAndAttributes, templateFile);
            }
        }, this);

        // Enable the detour for the current thread.
        hook.getAccessControl().setInclusiveAcl(new int[] { 0 });
        // Call kernel32.dll!CreateFile, which should call the detour handler.
        WinNT.HANDLE handle = Kernel32.INSTANCE.CreateFile("file.txt", GENERIC_ACCESS, EXCLUSIVE_ACCESS, null, OPEN_EXISTING, 0, null);

        assertTrue(detouredCreateFile);
        // Close the handle if it is valid.
        if(!WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
            Kernel32.INSTANCE.CloseHandle(handle);
        }
        hook.close();
    }

    private static boolean didNotDetourCreateFile = false;

    @Test
    void createFunctionDetour_shouldNotCallbackCustomHandlerForCreateFileWhenNotEnabled() {
        didNotDetourCreateFile = false;
        Pointer createFileFunctionAddress = corehook.findFunction("kernel32.dll", "CreateFileW");
        assertNotEquals(Pointer.NULL, createFileFunctionAddress);

        // The detour is created but is not enabled, so the callback is not executed.
        LocalHook hook = corehook.create(createFileFunctionAddress, new CoreHookDetourCallback() {
            public WinNT.HANDLE createFile(Pointer fileName, int desiredAccess, int shareMode, WinBase.SECURITY_ATTRIBUTES securityAttributes, int creationDisposition, int flagsAndAttributes, WinNT.HANDLE templateFile) {
                didNotDetourCreateFile = true;
                return Kernel32.INSTANCE.CreateFile( fileName.getWideString(0), desiredAccess, shareMode, securityAttributes, creationDisposition, flagsAndAttributes, templateFile);
            }
        }, this);

        // Call kernel32.dll!CreateFile, which should not call the detour handler.
        WinNT.HANDLE handle = Kernel32.INSTANCE.CreateFile("file.txt", GENERIC_ACCESS, EXCLUSIVE_ACCESS, null, OPEN_EXISTING, 0, null);

        assertFalse(didNotDetourCreateFile);
        // Close the handle if it is valid.
        if(!WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
            Kernel32.INSTANCE.CloseHandle(handle);
        }
        hook.close();
    }
}
