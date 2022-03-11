package br.com.sw2you.realmeet.util;

import org.mapstruct.factory.Mappers;

import br.com.sw2you.realmeet.mapper.RoomMapper;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static RoomMapper roomMapper() {
        return Mappers.getMapper(RoomMapper.class);
    }

}
