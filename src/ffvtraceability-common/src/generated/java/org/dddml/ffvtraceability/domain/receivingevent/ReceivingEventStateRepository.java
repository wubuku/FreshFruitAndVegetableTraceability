// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.receivingevent;

import java.util.*;
import org.dddml.support.criterion.Criterion;
import org.dddml.ffvtraceability.domain.*;
import java.time.OffsetDateTime;

public interface ReceivingEventStateRepository {
    ReceivingEventState get(Long id, boolean nullAllowed);

    void save(ReceivingEventState state);

    void merge(ReceivingEventState detached);
}
