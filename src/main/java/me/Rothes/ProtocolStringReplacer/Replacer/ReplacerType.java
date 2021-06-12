package me.Rothes.ProtocolStringReplacer.Replacer;

import com.comphenix.protocol.PacketType;

public enum ReplacerType {

    CHAT("CHAT", PacketType.Play.Server.CHAT),
    OPEN_WINDOW("OPEN_WINDOW", PacketType.Play.Server.OPEN_WINDOW),
    SET_SLOT("SET_SLOT", PacketType.Play.Server.SET_SLOT),
    WINDOW_ITEMS("WINDOW_ITEMS", PacketType.Play.Server.WINDOW_ITEMS),
    ENTITY_METADATA("ENTITY_METADATA", PacketType.Play.Server.ENTITY_METADATA);

    ReplacerType(String name, PacketType packetType) {
        this.name = name;
        this.packetType = packetType;
    }

    private String name;
    private PacketType packetType;

    public PacketType getPacketType() {
        return packetType;
    }

    public String getName() {
        return name;
    }

}