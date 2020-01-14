package be.stijnhooft.portal.recurringtasks.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper<X, Y> {

    public abstract Y map(X x);

    public Collection<Y> map(Collection<X> x) {
        return mapAsList(x);
    }

    public List<Y> mapAsList(Collection<X> x) {
        return x.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
