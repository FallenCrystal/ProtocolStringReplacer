package me.Rothes.ProtocolStringReplacer.PacketListeners.Client.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.Rothes.ProtocolStringReplacer.ProtocolStringReplacer;
import me.Rothes.ProtocolStringReplacer.User.User;
import org.bukkit.inventory.ItemStack;

public class WindowClick extends AbstractClientItemPacketListener {

    public WindowClick() {
        super(PacketType.Play.Client.WINDOW_CLICK);
    }

    public final PacketAdapter packetAdapter = new PacketAdapter(ProtocolStringReplacer.getInstance(), ListenerPriority.LOW, packetType) {
        public void onPacketReceiving(PacketEvent packetEvent) {
            User user = getEventUser(packetEvent);
            ItemStack itemStack = packetEvent.getPacket().getItemModifier().read(0);
            if (user.hasPermission("protocolstringreplacer.feature.usermetacache.noncreative")) {
                resotreItem(user, itemStack);
            }
        }
    };

}
