package com.giggi.mapper;

import com.giggi.dto.response.Partita.PartitaFindSmallDTO;
import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.entity.Partita;
import com.giggi.dto.request.partita.PartitaCreateRequestDTO;
import com.giggi.dto.request.partita.PartitaUpdateRequestDTO;
import com.giggi.dto.response.Partita.PartitaFindDTO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PartitaMapper {

    @Mapping(target = "id", ignore = true)
    Partita convert(PartitaCreateRequestDTO dto);

    Partita convert(PartitaUpdateRequestDTO dto);

    PartitaFindDTO convert(Partita entity);
    PartitaFindSmallDTO convertSmall(Partita partita);
    List<PartitaFindSmallDTO> convertSmall(List<Partita> partite);

    List<PartitaFindDTO> convert(List<Partita> entities);
}