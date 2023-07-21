package me.djben.cpark.permissions;


import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public enum Permissions implements IPermissionEnum {
    COMMAND_ADMIN("cpark.admin", PermissionDefault.OP, "Spawn edit for admin"),
    COMMAND_FlY("cpark.fly",PermissionDefault.TRUE, "Spawn edit for admin"),
    COMMAND_SPAWN("cpark.spawn",PermissionDefault.TRUE, "Spawn edit for admin"),
    COMMAND_MENU("cpark.menu", PermissionDefault.TRUE, "open menu");

    private final String _root;
    private final PermissionDefault _default;
    private final String _description;

    Permissions(final String node, final PermissionDefault permdefault, final String desc) {
        this._root = node;
        this._default = permdefault;
        this._description = desc;
    }

    @Override
    public boolean has(CommandSender sender) {
        // Brigadier nags us about this after the plugin disables, which causes problems
        if (CommonPlugin.hasInstance()) {
            return IPermissionEnum.super.has(sender);
        } else {
            return sender.hasPermission(this.getName());
        }
    }

    @Override
    public String getRootName() {
        return this._root;
    }

    @Override
    public PermissionDefault getDefault() {
        return this._default;
    }

    @Override
    public String getDescription() {
        return this._description;
    }
}