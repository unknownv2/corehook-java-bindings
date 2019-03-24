package corehook;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class LocalHook {

    public Object Callback;
    public Pointer TargetAddress;
    public Callback DetourFunction;
    public byte[] Handle;
    public HookAccessControl AccessControl;

    public LocalHook() {

    }
}
