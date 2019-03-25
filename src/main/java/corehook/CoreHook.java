package corehook;

import com.sun.jna.Pointer;
import corehook.natives.DirectMappingCoreHookNative;

public class CoreHook implements AutoCloseable {

    public Pointer FindFunction(String module, String function) {
        return DirectMappingCoreHookNative.DetourFindFunction(module, function);
    }

    public LocalHook Create(Pointer targetFunction, CoreHookDetourCallback detourFunction, Object callback) {
        LocalHook hook = new LocalHook();
        hook.targetAddress = targetFunction;
        hook.detourFunction = detourFunction;
        hook.callback = callback;
        hook.handle = new byte[8];
        DirectMappingCoreHookNative.DetourInstallHook(targetFunction, detourFunction, null, hook.handle);

        hook.AccessControl = new HookAccessControl(hook.handle);
        return hook;
    }

    @Override
    public void close() throws Exception {

    }
}
