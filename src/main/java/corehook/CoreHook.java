package corehook;

import com.sun.jna.Pointer;
import corehook.natives.DirectMappingCoreHookNative;

public class CoreHook {

    public Pointer FindFunction(String module, String function) {
        return DirectMappingCoreHookNative.DetourFindFunction(module, function);
    }
}
