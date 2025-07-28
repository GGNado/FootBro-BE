package com.giggi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import com.giggi.entity.PartecipazioneCampionato;
import com.giggi.dto.request.PartecipazioneCampionato.PartecipazioneCampionatoCreateRequestDTO;
import com.giggi.dto.request.PartecipazioneCampionato.PartecipazioneCampionatoUpdateRequestDTO;
import com.giggi.dto.response.PartecipazioneCampionato.PartecipazioneCampionatoFindDTO;

@Mapper(componentModel = "spring")
public interface PartecipazioneCampionatoMapper {

    PartecipazioneCampionato convert(PartecipazioneCampionatoCreateRequestDTO dto);

    PartecipazioneCampionato convert(PartecipazioneCampionatoUpdateRequestDTO dto);

    PartecipazioneCampionatoFindDTO convert(PartecipazioneCampionato entity);

    List<PartecipazioneCampionatoFindDTO> convert(List<PartecipazioneCampionato> entities);
}