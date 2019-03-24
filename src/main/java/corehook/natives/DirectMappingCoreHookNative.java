package corehook.natives;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import corehook.jna.CoreHookTypeMapper;

import java.util.HashMap;
import java.util.Map;

public final class DirectMappingCoreHookNative {
    static {
        Map<String, Object> options = new HashMap<>();
        //options.put(Library.OPTION_TYPE_MAPPER, new CoreHookTypeMapper());

        // Direct Mapping using JNA
        Native.register(NativeLibrary.getInstance("corehook64", options));
    }

    private DirectMappingCoreHookNative() {
    }

    public static native Pointer DetourFindFunction(String module, String function);

    public static native int DetourInstallHook(Pointer entryPoint, Callback hookProcedure, Pointer callback, byte[] handle);

    public static native void DetourSetGlobalExclusiveACL(int[] threadIdList, int threadCount);

    public static native void DetourSetExclusiveACL(int[] threadIdList, int threadCount, byte[] handle);
}
