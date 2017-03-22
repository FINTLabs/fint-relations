package no.fint.relations.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class FintRelationsProps {

    @Getter
    @Value("${fint.relations.relation-id-base:https://dokumentasjon.felleskomponent.no/relasjoner/}")
    private String relationBase;

    @Getter
    @Value("${fint.relations.force-https:true}")
    private String forceHttps;

}
