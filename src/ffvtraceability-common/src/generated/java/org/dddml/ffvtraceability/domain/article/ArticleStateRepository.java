// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article;

import java.util.*;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface ArticleStateRepository {
    ArticleState get(Long id, boolean nullAllowed);

    void save(ArticleState state);

    void merge(ArticleState detached);
}

