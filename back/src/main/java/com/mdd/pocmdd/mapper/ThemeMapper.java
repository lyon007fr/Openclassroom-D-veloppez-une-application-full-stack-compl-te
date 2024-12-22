package com.mdd.pocmdd.mapper;

import org.mapstruct.Mapper;
import com.mdd.pocmdd.dto.ThemeDTO;
import com.mdd.pocmdd.models.Theme;

@Mapper(componentModel = "spring")
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {

}
