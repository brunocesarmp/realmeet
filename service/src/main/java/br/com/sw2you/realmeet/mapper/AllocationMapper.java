package br.com.sw2you.realmeet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;

@Mapper(componentModel = "spring")
public abstract class AllocationMapper {

    @Mapping(source = "room", target = "room")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createAllocationDTO.employeeName", target = "employee.name")
    @Mapping(source = "createAllocationDTO.employeeEmail", target = "employee.email")
    public abstract Allocation fromCreateAllocationDtoToEntity(CreateAllocationDTO createAllocationDTO, Room room);

    public abstract AllocationDTO fromEntityToDto(Allocation allocation);

}