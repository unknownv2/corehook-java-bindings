package corehook;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import corehook.natives.DirectMappingCoreHookNative;

public class CoreHook {

    public Pointer FindFunction(String module, String function) {
        return DirectMappingCoreHookNative.DetourFindFunction(module, function);
    }
    public LocalHook Create(Pointer targetFunction, Callback detourFunction, Object callback) {
        var pointerObject = new PointerByReference();

        var hook = new LocalHook();
        hook.TargetAddress = targetFunction;
        hook.DetourFunction = detourFunction;
        hook.Callback = callback;
        hook.Handle = new byte[8];
        DirectMappingCoreHookNative.DetourInstallHook(targetFunction, detourFunction, null, hook.Handle);

        hook.AccessControl = new HookAccessControl(hook.Handle);
        return hook;
    }
}
