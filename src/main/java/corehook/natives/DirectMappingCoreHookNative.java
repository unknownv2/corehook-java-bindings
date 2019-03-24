package corehook.natives;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
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

    public static native long DetourFindFunction(String module, String function);
}
