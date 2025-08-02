package com.giggi.mapper;

import com.giggi.entity.enums.Squadra;
import com.giggi.entity.enums.TipologiaCampionato;
import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.entity.PartecipazioneCampionato;
import com.giggi.dto.request.partecipazioneCampionato.PartecipazioneCampionatoCreateRequestDTO;
import com.giggi.dto.request.partecipazioneCampionato.PartecipazioneCampionatoUpdateRequestDTO;
import com.giggi.dto.response.PartecipazioneCampionato.PartecipazioneCampionatoFindDTO;

@Mapper(componentModel = "spring")
public interface PartecipazioneCampionatoMapper {

    PartecipazioneCampionato convert(PartecipazioneCampionatoCreateRequestDTO dto);

    PartecipazioneCampionato convert(PartecipazioneCampionatoUpdateRequestDTO dto);

    PartecipazioneCampionatoFindDTO convert(PartecipazioneCampionato entity);

    List<PartecipazioneCampionatoFindDTO> convert(List<PartecipazioneCampionato> entities);
}