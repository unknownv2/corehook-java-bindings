package corehook;

import com.sun.jna.Pointer;
import corehook.natives.DirectMappingCoreHookNative;

public class CoreHook implements AutoCloseable {

    public Pointer findFunction(String module, String function) {
        return DirectMappingCoreHookNative.DetourFindFunction(module, function);
    }

    public LocalHook create(Pointer targetFunction, CoreHookDetourCallback detourFunction, Object callback) {
        LocalHook hook = new LocalHook(callback, targetFunction, detourFunction, new byte[8]);
        DirectMappingCoreHookNative.DetourInstallHook(targetFunction, detourFunction, null, hook.getHandle());
        hook.setAccessControl(new HookAccessControl(hook.getHandle()));
        return hook;
    }

    @Override
    public void close() {
    }
}
