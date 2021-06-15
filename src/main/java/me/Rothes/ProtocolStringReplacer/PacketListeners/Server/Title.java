package me.Rothes.ProtocolStringReplacer.PacketListeners.Server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.Rothes.ProtocolStringReplacer.ProtocolStringReplacer;
import me.Rothes.ProtocolStringReplacer.User.User;

public final class Title extends AbstractServerPacketListener {

    public Title() {
        super(PacketType.Play.Server.TITLE);
    }

    public final PacketAdapter packetAdapter = new PacketAdapter(ProtocolStringReplacer.getInstance(), ListenerPriority.HIGHEST, packetType) {
        public void onPacketSending(PacketEvent packetEvent) {
            // Can cause exception in ProtocolLib for 1.17 right now...
            // However, it's from ProtocolLib. I won't fix it right now.
            if (packetEvent.getPacketType() == packetType) {

                User user = getEventUser(packetEvent);
                StructureModifier<WrappedChatComponent> wrappedChatComponentStructureModifier = packetEvent.getPacket().getChatComponents();
                WrappedChatComponent wrappedChatComponent = wrappedChatComponentStructureModifier.read(0);
                if (wrappedChatComponent != null) {
                    String currentTitle = jsonToLegacyText(wrappedChatComponent.getJson());
                    user.setCurrentWindowTitle(currentTitle);
                    wrappedChatComponent.setJson(legacyTextToJson(ProtocolStringReplacer.getInstance().getReplacerManager().getReplacedString(currentTitle, user, filter)));
                    wrappedChatComponentStructureModifier.write(0, wrappedChatComponent);
                }
            }
        }
    };

}
