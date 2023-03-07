package earth.terrarium.momento.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResetingIterator<T> implements Iterator<T> {

    private final List<T> list = new ArrayList<>();
    private int index = 0;

    public ResetingIterator(Iterator<T> iterator) {
        iterator.forEachRemaining(list::add);
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public T next() {
        return list.get(index++);
    }

    public void reset() {
        index = 0;
    }
}
