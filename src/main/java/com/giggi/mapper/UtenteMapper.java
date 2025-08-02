package com.giggi.mapper;

import com.giggi.dto.request.auth.RegisterRequest;
import com.giggi.dto.request.utente.UtenteCreateRequestDTO;
import com.giggi.dto.request.utente.UtenteUpdateRequestDTO;
import com.giggi.dto.response.utente.UtenteFindDTO;
import com.giggi.dto.response.utente.UtenteFindDTOSmall;
import com.giggi.entity.Role;
import com.giggi.entity.Utente;
import com.giggi.entity.enums.RuoliPartita;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    Utente convert(UtenteCreateRequestDTO dto);

    Utente convert(UtenteUpdateRequestDTO dto);

    Utente convert(RegisterRequest dto);

    Utente convert(UtenteFindDTO dto);

    UtenteFindDTOSmall convertSmall(Utente entity);
    UtenteFindDTO convert(Utente entity);

    List<UtenteFindDTO> convert(List<Utente> entities);

    // Metodo di mapping personalizzato
    default Set<Role> map(Set<String> value) {
        if (value == null) return null;
        return value.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    default Set<RuoliPartita> mapRuoliPreferiti(Set<String> value) {
        if (value == null) return null;
        return value.stream()
                .map(RuoliPartita::valueOf)
                .collect(Collectors.toSet());
    }

    default Set<String> mapRuoliPreferitiToString(Set<RuoliPartita> value) {
        if (value == null) return null;
        return value.stream()
                .map(RuoliPartita::name)
                .collect(Collectors.toSet());
    }

}