package com.giggi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import com.giggi.entity.Campionato;
import com.giggi.dto.request.campionato.CampionatoCreateRequestDTO;
import com.giggi.dto.request.campionato.CampionatoUpdateRequestDTO;
import com.giggi.dto.response.campionato.CampionatoFindDTO;

@Mapper(componentModel = "spring")
public interface CampionatoMapper {

    @Mapping(target = "id", ignore = true)
    Campionato convert(CampionatoCreateRequestDTO dto);

    Campionato convert(CampionatoUpdateRequestDTO dto);

    CampionatoFindDTO convert(Campionato entity);

    List<CampionatoFindDTO> convert(List<Campionato> entities);
}