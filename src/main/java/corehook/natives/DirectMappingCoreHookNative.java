package corehook.natives;

import com.sun.jna.*;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import corehook.CoreHookDetourCallback;

import java.util.HashMap;
import java.util.Map;

public final class DirectMappingCoreHookNative {
    static {
        Map<String, Object> options = new HashMap<>();
        WinNT.HANDLE handle = Kernel32.INSTANCE.GetCurrentProcess();
        IntByReference isWow64 = new IntByReference();
        Kernel32.INSTANCE.IsWow64Process(handle, isWow64);
        if(isWow64.getValue() == 1) {
            Native.register(NativeLibrary.getInstance("corehook32", options));
        }
        else {
            Native.register(NativeLibrary.getInstance("corehook64", options));
        }
    }

    private DirectMappingCoreHookNative() {
    }

    public static native Pointer DetourFindFunction(String module, String function);

    public static native int DetourInstallHook(Pointer entryPoint, CoreHookDetourCallback hookProcedure, Pointer callback, byte[] handle);

    public static native void DetourSetGlobalExclusiveACL(int[] threadIdList, int threadCount);

    public static native void DetourSetExclusiveACL(int[] threadIdList, int threadCount, byte[] handle);

    public static native void DetourSetInclusiveACL(int[] threadIdList, int threadCount, byte[] handle);

    public static native void DetourSetGlobalInclusiveACL(int[] threadIdList, int threadCount);

    public static native void DetourUninstallHook(byte[] handle);
}
