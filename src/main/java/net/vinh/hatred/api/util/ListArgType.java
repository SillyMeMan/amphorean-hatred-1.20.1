package net.vinh.hatred.api.util;

import java.util.List;

public class ListArgType<T> implements ArgType<List<T>> {
    private final ArgType<T> elementType;

    public ListArgType(ArgType<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public boolean matches(Object value) {
        if(!(value instanceof List<?> list)) {
            return false;
        }

        for(Object element : list) {
            if(!elementType.matches(element)) return false;
        }

        return true;
    }

    @Override
    public List<T> cast(Object value) {
        if(!matches(value)) {
            throw new ClassCastException(
                    "Value is not a valid " + this
            );
        }

        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) value;

        return result;
    }

    @Override
    public String toString() {
        return "List<" + elementType + ">";
    }
}
