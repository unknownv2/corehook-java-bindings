package corehook;

import com.sun.jna.Pointer;

public class LocalHook {

    private Object callback;
    private Pointer targetAddress;
    private CoreHookDetourCallback detourFunction;
    private byte[] handle;
    private HookAccessControl accessControl;

    public LocalHook() {

    }
    public LocalHook(Object callback, Pointer targetAddress, CoreHookDetourCallback detourFunction, byte[] handle) {
        this.callback = callback;
        this.targetAddress = targetAddress;
        this.detourFunction = detourFunction;
        this.handle = handle;
    }

    public void setTargetAddress(Pointer targetAddress) {
        this.targetAddress = targetAddress;
    }

    public void setCallback(Object callback) {
        this.callback = callback;
    }

    public void setDetourFunction(CoreHookDetourCallback detourFunction) {
        this.detourFunction = detourFunction;
    }

    public void setHandle(byte[] handle) {
        this.handle = handle;
    }

    public byte[] getHandle() {
        return this.handle;
    }

    public void setAccessControl(HookAccessControl accessControl) {
        this.accessControl = accessControl;
    }
    public HookAccessControl getAccessControl() {
        return this.accessControl;
    }
}
