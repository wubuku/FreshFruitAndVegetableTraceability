// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;

public abstract class AbstractArticleAggregate extends AbstractAggregate implements ArticleAggregate {
    private ArticleState.MutableArticleState state;

    protected List<Event> changes = new ArrayList<Event>();

    public AbstractArticleAggregate(ArticleState state) {
        this.state = (ArticleState.MutableArticleState)state;
    }

    public ArticleState getState() {
        return this.state;
    }

    public List<Event> getChanges() {
        return this.changes;
    }

    public void create(ArticleCommand.CreateArticle c) {
        if (c.getVersion() == null) { c.setVersion(ArticleState.VERSION_NULL); }
        ArticleEvent e = map(c);
        apply(e);
    }

    public void mergePatch(ArticleCommand.MergePatchArticle c) {
        ArticleEvent e = map(c);
        apply(e);
    }

    public void throwOnInvalidStateTransition(Command c) {
        ArticleCommand.throwOnInvalidStateTransition(this.state, c);
    }

    protected void apply(Event e) {
        onApplying(e);
        state.mutate(e);
        changes.add(e);
    }

    @Override
    protected void onApplying(Event e) {
        if (state.getVersion() == null) {
            state.setTenantId(TenantContext.getTenantId());
        }
        if (e instanceof ArticleEvent) {
            ArticleEvent ee = (ArticleEvent) e;
            ee.setTenantId(state.getTenantId());
        }
        super.onApplying(e);
    }

