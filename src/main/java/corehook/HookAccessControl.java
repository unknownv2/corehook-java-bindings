package corehook;

import corehook.natives.DirectMappingCoreHookNative;

public class HookAccessControl {
    private final int[] DefaultThreadAcl = new int[0];

    private boolean isExclusive;
    private byte[] handle;
    private int[] acl = DefaultThreadAcl;

    public HookAccessControl(byte[] handle) {
        isExclusive = handle == null;
        this.handle = handle;
    }

    public void setExclusiveAcl(int[] acl) {
        isExclusive = true;
        this.acl = acl.clone();

        if(handle == null) {
            DirectMappingCoreHookNative.DetourSetGlobalExclusiveACL(acl, acl.length);
        }
        else {
            DirectMappingCoreHookNative.DetourSetExclusiveACL(acl, acl.length, handle);
        }
    }

    public void setInclusiveAcl(int[] acl) {
        isExclusive = false;

        this.acl = acl.clone();

        if(handle == null) {
            DirectMappingCoreHookNative.DetourSetGlobalInclusiveACL(this.acl, this.acl.length);
        }
        else {
            DirectMappingCoreHookNative.DetourSetInclusiveACL(this.acl, this.acl.length, handle);
        }
    }

    public int[] getEntries() {
        return this.acl.clone();
    }
}
