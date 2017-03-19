package no.fint.relations.relations.hal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
class FintRelationObjectMethod {
    private Method method;
    private Object linkMapper;
}