    protected ArticleEvent map(ArticleCommand.CreateArticle c) {
        ArticleEventId stateEventId = new ArticleEventId(c.getArticleId(), c.getVersion());
        ArticleEvent.ArticleStateCreated e = newArticleStateCreated(stateEventId);
        e.setTitle(c.getTitle());
        e.setBody(c.getBody());
        e.setAuthor(c.getAuthor());
        ((AbstractArticleEvent.AbstractArticleStateEvent)e).setTags(c.getTags() == null ? null : java.util.Arrays.stream(c.getTags()).collect(java.util.stream.Collectors.toSet()));
        ((AbstractArticleEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        Long version = c.getVersion();
        for (CommentCommand.CreateComment innerCommand : c.getCreateCommentCommands()) {
            throwOnInconsistentCommands(c, innerCommand);
            CommentEvent.CommentStateCreated innerEvent = mapCreate(innerCommand, c, version, this.state);
            e.addCommentEvent(innerEvent);
        }

        return e;
    }

    protected ArticleEvent map(ArticleCommand.MergePatchArticle c) {
        ArticleEventId stateEventId = new ArticleEventId(c.getArticleId(), c.getVersion());
        ArticleEvent.ArticleStateMergePatched e = newArticleStateMergePatched(stateEventId);
        e.setTitle(c.getTitle());
        e.setBody(c.getBody());
        e.setAuthor(c.getAuthor());
        ((AbstractArticleEvent.AbstractArticleStateEvent)e).setTags(c.getTags() == null ? null : java.util.Arrays.stream(c.getTags()).collect(java.util.stream.Collectors.toSet()));
        e.setIsPropertyTitleRemoved(c.getIsPropertyTitleRemoved());
        e.setIsPropertyBodyRemoved(c.getIsPropertyBodyRemoved());
        e.setIsPropertyAuthorRemoved(c.getIsPropertyAuthorRemoved());
        e.setIsPropertyTagsRemoved(c.getIsPropertyTagsRemoved());
        ((AbstractArticleEvent)e).setCommandId(c.getCommandId());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        Long version = c.getVersion();
        for (CommentCommand innerCommand : c.getCommentCommands()) {
            throwOnInconsistentCommands(c, innerCommand);
            CommentEvent innerEvent = map(innerCommand, c, version, this.state);
            e.addCommentEvent(innerEvent);
        }

        return e;
    }


    protected CommentEvent map(CommentCommand c, ArticleCommand outerCommand, Long version, ArticleState outerState) {
        CommentCommand.CreateComment create = (c.getCommandType().equals(CommandType.CREATE)) ? ((CommentCommand.CreateComment)c) : null;
        if(create != null) {
            return mapCreate(create, outerCommand, version, outerState);
        }

        CommentCommand.MergePatchComment merge = (c.getCommandType().equals(CommandType.MERGE_PATCH)) ? ((CommentCommand.MergePatchComment)c) : null;
        if(merge != null) {
            return mapMergePatch(merge, outerCommand, version, outerState);
        }

        throw new UnsupportedOperationException("Unsupported command type: " + c.getCommandType() + " for " + c.getClass().getName());
    }

    protected CommentEvent.CommentStateCreated mapCreate(CommentCommand.CreateComment c, ArticleCommand outerCommand, Long version, ArticleState outerState) {
        ((AbstractCommand)c).setRequesterId(outerCommand.getRequesterId());
        CommentEventId stateEventId = new CommentEventId(outerState.getArticleId(), c.getCommentSeqId(), version);
        CommentEvent.CommentStateCreated e = newCommentStateCreated(stateEventId);
        CommentState s = ((EntityStateCollection.MutableEntityStateCollection<Long, CommentState>)outerState.getComments()).getOrAddDefault(c.getCommentSeqId());

        e.setCommenter(c.getCommenter());
        e.setBody(c.getBody());
        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

        return e;

    }// END map(ICreate... ////////////////////////////

    protected CommentEvent.CommentStateMergePatched mapMergePatch(CommentCommand.MergePatchComment c, ArticleCommand outerCommand, Long version, ArticleState outerState) {
        ((AbstractCommand)c).setRequesterId(outerCommand.getRequesterId());
        CommentEventId stateEventId = new CommentEventId(outerState.getArticleId(), c.getCommentSeqId(), version);
        CommentEvent.CommentStateMergePatched e = newCommentStateMergePatched(stateEventId);
        CommentState s = ((EntityStateCollection.MutableEntityStateCollection<Long, CommentState>)outerState.getComments()).getOrAddDefault(c.getCommentSeqId());

        e.setCommenter(c.getCommenter());
        e.setBody(c.getBody());
        e.setIsPropertyCommenterRemoved(c.getIsPropertyCommenterRemoved());
        e.setIsPropertyBodyRemoved(c.getIsPropertyBodyRemoved());

        e.setCreatedBy(c.getRequesterId());
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

        return e;

    }// END map(IMergePatch... ////////////////////////////

    protected void throwOnInconsistentCommands(ArticleCommand command, CommentCommand innerCommand) {
        AbstractArticleCommand properties = command instanceof AbstractArticleCommand ? (AbstractArticleCommand) command : null;
        AbstractCommentCommand innerProperties = innerCommand instanceof AbstractCommentCommand ? (AbstractCommentCommand) innerCommand : null;
        if (properties == null || innerProperties == null) { return; }
        String outerArticleIdName = "ArticleId";
        Long outerArticleIdValue = properties.getArticleId();
        String innerArticleIdName = "ArticleId";
        Long innerArticleIdValue = innerProperties.getArticleId();
        if (innerArticleIdValue == null) {
            innerProperties.setArticleId(outerArticleIdValue);
        }
        else if (innerArticleIdValue != outerArticleIdValue 
            && (innerArticleIdValue == null || innerArticleIdValue != null && !innerArticleIdValue.equals(outerArticleIdValue))) {
            throw DomainError.named("inconsistentId", "Outer %1$s %2$s NOT equals inner %3$s %4$s", outerArticleIdName, outerArticleIdValue, innerArticleIdName, innerArticleIdValue);
        }
    }// END throwOnInconsistentCommands /////////////////////


    ////////////////////////

    protected ArticleEvent.ArticleStateCreated newArticleStateCreated(Long version, String commandId, String requesterId) {
        ArticleEventId stateEventId = new ArticleEventId(this.state.getArticleId(), version);
        ArticleEvent.ArticleStateCreated e = newArticleStateCreated(stateEventId);
        ((AbstractArticleEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected ArticleEvent.ArticleStateMergePatched newArticleStateMergePatched(Long version, String commandId, String requesterId) {
        ArticleEventId stateEventId = new ArticleEventId(this.state.getArticleId(), version);
        ArticleEvent.ArticleStateMergePatched e = newArticleStateMergePatched(stateEventId);
        ((AbstractArticleEvent)e).setCommandId(commandId);
        e.setCreatedBy(requesterId);
        e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));
        return e;
    }

    protected ArticleEvent.ArticleStateCreated newArticleStateCreated(ArticleEventId stateEventId) {
        return new AbstractArticleEvent.SimpleArticleStateCreated(stateEventId);
    }

    protected ArticleEvent.ArticleStateMergePatched newArticleStateMergePatched(ArticleEventId stateEventId) {
        return new AbstractArticleEvent.SimpleArticleStateMergePatched(stateEventId);
    }

    protected CommentEvent.CommentStateCreated newCommentStateCreated(CommentEventId stateEventId) {
        return new AbstractCommentEvent.SimpleCommentStateCreated(stateEventId);
    }

    protected CommentEvent.CommentStateMergePatched newCommentStateMergePatched(CommentEventId stateEventId) {
        return new AbstractCommentEvent.SimpleCommentStateMergePatched(stateEventId);
    }


    public static class SimpleArticleAggregate extends AbstractArticleAggregate {
        public SimpleArticleAggregate(ArticleState state) {
            super(state);
        }

        @Override
        public void updateBody(String body, Long version, String commandId, String requesterId, ArticleCommands.UpdateBody c) {
            java.util.function.Supplier<ArticleEvent.ArticleBodyUpdated> eventFactory = () -> newArticleBodyUpdated(body, version, commandId, requesterId);
            ArticleEvent.ArticleBodyUpdated e = verifyUpdateBody(eventFactory, body, c);
            apply(e);
        }

        protected ArticleEvent.ArticleBodyUpdated verifyUpdateBody(java.util.function.Supplier<ArticleEvent.ArticleBodyUpdated> eventFactory, String body, ArticleCommands.UpdateBody c) {
            String Body = body;

            ArticleEvent.ArticleBodyUpdated e = (ArticleEvent.ArticleBodyUpdated) ApplicationContext.current.get(IUpdateBodyLogic.class).verify(
                    eventFactory, getState(), body, VerificationContext.of(c));

            return e;
        }

        protected AbstractArticleEvent.ArticleBodyUpdated newArticleBodyUpdated(String body, Long version, String commandId, String requesterId) {
            ArticleEventId eventId = new ArticleEventId(getState().getArticleId(), version);
            AbstractArticleEvent.ArticleBodyUpdated e = new AbstractArticleEvent.ArticleBodyUpdated();

            e.getDynamicProperties().put("body", body);

            e.setCommandId(commandId);
            e.setCreatedBy(requesterId);
            e.setCreatedAt((OffsetDateTime)ApplicationContext.current.getTimestampService().now(OffsetDateTime.class));

            e.setArticleEventId(eventId);
            return e;
        }

    }

}

