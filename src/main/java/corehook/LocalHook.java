package corehook;

import com.sun.jna.Pointer;
import corehook.natives.DirectMappingCoreHookNative;

import java.util.concurrent.atomic.AtomicBoolean;

public class LocalHook implements AutoCloseable {

    private Object callback;
    private CoreHookDetourCallback detourFunction;
    private Pointer targetAddress;
    private byte[] handle;
    private HookAccessControl accessControl;

    private final AtomicBoolean hasBeenClosed;

    public LocalHook() {
        handle = null;
        hasBeenClosed = new AtomicBoolean(false);
    }

    public LocalHook(Object callback, Pointer targetAddress, CoreHookDetourCallback detourFunction, byte[] handle) {
        this.callback = callback;
        this.targetAddress = targetAddress;
        this.detourFunction = detourFunction;
        this.handle = handle;
        this.hasBeenClosed = new AtomicBoolean(false);
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

    @Override
    public void close() {
        boolean isClosed = hasBeenClosed.getAndSet(true);

        if(!isClosed && handle != null) {
            DirectMappingCoreHookNative.DetourUninstallHook(this.handle);

            // Reset the hooking  fields.
            callback = null;
            detourFunction = null;
            targetAddress = Pointer.NULL;
            handle = null;
        }
    }
}
