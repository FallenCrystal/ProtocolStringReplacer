package me.Rothes.ProtocolStringReplacer.PacketListeners.Server.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import me.Rothes.ProtocolStringReplacer.ProtocolStringReplacer;
import me.Rothes.ProtocolStringReplacer.User.User;
import org.bukkit.inventory.ItemStack;

public class WindowItems extends AbstractServerItemPacketListener {

    public WindowItems() {
        super(PacketType.Play.Server.WINDOW_ITEMS);
    }

    public final PacketAdapter packetAdapter = new PacketAdapter(ProtocolStringReplacer.getInstance(), ListenerPriority.HIGHEST, packetType) {
        public void onPacketSending(PacketEvent packetEvent) {
            User user = getEventUser(packetEvent);
            Object[] read = (Object[]) packetEvent.getPacket().getModifier().read(1);
            for (var item : read) {
                ItemStack itemStack = BukkitConverters.getItemStackConverter().getSpecific(item);
                if (itemStack.hasItemMeta()) {
                    ItemStack original = itemStack.clone();
                    ProtocolStringReplacer.getInstance().getReplacerManager().getReplacedItemStack(itemStack, user, itemFilter);
                    if (!original.isSimilar(itemStack)) {
                        saveUserMetaCacche(user, original, itemStack);
                    }
                }
            }
        }
    };

}
