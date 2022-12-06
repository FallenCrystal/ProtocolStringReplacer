package me.rothes.protocolstringreplacer.packetlisteners.server.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import me.rothes.protocolstringreplacer.api.user.PsrUser;

public final class TileEntityData extends AbstractServerSignPacketListener {

    public TileEntityData() {
        super(PacketType.Play.Server.TILE_ENTITY_DATA);
    }

    protected void process(PacketEvent packetEvent) {
        PsrUser user = getEventUser(packetEvent);
        if (user == null) {
            return;
        }
        PacketContainer packet = packetEvent.getPacket();
        // 9: Set the text on a sign
        if (packet.getIntegers().read(0) == 9) {
            Object read = packet.getStructures().withType(MinecraftReflection.getNBTBaseClass()).read(0);
            setSignText(packetEvent, new NBTContainer(read), user, filter);
        }
    }

}
