package br.com.sw2you.realmeet.mapper;

import org.mapstruct.Mapper;

import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    public abstract RoomDTO fromEntityToDto(Room room);

}
