// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortgoodstandardtype;

import java.util.*;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface WorkEffortGoodStandardTypeStateRepository {
    WorkEffortGoodStandardTypeState get(String id, boolean nullAllowed);

    void save(WorkEffortGoodStandardTypeState state);

    void merge(WorkEffortGoodStandardTypeState detached);
}

