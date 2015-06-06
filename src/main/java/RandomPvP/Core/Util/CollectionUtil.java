package RandomPvP.Core.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class CollectionUtil<T> {

    public List<T> toList(Collection<T> collection) {
        List<T> list = new ArrayList<>();
        {
            list.addAll(collection);
        }
        return list;
    }

    public Collection<T> toCollection(List<T> list) {
        Collection<T> collection = new ArrayList<>();
        {
            collection.addAll(list);
        }
        return collection;
    }

}
