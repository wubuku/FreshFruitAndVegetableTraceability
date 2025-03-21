// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractArticleCommand extends AbstractCommand implements ArticleCommand {

    private Long articleId;

    public Long getArticleId()
    {
        return this.articleId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public static abstract class AbstractCreateOrMergePatchArticle extends AbstractArticleCommand implements CreateOrMergePatchArticle
    {
        private String title;

        public String getTitle()
        {
            return this.title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        private String body;

        public String getBody()
        {
            return this.body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }

        private String author;

        public String getAuthor()
        {
            return this.author;
        }

        public void setAuthor(String author)
        {
            this.author = author;
        }

        private String[] tags;

        public String[] getTags() {
            return this.tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public CommentCommand.CreateComment newCreateComment()
        {
            AbstractCommentCommand.SimpleCreateComment c = new AbstractCommentCommand.SimpleCreateComment();
            c.setArticleId(this.getArticleId());

            return c;
        }

        public CommentCommand.MergePatchComment newMergePatchComment()
        {
            AbstractCommentCommand.SimpleMergePatchComment c = new AbstractCommentCommand.SimpleMergePatchComment();
            c.setArticleId(this.getArticleId());

            return c;
        }

        public CommentCommand.RemoveComment newRemoveComment()
        {
            AbstractCommentCommand.SimpleRemoveComment c = new AbstractCommentCommand.SimpleRemoveComment();
            c.setArticleId(this.getArticleId());

            return c;
        }

    }

    public static abstract class AbstractCreateArticle extends AbstractCreateOrMergePatchArticle implements CreateArticle
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

        private CreateCommentCommandCollection createCommentCommands = new SimpleCreateCommentCommandCollection();

        public CreateCommentCommandCollection getCreateCommentCommands() {
            return this.createCommentCommands;
        }

        public CreateCommentCommandCollection getComments() {
            return this.createCommentCommands; //comments;
        }

    }

    public static abstract class AbstractMergePatchArticle extends AbstractCreateOrMergePatchArticle implements MergePatchArticle
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyTitleRemoved;

        public Boolean getIsPropertyTitleRemoved()
        {
            return this.isPropertyTitleRemoved;
        }

        public void setIsPropertyTitleRemoved(Boolean removed)
        {
            this.isPropertyTitleRemoved = removed;
        }

        private Boolean isPropertyBodyRemoved;

        public Boolean getIsPropertyBodyRemoved()
        {
            return this.isPropertyBodyRemoved;
        }

        public void setIsPropertyBodyRemoved(Boolean removed)
        {
            this.isPropertyBodyRemoved = removed;
        }

        private Boolean isPropertyAuthorRemoved;

        public Boolean getIsPropertyAuthorRemoved()
        {
            return this.isPropertyAuthorRemoved;
        }

        public void setIsPropertyAuthorRemoved(Boolean removed)
        {
            this.isPropertyAuthorRemoved = removed;
        }

        private Boolean isPropertyTagsRemoved;

        public Boolean getIsPropertyTagsRemoved()
        {
            return this.isPropertyTagsRemoved;
        }

        public void setIsPropertyTagsRemoved(Boolean removed)
        {
            this.isPropertyTagsRemoved = removed;
        }


        private CommentCommandCollection commentCommands = new SimpleCommentCommandCollection();

        public CommentCommandCollection getCommentCommands()
        {
            return this.commentCommands;
        }

    }

    public static class SimpleCreateArticle extends AbstractCreateArticle
    {
    }

    
    public static class SimpleMergePatchArticle extends AbstractMergePatchArticle
    {
    }

    
    public static class SimpleDeleteArticle extends AbstractArticleCommand implements DeleteArticle
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    
    public static class SimpleCreateCommentCommandCollection implements CreateCommentCommandCollection {
        private List<CommentCommand.CreateComment> innerCommands = new ArrayList<CommentCommand.CreateComment>();

        public void add(CommentCommand.CreateComment c) {
            innerCommands.add(c);
        }

        public void remove(CommentCommand.CreateComment c) {
            innerCommands.remove(c);
        }

        public void clear() {
            innerCommands.clear();
        }

        @Override
        public Iterator<CommentCommand.CreateComment> iterator() {
            return innerCommands.iterator();
        }
    }

    public static class SimpleCommentCommandCollection implements CommentCommandCollection {
        private List<CommentCommand> innerCommands = new ArrayList<CommentCommand>();

        public void add(CommentCommand c) {
            innerCommands.add(c);
        }

        public void remove(CommentCommand c) {
            innerCommands.remove(c);
        }

        public void clear() {
            innerCommands.clear();
        }

        @Override
        public Iterator<CommentCommand> iterator() {
            return innerCommands.iterator();
        }
    }


}

