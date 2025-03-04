// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.product;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface GoodIdentificationCommand extends Command {

    String getGoodIdentificationTypeId();

    void setGoodIdentificationTypeId(String goodIdentificationTypeId);

    interface CreateOrMergePatchGoodIdentification extends GoodIdentificationCommand {
        String getIdValue();

        void setIdValue(String idValue);

    }

    interface CreateGoodIdentification extends CreateOrMergePatchGoodIdentification {
    }

    interface MergePatchGoodIdentification extends CreateOrMergePatchGoodIdentification {
        Boolean getIsPropertyIdValueRemoved();

        void setIsPropertyIdValueRemoved(Boolean removed);


    }

    interface RemoveGoodIdentification extends GoodIdentificationCommand {
    }

}

