// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface CommentState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    Long getCommentSeqId();

    String getCommenter();

    String getBody();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    Boolean getDeleted();

    Long getArticleId();

    interface MutableCommentState extends CommentState {
        void setCommentSeqId(Long commentSeqId);

        void setCommenter(String commenter);

        void setBody(String body);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setDeleted(Boolean deleted);

        void setArticleId(Long articleId);


        void mutate(Event e);

        //void when(CommentEvent.CommentStateCreated e);

        //void when(CommentEvent.CommentStateMergePatched e);

        //void when(CommentEvent.CommentStateRemoved e);
    }

    interface SqlCommentState extends MutableCommentState {
        ArticleCommentId getArticleCommentId();

        void setArticleCommentId(ArticleCommentId articleCommentId);


        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
