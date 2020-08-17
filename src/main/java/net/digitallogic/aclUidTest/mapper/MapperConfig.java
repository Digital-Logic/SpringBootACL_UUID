package net.digitallogic.aclUidTest.mapper;

import org.mapstruct.*;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        disableSubMappingMethodsGeneration = true,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MapperConfig {
}
