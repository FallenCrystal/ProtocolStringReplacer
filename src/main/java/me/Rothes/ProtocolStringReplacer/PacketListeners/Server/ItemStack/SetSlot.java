package me.Rothes.ProtocolStringReplacer.PacketListeners.Server.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.Rothes.ProtocolStringReplacer.User.User;
import me.Rothes.ProtocolStringReplacer.ProtocolStringReplacer;
import org.bukkit.inventory.ItemStack;

public class SetSlot extends AbstractServerItemPacketListener {

    public SetSlot() {
        super(PacketType.Play.Server.SET_SLOT);
    }

    public final PacketAdapter packetAdapter = new PacketAdapter(ProtocolStringReplacer.getInstance(), ListenerPriority.LOW, packetType) {
        public void onPacketSending(PacketEvent packetEvent) {
            User user = getEventUser(packetEvent);
            ItemStack itemStack = packetEvent.getPacket().getItemModifier().read(0);
            ItemStack original = itemStack.clone();
            ProtocolStringReplacer.getReplacerManager().getReplacedItemStack(itemStack, user, itemFilter);
            if (!original.isSimilar(itemStack)) {
                saveUserMetaCacche(user, original, itemStack);
            }
        }
    };

}