// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.gs1applicationidentifier;

import java.util.*;
import org.dddml.support.criterion.Criterion;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;

public interface Gs1ApplicationIdentifierStateRepository {
    Gs1ApplicationIdentifierState get(String id, boolean nullAllowed);

    void save(Gs1ApplicationIdentifierState state);

    void merge(Gs1ApplicationIdentifierState detached);
}

