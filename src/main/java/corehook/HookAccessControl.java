package corehook;

import corehook.natives.DirectMappingCoreHookNative;

public class HookAccessControl {
    private final int[] DefaultThreadAcl = new int[0];

    public boolean IsExclusive;
    public byte[] Handle;
    private int[] acl = DefaultThreadAcl;

    public HookAccessControl(byte[] handle) {
        IsExclusive = handle == null;
        Handle = handle;
    }
    public void SetExclusiveAcl(int[] acl) {
        if(Handle == null) {
            DirectMappingCoreHookNative.DetourSetGlobalExclusiveACL(acl, acl.length);
        }
        else {
            DirectMappingCoreHookNative.DetourSetExclusiveACL(acl, acl.length, Handle);
        }
    }
    public void SetInclusiveAcl(int[] acl) {

    }

    public int[] GetEntries() {
        return new int[] { 0 };
    }
}
