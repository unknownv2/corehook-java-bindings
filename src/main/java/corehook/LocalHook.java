package corehook;

import com.sun.jna.Pointer;

public class LocalHook {

    public Object callback;
    public Pointer targetAddress;
    public CoreHookDetourCallback detourFunction;
    public byte[] handle;
    public HookAccessControl AccessControl;

    public LocalHook() {

    }
}
