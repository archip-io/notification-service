package com.archipio.notificationservice.mapper;

import com.archipio.notificationservice.amqp.message.NotificationEmailMessage;
import com.archipio.notificationservice.dto.RenderDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface RenderMapper {

    RenderDto toDto(NotificationEmailMessage.Template template);

    RenderDto.ParameterDto toDto(NotificationEmailMessage.Template.Parameter parameter);
}
