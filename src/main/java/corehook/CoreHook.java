package corehook;

import corehook.natives.DirectMappingCoreHookNative;

public class CoreHook {

    public long FindFunction(String module, String function) {
        return DirectMappingCoreHookNative.DetourFindFunction(module, function);
    }
}
